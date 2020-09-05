package com.dotop.smartwater.project.module.api.revenue.impl;

import com.dotop.smartwater.dependence.cache.StringValueCache;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.dependence.core.utils.BeanUtils;
import com.dotop.smartwater.dependence.core.utils.DateUtils;
import com.dotop.smartwater.dependence.core.utils.JSONUtils;
import com.dotop.smartwater.dependence.core.utils.VerificationUtils;
import com.dotop.smartwater.project.module.api.revenue.*;
import com.dotop.smartwater.project.module.service.revenue.*;
import com.dotop.smartwater.project.module.api.revenue.*;
import com.dotop.smartwater.project.module.api.revenue.async.CalPreviewBillsFactoryImpl;
import com.dotop.smartwater.project.module.core.auth.local.AuthCasClient;
import com.dotop.smartwater.project.module.core.auth.local.IAuthCasClient;
import com.dotop.smartwater.project.module.core.auth.vo.AreaNodeVo;
import com.dotop.smartwater.project.module.core.auth.vo.SettlementVo;
import com.dotop.smartwater.project.module.core.auth.vo.UserVo;
import com.dotop.smartwater.project.module.core.third.form.iot.MeterDataForm;
import com.dotop.smartwater.project.module.core.water.bo.*;
import com.dotop.smartwater.project.module.core.water.bo.customize.LadderPriceDetailBo;
import com.dotop.smartwater.project.module.core.water.config.Config;
import com.dotop.smartwater.project.module.core.water.constants.*;
import com.dotop.smartwater.project.module.core.water.enums.ErrorType;
import com.dotop.smartwater.project.module.core.water.form.*;
import com.dotop.smartwater.project.module.core.water.form.customize.PreviewForm;
import com.dotop.smartwater.project.module.core.water.utils.BusinessUtil;
import com.dotop.smartwater.project.module.core.water.utils.CalUtil;
import com.dotop.smartwater.project.module.core.water.vo.*;
import com.dotop.smartwater.project.module.core.water.vo.customize.LadderPriceDetailVo;
import com.dotop.smartwater.project.module.core.water.vo.customize.LastUpLinkVo;
import com.dotop.smartwater.project.module.core.water.vo.customize.OwnerDeviceVo;
import com.dotop.smartwater.project.module.service.device.IDeviceService;
import com.dotop.smartwater.project.module.service.device.IDeviceUplinkService;
import com.dotop.smartwater.project.module.service.revenue.*;
import com.dotop.smartwater.project.module.service.tool.ISettlementService;
import com.dotop.water.tool.service.BaseInf;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

/**
 * 用户账单
 *

 * @date 2019年2月25日
 */
@Component
public class OrderFactoryImpl implements IOrderFactory, IAuthCasClient {

    private final static Logger logger = LogManager.getLogger(OrderFactoryImpl.class);

    @Autowired
    private IOrderService iOrderService;

    @Autowired
    private IOwnerService iOwnerService;

    @Autowired
    private IDeviceService iDeviceService;

    @Autowired
    private IDeviceUplinkService iDeviceUplinkService;

    @Autowired
    private IWrongAccountService iWrongAccountService;

    @Autowired
    private ICouponService iCouponService;

    @Autowired
    private IPayTypeService iPayTypeService;

    @Autowired
    private IPurposeService iPurposeService;

    @Autowired
    private IReduceService iReduceService;

    @Autowired
    private StringValueCache svc;

    @Autowired
    private ICalPreviewBillsFactory iCalPreviewBillsFactory;

    @Autowired
    private IMakeOrderFactory iMakeOrderFactory;

    @Autowired
    private IPaymentFactory iPaymentFactory;

    @Autowired
    private IDeleteOrderExtTaskFactory iDeleteOrderExtTaskFactory;

    @Autowired
    private ISettlementService iSettlementService;

    @Override
    public OrderVo get(OrderForm orderForm) throws FrameworkRuntimeException {
        return iOrderService.findOrderByTradeNo(orderForm.getTradeno());
    }

    @Override
    public OwnerVo getOwnerUser(OwnerForm ownerForm) throws FrameworkRuntimeException {
        UserVo user = AuthCasClient.getUser();
        OwnerBo ownerBo = new OwnerBo();
        ownerBo.setEnterpriseid(user.getEnterpriseid());
        ownerBo.setUserno(ownerForm.getUserno());
        OwnerVo ownerVo = iOwnerService.getOwnerUser(ownerBo);

        if (ownerVo == null) {
            throw new FrameworkRuntimeException(ResultCode.Fail, "没有该编号的业主");
        }

        /**
         * 获区域名称
         **/
        Map<String, AreaNodeVo> map = BaseInf.getAreaMaps(user.getUserid(), user.getTicket());
        if (map != null && map.size() != 0) {
            AreaNodeVo areaNode = map.get(ownerVo.getCommunityid());
            if (areaNode != null) {
                ownerVo.setCommunityname(areaNode.getTitle());
            }
        }
        return ownerVo;
    }

    @Override
    public Pagination<OrderPreviewVo> OrderPreviewList(PreviewForm previewForm) throws FrameworkRuntimeException {
        UserVo user = AuthCasClient.getUser();
        return iOrderService.orderPreviewList(previewForm);
    }

    @Override
    public void generateOrder(PreviewForm previewForm) throws FrameworkRuntimeException {
        UserVo user = AuthCasClient.getUser();
        List<OwnerDeviceVo> ownerDevices;

        String year = previewForm.getMonth().substring(0, 4);
        String month = previewForm.getMonth().substring(4, 6);

        // 截止时间
        String[] data = previewForm.getMetertime().split("-");
        if (data.length != OrderGenerateType.DATE_STRING_ARRAY_LENGTH) {
            throw new FrameworkRuntimeException(ResultCode.ParamIllegal, "预览账单截止时间格式错误", null);
        }
        // 得到一个Calendar的实例
        Calendar ca = Calendar.getInstance();
        // 月份是从0开始的，所以11表示12月
        ca.set(Integer.parseInt(data[0]), Integer.parseInt(data[1]) - 1, Integer.parseInt(data[2]));
        // 月份减1
        ca.add(Calendar.MONTH, -1);
        // 结果
        String CurrentMonth = data[0] + data[1];
        String LastMonth = DateUtils.format(ca.getTime(), DateUtils.YYYYMM);
        String meterTime = previewForm.getMetertime() + " 23:59:59";

        switch (previewForm.getType()) {
            case OrderGenerateType.COMMUNITY:
                // 根据选择的区域获取业主和水表信息
                long yy = System.currentTimeMillis();
                ownerDevices = iOwnerService.findOwnerByCommunityIds(previewForm.getCommunityIds(), getEnterpriseid());
                logger.info("owners:{}", (System.currentTimeMillis() - yy));
                break;
            case OrderGenerateType.OWNER:
                // 根据业主编号获取业主信息
                ownerDevices = iOwnerService.findOwnerByOwnernos(previewForm.getUsernos(), getEnterpriseid());
                break;
            default:
                throw new FrameworkRuntimeException(ResultCode.ParamIllegal, "没知的生成方式", null);
        }

        if (ownerDevices == null || ownerDevices.isEmpty()) {
            throw new FrameworkRuntimeException(ResultCode.ParamIllegal, "没有获取到业主信息或者业主已生成预览", null);
        }

        Map<String, AreaNodeVo> map = BaseInf.getAreaMaps(user.getUserid(), user.getTicket());

        if (map != null && map.size() != 0) {
            for (OwnerDeviceVo o : ownerDevices) {
                AreaNodeVo areaNode = map.get(String.valueOf(o.getCommunityid()));
                if (areaNode == null) {
                    continue;
                }
                o.setComname(areaNode.getTitle());
            }
        }

        try {
            // 批量获取当前业主水表读数,写入map
            long tt = System.currentTimeMillis();
            Map<String, LastUpLinkVo> waterMap = iOrderService.findLastUplinkList(previewForm, CurrentMonth, LastMonth,
                    meterTime);
            logger.info("waterMap:{}", (System.currentTimeMillis() - tt));

            tt = System.currentTimeMillis();
            Map<String, OrderVo> orderMap = null;
            if (OrderGenerateType.OWNER.equals(previewForm.getType())) {
                orderMap = iOrderService.findByUserNoMap(user.getEnterpriseid(), previewForm.getUsernos());
            } else {
                orderMap = iOrderService.findByUserNoMap(user.getEnterpriseid(), null);
            }

            logger.info("orderMap:{}", (System.currentTimeMillis() - tt));
            int size = ownerDevices.size();
            int unitNum = 2000;
            int startIndex = 0;
            int endIndex = 0;

            List<Future<Integer>> futurelist = new ArrayList<>();

            // TODO 生成账单 异步任务
            while (size > 0) {
                if (size > unitNum) {
                    endIndex = startIndex + unitNum;
                } else {
                    endIndex = startIndex + size;
                }

                Future<Integer> future = iCalPreviewBillsFactory.asyncCalPreviewBill(previewForm,
                        ownerDevices.subList(startIndex, endIndex), waterMap, orderMap, year, month, user);
                futurelist.add(future);
                size = size - unitNum;
                startIndex = endIndex;
            }

            for (Future<Integer> f : futurelist) {
                f.get();
            }

            svc.del(OrderGenerateType.GENERATE_ORDER_SWITCH + user.getEnterpriseid());
        } catch (Exception e) {

        }

    }

    @Override
    public void deletePreview(OwnerForm ownerForm) throws FrameworkRuntimeException {
        UserVo user = AuthCasClient.getUser();
        iOrderService.deletePreview(ownerForm.getOwnerid());
    }

    @Override
    public void clearPreview(PreviewForm previewForm, boolean flag) throws FrameworkRuntimeException {
        UserVo user = AuthCasClient.getUser();
        iOrderService.clearPreview(previewForm, flag);
    }

    @Override
    public void makeOrder(PreviewForm previewForm) throws FrameworkRuntimeException {
        UserVo user = AuthCasClient.getUser();
        List<OrderPreviewVo> orders = iOrderService.findOrderPreview(previewForm);

        if (iOrderService.addOrders(previewForm) > 0) {

            iOrderService.clearPreview(previewForm, false);

            // TODO 异步任务
            // 发生成账单的消息推送
            // makeOrder.batchSendmakeOrderExecutor(orders);
            // 勾选自动扣费处理逻辑,一并处理<账单金额等于0则直接将账单设置为已缴费>
            // makeOrder.asyncAutoPay(user);
        } else {
            throw new FrameworkRuntimeException(ResultCode.Fail, "没有新的账单", null);
        }
    }

    @Override
    public Pagination<OrderVo> bills(OrderPreviewForm orderPreviewForm) throws FrameworkRuntimeException {
        UserVo user = AuthCasClient.getUser();
        Map<String, AreaNodeVo> map = BaseInf.getAreaMaps(user.getUserid(), user.getTicket());
        if (map == null || map.size() == 0) {
            throw new FrameworkRuntimeException(ResultCode.Fail, "系统未给您分配区域无法查询", null);
        }

        StringBuilder cIds = new StringBuilder();
        for (String key : map.keySet()) {
            cIds.append(key);
            cIds.append(",");
        }
        String ids = cIds.substring(0, cIds.length() - 1);

        Pagination<OrderVo> pagination;
        OrderPreviewBo orderPreviewBo = new OrderPreviewBo();
        BeanUtils.copy(orderPreviewForm, orderPreviewBo);

        orderPreviewBo.setEnterpriseid(user.getEnterpriseid());
        // 水司普通用户根据区域分配
        if (user.getType().equals(UserVo.USER_TYPE_ENTERPRISE_NORMAL)) {
            pagination = iOrderService.bills(orderPreviewBo, ids);
            return pagination;
        }

        pagination = iOrderService.bills(orderPreviewBo, null);
        return pagination;
    }

    @Override
    public Pagination<OrderVo> orderList(OrderPreviewForm orderPreviewForm) throws FrameworkRuntimeException {
        UserVo user = AuthCasClient.getUser();
        orderPreviewForm.setEnterpriseid(user.getEnterpriseid());
        OrderPreviewBo orderPreviewBo = new OrderPreviewBo();
        BeanUtils.copy(orderPreviewForm, orderPreviewBo);
        return iOrderService.orderList(orderPreviewBo);
    }

    @Override
    public void deleteOrder(OrderForm orderForm) throws FrameworkRuntimeException {
        UserVo user = AuthCasClient.getUser();
        OrderVo order = iOrderService.findById(orderForm.getId());
        if (order == null) {
            throw new FrameworkRuntimeException(ResultCode.ParamIllegal, "没该缴费记录", null);
        }
        if (order.getPaystatus() == WaterConstants.ORDER_PAYSTATUS_PAID) {
            throw new FrameworkRuntimeException(ResultCode.ParamIllegal, "已缴费,不能撤销", null);
        }

        WrongAccountBo wrongAccountBo = new WrongAccountBo();
        wrongAccountBo.setOrderid(order.getId());
        if (iWrongAccountService.isExist(wrongAccountBo)) {
            throw new FrameworkRuntimeException(ResultCode.ParamIllegal, "已经进行错账处理,不能撤销", null);
        }
        OrderBo orderBo = new OrderBo();
        BeanUtils.copy(orderForm, orderBo);
        iOrderService.deleteById(orderBo);
    }

    @Override
    public void signNormal(OrderForm orderForm) throws FrameworkRuntimeException {
        UserVo user = AuthCasClient.getUser();
        OrderVo order = iOrderService.findByOwnerId(orderForm.getOwnerid());
        if (order != null && order.getErrortype() == ErrorType.OFFLINEDEVICE.intValue()) {
            iOrderService.updateOrderPreviewStatus(order.getOwnerid());
        } else {
            throw new FrameworkRuntimeException(ResultCode.Fail, "当前账单不能标记为正常", null);
        }
    }

    @Override
    public PayTypeVo findOrderPayTypeDetail(OrderForm orderForm) throws FrameworkRuntimeException {
        UserVo user = AuthCasClient.getUser();
        OrderExtVo orderExt = iOrderService.findOrderExtByTradeno(orderForm.getTradeno());
        if (orderExt == null) {
            throw new FrameworkRuntimeException(ResultCode.ParamIllegal, "错误的账单", null);
        }

        PayTypeVo payType = JSONUtils.parseObject(orderExt.getPaytypeinfo(), PayTypeVo.class);
        if (payType == null) {
            throw new FrameworkRuntimeException(ResultCode.ParamIllegal, "没有找到该账单的收费种类明细", null);
        }
        return payType;
    }

    @Override
    public OrderExtVo loadOrderExt(OrderForm orderForm) throws FrameworkRuntimeException {
        UserVo user = AuthCasClient.getUser();

        OwnerBo ownerBo = new OwnerBo();
        ownerBo.setOwnerid(orderForm.getOwnerid());
        OwnerVo owner = iOwnerService.findByOwnerId(ownerBo);
        if (owner == null) {
            throw new FrameworkRuntimeException(ResultCode.ParamIllegal, "不存在的业主", null);
        }

        OrderVo order = iOrderService.findById(orderForm.getId());
        if (order == null) {
            throw new FrameworkRuntimeException(ResultCode.ParamIllegal, "没有该记录", null);
        }

        OrderExtVo orderExt = iOrderService.findOrderExtByTradeno(order.getTradeno());
        if (orderExt == null) {
            throw new FrameworkRuntimeException(ResultCode.ParamIllegal, "错误的账单", null);
        }
        PayTypeVo payType = JSONUtils.parseObject(orderExt.getPaytypeinfo(), PayTypeVo.class);
        if (payType == null) {
            throw new FrameworkRuntimeException(ResultCode.ParamIllegal, "不存在的收费种类,请先设置收费种类，再生成账单", null);
        }

        OrderExtVo orderExtVo = new OrderExtVo();
        orderExtVo.setId(order.getId());
        orderExtVo.setOrder(order);
        orderExtVo.setCardid(owner.getCardid());
        orderExtVo.setIschargebacks(owner.getIschargebacks());
        if (owner.getAlreadypay() == null || owner.getAlreadypay() < 0) {
            orderExtVo.setAlreadypay(0.0);
        } else {
            orderExtVo.setAlreadypay(owner.getAlreadypay());
        }

        List<LadderPriceDetailVo> payDetailList = JSONUtils.parseArray(orderExt.getChargeinfo(),
                LadderPriceDetailVo.class);

        try{
            if (payDetailList != null && payDetailList.size() > 0) {
                if (iOrderService.findSubOrderCountByTradeNo(order.getTradeno()) == 0) {
                    Map<String, LadderPriceDetailVo> payDetailMap = payDetailList.stream()
                            .collect(Collectors.toMap(x -> x.getName(), x -> x));
                    LadderPriceDetailVo lpd = payDetailMap.get("保底收费");
                    if (lpd != null) {
                        payDetailList.clear();
                        payDetailList.add(lpd);
                    }
                }
            }
        }catch (Exception e){
            logger.error(e.getMessage());
            payDetailList = new ArrayList<>();
        }

        orderExtVo.setPayDetailList(payDetailList);
        // 滞纳金
        List<LadderPriceDetailBo> payDetailListBo = BeanUtils.copy(payDetailList, LadderPriceDetailBo.class);
        List<CompriseBo> compriseBos = BeanUtils.copy(payType.getComprises(), CompriseBo.class);

        orderExtVo.setPenalty(BusinessUtil.getPenalty(payDetailListBo, compriseBos, order.getGeneratetime(),
                payType.getOverdueday()));
        orderExtVo.setRealamount(CalUtil.add(order.getAmount(), orderExtVo.getPenalty()));

        if (order.getAmount() == null) {
            orderExtVo.setOwnerpay(null);
        } else {
            orderExtVo.setOwnerpay(new BigDecimal(Double.toString(orderExtVo.getRealamount())));
        }

        CouponVo conpon = JSONUtils.parseObject(orderExt.getCouponinfo(), CouponVo.class);
        if (conpon != null) {
            List<CouponVo> couponList = new ArrayList<>();
            couponList.add(conpon);
            orderExtVo.setCouponList(couponList);
            orderExtVo.setCouponid(conpon.getCouponid());
        }
        orderExtVo.setBalance(order.getBalance());
        // 实收
        orderExtVo.setOwnerpay(iOrderService.findOwnerPayByPayNo(order.getPayno()));
        // 滞纳金 paystatus
        if (order.getPaystatus() == 1) { // 已缴账单
            orderExtVo.setPenalty(orderExt.getPenalty());
        }
        // 水表用途
        PurposeVo purpose = JSONUtils.parseObject(orderExt.getPurposeinfo(), PurposeVo.class);
        orderExtVo.setPurpose(purpose);
        // 减免类型
        ReduceVo reduce = JSONUtils.parseObject(orderExt.getReduceinfo(), ReduceVo.class);
        orderExtVo.setReduce(reduce);
        // 交易流水
        TradePayVo trade = iOrderService.getTradePay(order.getTradeno());
        orderExtVo.setTradepay(trade);
        return orderExtVo;
    }

    @Override
    public void updatePayStatus(OrderExtForm orderExtForm) throws FrameworkRuntimeException {
        UserVo user = AuthCasClient.getUser();
        OrderVo order = iOrderService.findOrderByTradeNo(orderExtForm.getTradeno());
        TradePayVo trade = iOrderService.getTradePay(orderExtForm.getTradeno());
        if (trade == null) {
            throw new FrameworkRuntimeException(ResultCode.Fail, "未查询到交易信息", null);
        }
        iOrderService.getOrderPayStatus(order, trade.getPayno(), user.getEnterpriseid());
    }

    @Override
    public Map<String, Object> orderPay(OrderExtForm orderExtForm) throws FrameworkRuntimeException {
        UserVo user = AuthCasClient.getUser();
        String[] ids = orderExtForm.getOrderids().split(",");
        // 验证单个账单或者是多个账单，如果多账单支付则合并账单为新的账单，原账单隐藏
        if (ids.length == 1) {
            orderExtForm.setId(orderExtForm.getOrderids());
        } else if (ids.length > 1) {
            orderExtForm.setId(iOrderService.mergeOrders(orderExtForm.getOrderids(), user));
        }

        OrderVo order = iOrderService.findById(orderExtForm.getId());
        if (order == null) {
            throw new FrameworkRuntimeException(ResultCode.ParamIllegal, "没该缴费记录", null);
        }

        OrderExtVo orderExt = iOrderService.findOrderExtByTradeno(order.getTradeno());
        if (orderExt == null) {
            throw new FrameworkRuntimeException(ResultCode.ParamIllegal, "错误的账单", null);
        }
        PayTypeVo payType = JSONUtils.parseObject(orderExt.getPaytypeinfo(), PayTypeVo.class);
        if (payType == null) {
            throw new FrameworkRuntimeException(ResultCode.ParamIllegal, "不存在的收费种类", null);
        }

        if (order.getPaystatus() == WaterConstants.ORDER_PAYSTATUS_PAID) {
            throw new FrameworkRuntimeException(ResultCode.ParamIllegal, "已缴费的账单", null);
        }

        // 校验业主账号余额
        OwnerBo ownerBo = new OwnerBo();
        ownerBo.setOwnerid(order.getOwnerid());
        OwnerVo owner = iOwnerService.findByOwnerId(ownerBo);
        if (!orderExtForm.getAlreadypay().equals(owner.getAlreadypay())) {
            throw new FrameworkRuntimeException(ResultCode.ParamIllegal, "业主账号余额不正确", null);
        }
        // 检验余额抵扣
        Double ownerBalance = orderExtForm.getAlreadypay() - orderExtForm.getBalance();
        if (ownerBalance < 0.0) {
            throw new FrameworkRuntimeException(ResultCode.ParamIllegal, "抵扣值不能大于业主余额", null);
        }

        Double amount = order.getAmount();

        // 水费组成明细
        List<LadderPriceDetailVo> payDetailList = BusinessUtil.getLadderPriceDetailVo(payType.getLadders(),
                order.getWater());

        // 校验滞纳金
        List<LadderPriceDetailBo> payDetailListBo = BeanUtils.copy(payDetailList, LadderPriceDetailBo.class);
        List<CompriseBo> compriseBos = BeanUtils.copy(payType.getComprises(), CompriseBo.class);
        Double penalty = 0.0;
        if (ids.length == 1) {
            penalty = BusinessUtil.getPenalty(payDetailListBo, compriseBos, order.getGeneratetime(),
                    payType.getOverdueday());
            if (!penalty.equals(orderExtForm.getPenalty())) {
                throw new FrameworkRuntimeException(ResultCode.ParamIllegal, "滞纳金金额计算不正确,不能进行缴费", null);
            }
            amount = CalUtil.add(amount, penalty);
        }

        CouponVo coupon = null;
        if (orderExtForm.getCouponid() != null) {
            CouponBo couponBo = new CouponBo();
            couponBo.setCouponid(orderExtForm.getCouponid());
            coupon = iCouponService.getCoupon(couponBo);
            if (coupon != null) {
                // 当代金卷金额大于应缴金额,则代金卷抵扣等于应缴金额
                if (amount < coupon.getFacevalue()) {
                    coupon.setFacevalue(amount);
                    amount = 0.0;
                } else {
                    amount = CalUtil.sub(amount, coupon.getFacevalue());
                }
            }
        }

        // 检验实收金额
        amount = CalUtil.sub(amount, orderExtForm.getBalance());
        if (amount < 0) {
            amount = 0.0;
        }

        if (orderExtForm.getRealamount() == null) {
            orderExtForm.setRealamount(order.getRealamount());
        }

        if (!amount.equals(orderExtForm.getRealamount())) {
            throw new FrameworkRuntimeException(ResultCode.ParamIllegal, "剩余应缴金额计算不正确,不能进行缴费", null);
        }
        if (orderExtForm.getOwnerpay().doubleValue() - amount < 0 && orderExtForm.getPayMode() != null
                && orderExtForm.getPayMode().equals(1)) {
            // 现金支付时验证实收金额是否小于剩余应缴
            throw new FrameworkRuntimeException(ResultCode.ParamIllegal, "实收金额小于剩余应缴金额,不能进行缴费", null);
        }

        int payMode = 1;
        if (StringUtils.isNotBlank(orderExtForm.getPayMode())) {
            payMode = Integer.valueOf(orderExtForm.getPayMode());
        }
        // TODO 不知道干啥
        // view.setIp(IpAdrressUtil.getIpAdrress(request));
        Map<String, String> payMap = iOrderService.orderPay(orderExtForm, order, user, ownerBalance, payMode, coupon);
        if (payMap.get("return_code") != null && payMap.get("return_code").equals("SUCCESS")) {
            // TODO 发短信和微信消息

            Map<String, Object> map = new HashMap<>(10);

            // TODO 获取打印信息
            /*
             * PrintVo printVo = PrintInf.getPrintStatus(owner.getEnterpriseid(),
             * SmsEnum.pay_fee.intValue()); if (printVo != null) { map.put("designprintid",
             * printVo.getDesignprintid()); map.put("printstatus",
             * printVo.getPrintstatus()); map.put("tradeno", order.getTradeno()); }
             */
            return map;
        } else {
            throw new FrameworkRuntimeException(ResultCode.Fail, payMap.get("return_msg"), null);
        }
    }

    @Override
    public Map<String, Object> supplementPrint(OrderExtForm orderExtForm,PrintBindVo printBindVo) throws FrameworkRuntimeException {

        OwnerBo ownerBo = new OwnerBo();
        ownerBo.setOwnerid(orderExtForm.getOwnerid());
        OwnerVo owner = iOwnerService.findByOwnerId(ownerBo);

        Map<String, Object> map = new HashMap<>(4);
        if (printBindVo != null) {
            map.put("designprintid", printBindVo.getDesignprintid());
            map.put("printstatus", printBindVo.getPrintstatus());
        }

        map.put("tradeno", orderExtForm.getTradeno());
        map.put("ownerid", owner.getOwnerid());
        return map;
    }

    @Override
    public void manualMeter(MeterDataForm meterDataForm) throws FrameworkRuntimeException {
        DeviceVo d = iDeviceService.findByDevEUI(meterDataForm.getDeveui());
        if (d == null) {
            throw new FrameworkRuntimeException(ResultCode.NOTFINDWATER, "未找到水表", null);
        }

        if (DeviceCode.DEVICE_KIND_FAKE.equals(d.getKind())) {
            throw new FrameworkRuntimeException(ResultCode.NOTFINDWATER, "虚表不能抄表", null);
        }

        OwnerVo ownerVo = iOwnerService.getOwnerUserByDevId(d.getDevid());
        if (ownerVo == null) {
            throw new FrameworkRuntimeException(ResultCode.ParamIllegal, "业主不存在或者已销户", null);
        }

        if (ownerVo.getUpreadtime() != null && ownerVo.getUpreadwater() != null) {
            if (Double.parseDouble(meterDataForm.getMeter()) < Double.parseDouble(ownerVo.getUpreadwater())) {
                throw new FrameworkRuntimeException(ResultCode.ParamIllegal, "抄表读数不能少于最近一期账单读数", null);
            }
        }

        Date date = new Date();
        d.setWater(Double.valueOf(meterDataForm.getMeter()));
        if (!StringUtils.isBlank(meterDataForm.getUplinktime())) {
            d.setUplinktime(meterDataForm.getUplinktime());
        } else {
            d.setUplinktime(DateUtils.formatDatetime(date));
        }

        DeviceBo deviceBo = new DeviceBo();
        BeanUtils.copy(d, deviceBo);
        iDeviceService.update(deviceBo);

        DeviceUplinkBo up = new DeviceUplinkBo();
        up.setConfirmed(false);
        up.setUplinkData(meterDataForm.getRemark());
        up.setDeveui(d.getDeveui());
        up.setDevid(d.getDevid());

        if (!StringUtils.isBlank(meterDataForm.getUplinktime())) {
            up.setRxtime(DateUtils.parseDatetime(meterDataForm.getUplinktime()));
        } else {
            up.setRxtime(date);
        }
        up.setWater(meterDataForm.getMeter());
        up.setDate(DateUtils.format(date, DateUtils.YYYYMM));
        iDeviceUplinkService.add(up);
    }

    @Override
    public Integer singleBuildOrder(OwnerVo owner, AreaNodeVo community, UserVo user, DeviceVo device,
                                    PayTypeVo payType) throws FrameworkRuntimeException {

        // 获取离当前时间最近的一条上行记录
        String meterTime = DateUtils.formatDate(new Date());
        String[] data = meterTime.split("-");
        String year = data[0];
        String month = data[1];

        Calendar ca = Calendar.getInstance();
        ca.set(Integer.parseInt(data[0]), Integer.parseInt(data[1]) - 1, Integer.parseInt(data[2]));
        ca.add(Calendar.MONTH, -1);
        Date lastMonth = ca.getTime();
        String CurrentMonth = data[0] + data[1];
        String LastMonth = DateUtils.format(lastMonth, DateUtils.YYYYMM);
        meterTime = meterTime + " 23:59:59";

        OrderBo order = new OrderBo();
        ReduceVo reduce = null;
        PurposeVo purpose = iPurposeService.findById(owner.getPurposeid());
        order.setOwnerid(owner.getOwnerid());
        order.setTradestatus(WaterConstants.ORDER_TRADESTATUS_ABNORMAL);

        OrderVo oldOrder = iOrderService.findByUserNo(owner.getEnterpriseid(), owner.getUserno());
        LastUpLinkVo lastUplink = iOrderService.findLastUplink(owner.getDevid(), CurrentMonth, LastMonth, meterTime);
        List<LadderPriceDetailVo> payDetailList = new ArrayList<>();
        if (oldOrder == null) {
            order.setUpreadtime(null);
            order.setUpreadwater(null);
            // 计算用水量,第一次:截止时间的上行读数减初始读数
            if (lastUplink != null) {
                order.setReadtime(lastUplink.getRxtime());
                order.setReadwater(lastUplink.getWater());
                if (device.getBeginvalue() == null) {
                    order.setDescribe(ErrorType.NOINITWATER.text());
                    order.setErrortype(ErrorType.NOWATERDATA.intValue());

                    device.setBeginvalue(0.0);
                }
                order.setWater(CalUtil.sub(lastUplink.getWater(), device.getBeginvalue()));
                // 一个月都没数据上报,判断为异常
                if (lastUplink.getDays() <= 62) {
                    order.setTradestatus(WaterConstants.ORDER_TRADESTATUS_NORMAL);

                    // 这才算钱
                    List<LadderVo> ladders = payType.getLadders();
                    if (ladders == null) {
                        order.setDescribe(ErrorType.NOSETPAYTYPE.text());
                        order.setErrortype(ErrorType.NOSETPAYTYPE.intValue());
                        order.setTradestatus(WaterConstants.ORDER_TRADESTATUS_ABNORMAL);
                    } else {
                        ReduceBo reduceBo = new ReduceBo();
                        reduceBo.setReduceid(owner.getReduceid());
                        reduce = iReduceService.findById(reduceBo);
                        Double realAmount = BusinessUtil.getRealPayV2(BeanUtils.copy(ladders, LadderBo.class),
                                order.getWater());
                        order.setOriginal(new BigDecimal(realAmount));

                        payDetailList = BusinessUtil.getLadderPriceDetailVo(payType.getLadders(), order.getWater());
                        // 计算是否设置保底收费，如果最终应缴金额小于保底收费则将保底收费赋给最终应缴金额
                        realAmount = CalPreviewBillsFactoryImpl.getRealAmount(payType, realAmount, payDetailList);

                        if (reduce != null) {
                            // 单位类型 1为吨 2为元
                            if (reduce.getUnit() == 1) {
                                Double realwater = CalUtil.sub(order.getWater(), reduce.getRvalue());
                                // 减出优惠的吨数后重计水费
                                if (realwater > 0.0) {
                                    realAmount = BusinessUtil.getRealPayV2(BeanUtils.copy(ladders, LadderBo.class),
                                            realwater);
                                } else {
                                    realAmount = 0.0;
                                }
                            }
                            // 减出优惠钱数
                            if (reduce.getUnit() == 2) {
                                realAmount = CalUtil.sub(realAmount, reduce.getRvalue());
                                if (realAmount < 0.0) {
                                    realAmount = 0.0;
                                }
                            }
                        }
                        order.setAmount(realAmount);
                    }
                } else {
                    order.setDescribe(ErrorType.WATEREXCEPTION.text());
                    order.setErrortype(ErrorType.WATEREXCEPTION.intValue());
                }
                order.setDay(lastUplink.getDays());
            } else {
                order.setDescribe(ErrorType.NOWATERDATA.text());
                order.setErrortype(ErrorType.NOWATERDATA.intValue());
            }
        } else {
            order.setUpreadtime(oldOrder.getReadtime());
            order.setUpreadwater(oldOrder.getReadwater());
            // 计算用水量
            if (lastUplink != null) {
                order.setReadtime(lastUplink.getRxtime());
                order.setReadwater(lastUplink.getWater());
                if (oldOrder.getReadwater() == null) {
                    order.setDescribe("上期账单没有抄表读数");
                    oldOrder.setReadwater(0.0);
                }
                order.setWater(CalUtil.sub(lastUplink.getWater(), oldOrder.getReadwater()));

                if (lastUplink.getDays() <= 62) {
                    order.setTradestatus(WaterConstants.ORDER_TRADESTATUS_NORMAL);

                    // 这才算钱
                    List<LadderVo> ladders = payType.getLadders();
                    if (ladders == null) {
                        order.setDescribe(ErrorType.NOSETPAYTYPE.text());
                        order.setErrortype(ErrorType.NOSETPAYTYPE.intValue());
                        order.setTradestatus(WaterConstants.ORDER_TRADESTATUS_ABNORMAL);
                    } else {
                        ReduceBo reduceBo = new ReduceBo();
                        reduceBo.setReduceid(owner.getReduceid());
                        reduce = iReduceService.findById(reduceBo);
                        Double realAmount = BusinessUtil.getRealPayV2(BeanUtils.copy(ladders, LadderBo.class),
                                order.getWater());
                        order.setOriginal(new BigDecimal(realAmount));

                        payDetailList = BusinessUtil.getLadderPriceDetailVo(payType.getLadders(), order.getWater());
                        // 计算是否设置保底收费，如果最终应缴金额小于保底收费则将保底收费赋给最终应缴金额
                        realAmount = CalPreviewBillsFactoryImpl.getRealAmount(payType, realAmount, payDetailList);

                        if (reduce != null) {
                            // 单位类型 1为吨 2为元
                            if (reduce.getUnit() == 1) {
                                Double realwater = CalUtil.sub(order.getWater(), reduce.getRvalue());
                                // 减出优惠的吨数后重计水费
                                if (realwater > 0.0) {
                                    realAmount = BusinessUtil.getRealPayV2(BeanUtils.copy(ladders, LadderBo.class),
                                            realwater);
                                } else {
                                    realAmount = 0.0;
                                }
                            }
                            // 减出优惠钱数
                            if (reduce.getUnit() == 2) {
                                realAmount = CalUtil.sub(realAmount, reduce.getRvalue());
                                if (realAmount < 0.0) {
                                    realAmount = 0.0;
                                }
                            }
                        }
                        order.setAmount(realAmount);
                    }
                } else {
                    order.setDescribe(ErrorType.WATEREXCEPTION.text());
                    order.setErrortype(ErrorType.WATEREXCEPTION.intValue());
                }
                order.setDay(lastUplink.getDays());
            }
        }
        order.setEnterpriseid(owner.getEnterpriseid());
        order.setTradeno(String.valueOf(Config.Generator.nextId()));
        order.setYear(year);
        order.setMonth(month);

        order.setCommunityid(owner.getCommunityid());
        order.setCommunityname(community.getTitle());
        order.setCommunityno("");
        order.setPricetypeid(owner.getPaytypeid());
        order.setPricetypename(payType.getName());
        order.setReduceid(owner.getReduceid());
        order.setPurposeid(owner.getPurposeid());

        order.setUsername(owner.getUsername());
        order.setUserno(owner.getUserno());
        order.setPhone(owner.getUserphone());
        order.setAddr(owner.getUseraddr());
        order.setCardid(owner.getCardid());

        order.setDeveui(device.getDeveui());
        order.setDevno(device.getDevno());
        order.setDevstatus(device.getStatus());
        order.setTapstatus(device.getTapstatus());
        order.setTaptype(device.getTaptype());
        order.setPaystatus(WaterConstants.ORDER_PAYSTATUS_NOTPAID);
        order.setExplain(device.getExplain());

        order.setGenerateuserid(user.getUserid());
        order.setGenerateusername(user.getAccount());
        order.setGeneratetime(DateUtils.formatDatetime(new Date()));

        if (order.getAmount() < 0) {
            order.setDescribe(ErrorType.WATEREXCEPTION.text());
            order.setErrortype(ErrorType.WATEREXCEPTION.intValue());
            order.setTradestatus(WaterConstants.ORDER_TRADESTATUS_ABNORMAL);
        }

        if (order.getErrortype() == null) {
            order.setErrortype(ErrorType.SUCCESS.intValue());
        }

        if (order.getErrortype() == ErrorType.SUCCESS.intValue()) {
            int i = iOrderService.addOrder(order, payType, reduce, purpose, payDetailList);

            if (i > 0 && order.getAmount() > 0) {
                //插入营业厅
                PaymentTradeOrderForm form = new PaymentTradeOrderForm();
                form.setBusinessId(PaymentConstants.BUSSINESS_TYPE_WATERFEE);
                form.setTradeNumber(order.getTradeno());
                form.setUserName(order.getUsername());
                form.setUserno(order.getUserno());
                form.setUserPhone(order.getPhone());
                form.setUserAddr(order.getAddr());
                form.setUserid(order.getOwnerid());
                form.setCreateDate(new Date());
                form.setAmount(String.valueOf(CalUtil.div(order.getAmount(),1.0,2)));
                form.setTradeName(order.getYear() + order.getMonth() + " - 水费账单");
                form.setEnterpriseid(order.getEnterpriseid());
                form.setStatus(PaymentConstants.PATMENT_STATUS_NOPAY);
                iPaymentFactory.add(form);

                if (VerificationUtils.regex(owner.getUserphone(), VerificationUtils.REG_PHONE)) {
                    // TODO 发短信和微信消息
                }

                //处理欠费自动关阀
                SettlementVo vo = iSettlementService.getSettlement(user.getEnterpriseid());
                if(vo != null){
                    iDeleteOrderExtTaskFactory.sendArrearsCloseNotice(owner.getOwnerid(), vo);
                }
            }
        }

        return order.getErrortype();
    }

    @Override
    public void readMeterAndBuildOrder(MeterDataForm meterDataForm) throws FrameworkRuntimeException {
        UserVo user = AuthCasClient.getUser();
        Map<String, AreaNodeVo> map = BaseInf.getAreaMaps(user.getUserid(), user.getTicket());
        if (map == null || map.size() == 0) {
            throw new FrameworkRuntimeException(ResultCode.ParamIllegal, "操作人没有分配区域,没有操作权限", null);
        }

        DeviceVo d = iDeviceService.findByDevEUI(meterDataForm.getDeveui());
        if (d == null) {
            throw new FrameworkRuntimeException(ResultCode.NOTFINDWATER, "未找到水表", null);
        }
        if (!DeviceCode.DEVICE_PARENT.equals(d.getPid())) {
            throw new FrameworkRuntimeException(ResultCode.DevnoNotExist, "该水表不是总表,不能给业主生成账单");
        }

        if (DeviceCode.DEVICE_KIND_FAKE.equals(d.getKind())) {
            throw new FrameworkRuntimeException(ResultCode.NOTFINDWATER, "虚表不能抄表", null);
        }

        OwnerVo owner = iOwnerService.getOwnerUserByDevId(d.getDevid());
        if (owner == null) {
            throw new FrameworkRuntimeException(ResultCode.ParamIllegal, "业主不存在或者已销户", null);
        }
        AreaNodeVo community = map.get(String.valueOf(owner.getCommunityid()));
        if (community == null) {
            throw new FrameworkRuntimeException(ResultCode.ParamIllegal, "操作人没有操作该区域的权限", null);
        }

        if (d.getBeginvalue() != null && d.getBeginvalue() > Double.parseDouble(meterDataForm.getMeter())) {
            throw new FrameworkRuntimeException(ResultCode.ParamIllegal, "抄表读数不能少于水表初始读数", null);
        }

        if (owner.getUpreadtime() != null && owner.getUpreadwater() != null) {
            if (Double.parseDouble(meterDataForm.getMeter()) < Double.parseDouble(owner.getUpreadwater())) {
                throw new FrameworkRuntimeException(ResultCode.ParamIllegal, "抄表读数不能少于最近一期账单读数", null);
            }
        }

        Date date = new Date();
        d.setWater(Double.valueOf(meterDataForm.getMeter()));
        if (!StringUtils.isBlank(meterDataForm.getUplinktime())) {
            d.setUplinktime(meterDataForm.getUplinktime());
        } else {
            d.setUplinktime(DateUtils.formatDatetime(date));
        }

        DeviceBo deviceBo = new DeviceBo();
        BeanUtils.copy(d, deviceBo);
        iDeviceService.update(deviceBo);

        DeviceUplinkBo up = new DeviceUplinkBo();
        up.setConfirmed(false);
        up.setUplinkData(meterDataForm.getRemark());
        up.setDeveui(d.getDeveui());
        up.setDevid(d.getDevid());

        if (!StringUtils.isBlank(meterDataForm.getUplinktime())) {
            up.setRxtime(DateUtils.parseDatetime(meterDataForm.getUplinktime()));
        } else {
            up.setRxtime(date);
        }
        up.setWater(meterDataForm.getMeter());
        up.setDate(DateUtils.format(date, DateUtils.YYYYMM));
        iDeviceUplinkService.add(up);

        PayTypeVo payType = iPayTypeService.get(owner.getPaytypeid());
        Integer errorType = singleBuildOrder(owner, community, user, d, payType);

        if (errorType != ErrorType.SUCCESS.intValue()) {
            throw new FrameworkRuntimeException(ResultCode.Fail, "error", ErrorType.getCodeText(errorType));
        }
    }

    @Override
    public Pagination<OrderPreviewVo> AuditingOrderPreviewList(PreviewForm previewForm)
            throws FrameworkRuntimeException {
        UserVo user = AuthCasClient.getUser();
        previewForm.setEnterpriseid(user.getEnterpriseid());
        return iOrderService.auditingOrderPreviewList(previewForm);
    }

    @Override
    public void makeOrderByAuditing(PreviewForm previewForm) throws FrameworkRuntimeException {
        UserVo user = AuthCasClient.getUser();
        previewForm.setEnterpriseid(user.getEnterpriseid());

        //只把正常的导入
        previewForm.setTradeStatus(String.valueOf(WaterConstants.ORDER_TRADESTATUS_NORMAL));

        List<OrderPreviewVo> orders = iOrderService.findOrderPreview(previewForm);

        if (iOrderService.addOrdersByAuditing(previewForm) > 0) {

            /**
             * 数据进入营业厅
             * */
            List<PaymentTradeOrderForm> forms = new ArrayList<>();
            Date date = new Date();
            for (OrderPreviewVo previewVo : orders) {
                if (previewVo.getAmount() != null && previewVo.getAmount() <= 0) {
                    continue;
                }
                PaymentTradeOrderForm form = new PaymentTradeOrderForm();
                form.setBusinessId(PaymentConstants.BUSSINESS_TYPE_WATERFEE);
                form.setTradeNumber(previewVo.getTradeno());
                form.setUserName(previewVo.getUsername());
                form.setUserno(previewVo.getUserno());
                form.setUserAddr(previewVo.getAddr());
                form.setUserPhone(previewVo.getPhone());
                form.setUserid(previewVo.getOwnerid());
                form.setCreateDate(date);
                form.setAmount(String.valueOf(previewVo.getAmount()));
                form.setTradeName(previewVo.getYear() + previewVo.getMonth() + " - 水费账单");
                form.setEnterpriseid(previewVo.getEnterpriseid());
                form.setStatus(PaymentConstants.PATMENT_STATUS_NOPAY);
                forms.add(form);
            }
            iPaymentFactory.batchAdd(forms);

            iOrderService.clearPreviewByAuditing(previewForm, false);

            // 异步任务
            // 发生成账单的消息推送
            // iMakeOrderFactory.batchSendmakeOrderExecutor(orders);
            // 勾选自动扣费处理逻辑,一并处理<账单金额等于0则直接将账单设置为已缴费>
            iMakeOrderFactory.asyncAutoPay(user);
        } else {
            throw new FrameworkRuntimeException(ResultCode.Fail, "没有新的账单", null);
        }
    }
}
