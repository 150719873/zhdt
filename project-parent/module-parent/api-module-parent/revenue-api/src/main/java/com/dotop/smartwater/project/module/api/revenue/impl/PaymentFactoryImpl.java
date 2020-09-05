package com.dotop.smartwater.project.module.api.revenue.impl;

import com.dotop.smartwater.dependence.cache.StringListCache;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.dependence.core.utils.*;
import com.dotop.smartwater.project.module.api.revenue.IPaymentFactory;
import com.dotop.smartwater.project.module.api.revenue.IPushFactory;
import com.dotop.smartwater.project.module.api.revenue.IPushMsgFactory;
import com.dotop.smartwater.project.module.service.revenue.*;
import com.dotop.smartwater.project.module.client.third.fegin.pay.IPayFeginClient;
import com.dotop.smartwater.project.module.core.auth.enums.SmsEnum;
import com.dotop.smartwater.project.module.core.auth.vo.SettlementVo;
import com.dotop.smartwater.project.module.core.pay.constants.PayConstants;
import com.dotop.smartwater.project.module.core.pay.vo.PayResultVo;
import com.dotop.smartwater.project.module.core.pay.vo.PushVo;
import com.dotop.smartwater.project.module.core.third.bo.pay.PaymentOrderBo;
import com.dotop.smartwater.project.module.core.water.bo.*;
import com.dotop.smartwater.project.module.core.water.config.Config;
import com.dotop.smartwater.project.module.core.water.constants.PaymentConstants;
import com.dotop.smartwater.project.module.core.water.constants.ResultCode;
import com.dotop.smartwater.project.module.core.water.constants.WaterConstants;
import com.dotop.smartwater.project.module.core.water.dto.PaymentTradeOrderDto;
import com.dotop.smartwater.project.module.core.water.dto.PaymentTradeOrderExtDto;
import com.dotop.smartwater.project.module.core.water.form.PaymentTradeOrderExtForm;
import com.dotop.smartwater.project.module.core.water.form.PaymentTradeOrderForm;
import com.dotop.smartwater.project.module.core.water.model.PayCallBack;
import com.dotop.smartwater.project.module.core.water.utils.CalUtil;
import com.dotop.smartwater.project.module.core.water.vo.*;
import com.dotop.smartwater.project.module.core.water.vo.customize.LadderPriceDetailVo;
import com.dotop.smartwater.project.module.service.revenue.*;
import com.dotop.smartwater.project.module.service.tool.ISettlementService;
import com.dotop.smartwater.project.module.service.tool.ISmsTemplateService;
import com.dotop.smartwater.project.module.service.tool.ISmsToolService;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;

/**
 * 票据管理
 *

 * @date 2019年2月25日
 */
@Component
public class PaymentFactoryImpl implements IPaymentFactory {

    private static final Logger LOGGER = LogManager.getLogger(PaymentFactoryImpl.class);

    private static final String QueueName = "rechargeSendSms";

    @Autowired
    private ISettlementService iSettlementService;

    @Autowired
    private ISmsTemplateService iSmsTemplateService;

    @Autowired
    private ISmsToolService iSmsToolService;

    @Autowired
    private IPushMsgFactory iPushMsgFactory;

    @Autowired
    private IOrderService iOrderService;

    @Autowired
    private ICouponService iCouponService;

    @Autowired
    private IPaymentTradeOrderService service;

    @Autowired
    private IPaymentTradeOrderExtService extService;

    @Autowired
    private IPaymentTradeResultService resultService;

    @Autowired
    private IPaymentTradeProService proService;

    @Autowired
    private IPayFeginClient iPayFeginClient;

    @Autowired
    private IOwnerService iOwnerService;

    @Autowired
    private IPushFactory iPushFactory;

    @Autowired
    private IPayDetailService iPayDetailService;

    @Autowired
    private ITradeOrderService iTradeOrderService;

    @Autowired
    private StringListCache stringListCache;

    @Override
    public PaymentTradeOrderVo add(PaymentTradeOrderForm paymentTradeOrderForm) {
        PaymentTradeOrderVo vo = service.add(BeanUtils.copy(paymentTradeOrderForm, PaymentTradeOrderBo.class));
        PaymentTradeOrderExtBo bo = new PaymentTradeOrderExtBo();
        bo.setTradeid(vo.getTradeid());
        bo.setCost(vo.getAmount());
        bo.setCouponMoney("0");
        bo.setBalance("0");
        bo.setPenalty("0");
        bo.setDetail(paymentTradeOrderForm.getDetail());
        extService.add(bo);
        return vo;
    }

    @Override
    public String del(PaymentTradeOrderForm paymentTradeOrderForm) {
        PaymentTradeOrderVo vo = service.get(BeanUtils.copy(paymentTradeOrderForm, PaymentTradeOrderBo.class));
        //报装
        if (vo == null) {
            return null;
        }

        if (PaymentConstants.PATMENT_STATUS_PAYED == vo.getStatus()) {
            throw new FrameworkRuntimeException(ResultCode.Fail, "账单已经支付,不能删除");
        }

        PaymentTradeOrderBo bo = new PaymentTradeOrderBo();
        bo.setTradeid(vo.getTradeid());
        service.del(bo);

        if (PaymentConstants.BUSSINESS_TYPE_REPAIR.equals(vo.getBusinessId())) {
            //报装也要删除
            TradeOrderBo tradeOrderBo = new TradeOrderBo();
            tradeOrderBo.setEnterpriseid(vo.getEnterpriseid());
            tradeOrderBo.setNumber(vo.getTradeNumber());

            TradeOrderVo tradeOrderVo = iTradeOrderService.get(tradeOrderBo);
            if (tradeOrderVo != null) {
                iTradeOrderService.del(tradeOrderBo);
            }
        }

        if (PaymentConstants.BUSSINESS_TYPE_WATERFEE.equals(vo.getBusinessId())) {
            OrderVo orderVo = iOrderService.findOrderByTradeNo(vo.getTradeNumber());
            if (orderVo != null) {
                OrderBo orderBo = new OrderBo();
                orderBo.setId(orderVo.getId());
                orderBo.setTradeno(orderVo.getTradeno());
                iOrderService.deleteById(orderBo);
            }
        }
        return null;
    }

    @Override
    public void batchAdd(List<PaymentTradeOrderForm> list) {
        if (list.size() == 0) {
            LOGGER.info("batchAdd 传入了空list");
            return;
        }

        List<PaymentTradeOrderDto> dtoList = new ArrayList<>();
        List<PaymentTradeOrderExtDto> extDtoList = new ArrayList<>();
        for (PaymentTradeOrderForm form : list) {
            String tradeid = UuidUtils.getUuid(true);
            PaymentTradeOrderDto dto = BeanUtils.copy(form, PaymentTradeOrderDto.class);
            dto.setTradeid(tradeid);
            dto.setMerge(tradeid);

            PaymentTradeOrderExtDto extDto = new PaymentTradeOrderExtDto();
            extDto.setTradeid(tradeid);
            extDto.setCost(dto.getAmount());
            extDto.setCouponMoney("0");
            extDto.setBalance("0");
            extDto.setPenalty("0");
            extDto.setDetail(form.getDetail());
            extDto.setId(UuidUtils.getUuid(true));
            dtoList.add(dto);
            extDtoList.add(extDto);
        }

        service.batchAdd(dtoList, extDtoList);
    }

    @Override
    public Pagination<PaymentTradeOrderVo> page(PaymentTradeOrderForm paymentTradeOrderForm) {
        return service.page(BeanUtils.copy(paymentTradeOrderForm, PaymentTradeOrderBo.class));
    }

    @Override
    public Map<String, String> pay(PaymentTradeOrderExtForm ext) {

        List<PaymentTradeOrderExtVo> list = extService.findNotPayListByTradeIds(ext.getTradeIds(), ext.getEnterpriseid());
        int count = list.size();
        if (count == 0) {
            throw new FrameworkRuntimeException(ResultCode.Fail, "请至少选择一张账单");
        }
        if (list.size() != ext.getTradeIds().size()) {
            throw new FrameworkRuntimeException(ResultCode.Fail, "选择的状态中有其他状态的账单");
        }

        Double allMustPay = 0.0;
        Double cal = 0.0;

        List<PaymentTradeOrderDto> paymentTradeOrderDtoList = new ArrayList<>();

        for (PaymentTradeOrderExtVo vo : list) {
            allMustPay = CalUtil.add(CalUtil.add(vo.getCost(), vo.getPenalty()).doubleValue(), allMustPay);
        }

        //实收
        if (StringUtils.isNotBlank(ext.getActualamount())) {
            cal = CalUtil.add(String.valueOf(cal), ext.getActualamount()).doubleValue();
        }

        //抵扣金额
        Double balance = 0.0;
        //用户余额
        Double ownerBalance = null;
        OwnerVo owner = null;
        if (StringUtils.isNotBlank(ext.getOwnerid()) && StringUtils.isNotBlank(ext.getBalance())
                && Double.parseDouble(ext.getBalance()) > 0) {
            OwnerBo ownerBo = new OwnerBo();
            ownerBo.setOwnerid(ext.getOwnerid());
            owner = iOwnerService.findByOwnerId(ownerBo);
            // 检验余额抵扣
            if (owner.getAlreadypay() == null || owner.getAlreadypay() < 0) {
                throw new FrameworkRuntimeException(ResultCode.ParamIllegal, "业主余额不足", null);
            }

            ownerBalance = CalUtil.sub(owner.getAlreadypay(), Double.parseDouble(ext.getBalance()));
            if (ownerBalance < 0.0) {
                throw new FrameworkRuntimeException(ResultCode.ParamIllegal, "抵扣值不能大于业主余额", null);
            }

            cal = CalUtil.add(String.valueOf(cal), ext.getBalance()).doubleValue();
            balance = Double.parseDouble(ext.getBalance());
        }

        //代金券金额
        CouponVo coupon = null;
        Double couponMoney = 0.0;
        if (StringUtils.isNotBlank(ext.getCouponid())) {
            CouponBo couponBo = new CouponBo();
            couponBo.setCouponid(ext.getCouponid());
            coupon = iCouponService.getCoupon(couponBo);
            if (coupon != null) {
                if (allMustPay < coupon.getFacevalue()) {
                    coupon.setFacevalue(allMustPay);
                }
                cal = CalUtil.add(cal, coupon.getFacevalue());
                couponMoney = coupon.getFacevalue();
            } else {
                throw new FrameworkRuntimeException(ResultCode.ParamIllegal, "无效的couponid", null);
            }
        }

        //找零
        Double change = 0.0;
        if (StringUtils.isNotBlank(ext.getChange())) {
            cal = CalUtil.sub(String.valueOf(cal), ext.getChange()).doubleValue();
            change = Double.parseDouble(ext.getChange());
        }

        if (!cal.equals(allMustPay)) {
            paymentTradeOrderDtoList.clear();
            throw new FrameworkRuntimeException(ResultCode.Fail, "账单金额不匹配");
        }

        ext.setCouponMoney(String.valueOf(couponMoney));

        /**
         * 数据处理(填平才行)
         * */
        //平均方案
        Double moneyLast = 0.0;
        Double moneyAvg = 0.0;
        if (couponMoney > 0) {
            moneyAvg = CalUtil.div(couponMoney, count);
            moneyLast = CalUtil.sub(couponMoney, CalUtil.mul(moneyAvg, (count - 1)));
        }

        Double balanceLast = 0.0;
        Double balanceAvg = 0.0;
        if (balance > 0) {
            balanceAvg = CalUtil.div(balance, count);
            balanceLast = CalUtil.sub(balance, CalUtil.mul(balanceAvg, (count - 1)));
        }

        Double changeLast = 0.0;
        Double changeAvg = 0.0;
        if (change > 0) {
            changeAvg = CalUtil.div(change, count);
            changeLast = CalUtil.sub(change, CalUtil.mul(changeAvg, (count - 1)));
        }

        int i = 0;
        for (PaymentTradeOrderExtVo vo : list) {
            if (i == count - 1) {
                vo.setCouponid(ext.getCouponid());
                vo.setMode(ext.getMode());
                vo.setBalance(String.valueOf(balanceLast));
                vo.setChange(String.valueOf(changeLast));
                vo.setCouponMoney(String.valueOf(moneyLast));
                //优惠券金额 + 余额抵扣金额 + 实收 - 找零 = 应收 + 滞纳金
                Double all = CalUtil.add(CalUtil.add(vo.getCost(), vo.getPenalty()).doubleValue(), changeLast);
                vo.setActualamount(String.valueOf(CalUtil.sub(all, CalUtil.add(balanceLast, moneyLast))));
                break;
            }

            vo.setCouponid(ext.getCouponid());
            vo.setMode(ext.getMode());
            vo.setBalance(String.valueOf(balanceAvg));
            vo.setChange(String.valueOf(changeAvg));
            vo.setCouponMoney(String.valueOf(moneyAvg));
            //优惠券金额 + 余额抵扣金额 + 实收 - 找零 = 应收 + 滞纳金
            Double all = CalUtil.add(CalUtil.add(vo.getCost(), vo.getPenalty()).doubleValue(), changeAvg);
            vo.setActualamount(String.valueOf(CalUtil.sub(all, CalUtil.add(balanceAvg, moneyAvg))));
            i++;
        }

        /**
         * merge 当只有一张单时，merge 等于 tradeid
         * 多单合并缴费时，重新设一个id用于区别是否合并缴费和对账
         * */
        String merge = count > 1 ? UuidUtils.getUuid(true) : list.get(0).getTradeid();
        for (PaymentTradeOrderExtVo vo : list) {
            PaymentTradeOrderDto paymentTradeOrderDto = new PaymentTradeOrderDto();
            paymentTradeOrderDto.setStatus(PaymentConstants.PATMENT_STATUS_PAYING);
            paymentTradeOrderDto.setTradeid(vo.getTradeid());
            paymentTradeOrderDto.setMerge(merge);
            paymentTradeOrderDtoList.add(paymentTradeOrderDto);
        }

        /**
         * 更新订单信息为锁定中
         * */
        extService.updateList(list);
        service.updateList(paymentTradeOrderDtoList);

        /**
         * 下单开始
         * */
        Map<String, String> resultMap = new HashMap<>(20);
        Date date = new Date();

        //交易号(查支付平台的)
        String tradeNum = String.valueOf(Config.Generator.nextId());

        /**
         * 插入结果表和过程表
         * */
        List<String> proIds = new ArrayList<>();
        List<String> resIds = new ArrayList<>();
        String extraId = UuidUtils.getUuid(true);
        for (PaymentTradeOrderExtVo vo : list) {
            PaymentTradeProBo paymentTradeProBo = new PaymentTradeProBo();
            paymentTradeProBo.setTradeid(vo.getTradeid());
            paymentTradeProBo.setTransactionNumber(tradeNum);
            paymentTradeProBo.setAmount(ext.getActualamount());
            paymentTradeProBo.setMode(ext.getMode());
            paymentTradeProBo.setStatus(PaymentConstants.STATUS_PAYING);
            paymentTradeProBo.setEnterpriseid(ext.getEnterpriseid());
            paymentTradeProBo.setPayCard(ext.getPayCard());
            paymentTradeProBo.setOpenid(ext.getOpenid());

            paymentTradeProBo.setThirdPartyMsg(extraId);
            PaymentTradeProVo paymentTradeProVo = proService.add(paymentTradeProBo);
            proIds.add(paymentTradeProVo.getTransactionProId());

            PaymentTradeResultBo paymentTradeResultBo = BeanUtils.copy(paymentTradeProBo, PaymentTradeResultBo.class);
            PaymentTradeResultVo paymentTradeResultVo = resultService.add(paymentTradeResultBo);
            resIds.add(paymentTradeResultVo.getTransactionid());
        }

        /**
         * 先把代金券和余额抵扣扣掉，失败则回滚
         * */
        service.handleBalanceAndCoupon(merge, ext.getOwnerid(), ownerBalance, coupon);

        switch (ext.getMode()) {
            //现金，同步操作，一定成功
            case PayConstants.PAYTYPE_MONEY:
                for (String proId : proIds) {
                    PaymentTradeProBo paymentTradeProBo = new PaymentTradeProBo();
                    paymentTradeProBo.setTransactionProId(proId);
                    paymentTradeProBo.setPayTime(date);
                    paymentTradeProBo.setDescription("现金支付成功");
                    paymentTradeProBo.setStatus(PaymentConstants.STATUS_PAYED);
                    proService.edit(paymentTradeProBo);
                }
                for (String resId : resIds) {
                    PaymentTradeResultBo paymentTradeResultBo = new PaymentTradeResultBo();
                    paymentTradeResultBo.setTransactionid(resId);
                    paymentTradeResultBo.setPayTime(date);
                    paymentTradeResultBo.setDescription("现金支付成功");
                    paymentTradeResultBo.setStatus(PaymentConstants.STATUS_PAYED);
                    resultService.edit(paymentTradeResultBo);
                }
                for (PaymentTradeOrderExtVo vo : list) {
                    PaymentTradeOrderBo paymentTradeOrderBo = new PaymentTradeOrderBo();
                    paymentTradeOrderBo.setStatus(PaymentConstants.PATMENT_STATUS_PAYED);
                    paymentTradeOrderBo.setPayTime(date);
                    paymentTradeOrderBo.setTradeid(vo.getTradeid());
                    service.edit(paymentTradeOrderBo);
                }

                // 余额抵扣大于0,写进记录表
                if (balance > 0) {
                    PayDetailBo payDetail = new PayDetailBo();
                    payDetail.setId(UuidUtils.getUuid());
                    payDetail.setOwnerid(owner.getOwnerid());
                    payDetail.setOwnerno(owner.getUserno());
                    payDetail.setOwnername(owner.getUsername());
                    payDetail.setPaymoney(new BigDecimal(ext.getBalance()));
                    payDetail.setCreatetime(new Date());

                    payDetail.setType(WaterConstants.PAY_DETAIL_TYPE_OUT);
                    payDetail.setBeforemoney(new BigDecimal(owner.getAlreadypay()));
                    payDetail.setAftermoney(new BigDecimal(ownerBalance));
                    payDetail.setPayno(tradeNum);
                    payDetail.setTradeno("");
                    payDetail.setRemark("余额抵扣水费");
                    iPayDetailService.addPayDetail(payDetail);
                }

                iPushFactory.push(ext);
                resultMap.put("status", PaymentConstants.STATUS_PAYED);
                return resultMap;
            //微信（异步操作）
            case PayConstants.PAYTYPE_PAYCARD:
            case PayConstants.PAYTYPE_WEIXIN:
            case PayConstants.PAYTYPE_QRCODE:
                PaymentOrderBo paymentOrderBo = new PaymentOrderBo();

                paymentOrderBo.setPayCard(ext.getPayCard());
                paymentOrderBo.setOpenid(ext.getOpenid());

                paymentOrderBo.setEnterpriseid(ext.getEnterpriseid());
                paymentOrderBo.setTradeName("JY-" + Config.Generator.nextId());

                //测试统一支付0.01
                if (WaterConstants.DX_ENTERPRISE_ID.equals(ext.getEnterpriseid())) {
                    paymentOrderBo.setAmount("0.01");
                } else {
                    paymentOrderBo.setAmount(ext.getActualamount());
                }

                paymentOrderBo.setMode(ext.getMode());
                paymentOrderBo.setCallbackUrl(Config.PayCallBackUrl);
                paymentOrderBo.setTradeNumber(tradeNum);

                PayCallBack payCallBack = new PayCallBack();
                payCallBack.setProIds(proIds);
                payCallBack.setResIds(resIds);
                payCallBack.setExt(ext);

                PaymentTradeExtraBo paymentTradeExtraBo = new PaymentTradeExtraBo();

                paymentTradeExtraBo.setExtraid(extraId);
                paymentTradeExtraBo.setDetail(JSONUtils.toJSONString(payCallBack));
                service.addExtra(paymentTradeExtraBo);

                paymentOrderBo.setExtra(extraId);

                String data = iPayFeginClient.submit(paymentOrderBo);
                Map<Object, Object> map = JSONUtils.parseObject(data, Object.class, Object.class);

                String code = map.get("code").toString();
                String msg = map.get("msg").toString();
                String obj = map.get("data") == null ? null : map.get("data").toString();
                if (ResultCode.Success.equals(code)) {
                    PayResultVo payResultVo = JSONUtils.parseObject(obj, PayResultVo.class);
                    //下单成功
                    if (payResultVo != null && PayConstants.WECHAT_ORDER_STATUS_SUCCESS.equals(payResultVo.getReturn_code())) {
                        if (payResultVo.getReturn_data() != null && payResultVo.getReturn_data().size() > 0) {
                            resultMap.putAll(payResultVo.getReturn_data());
                        }
                        resultMap.put("status", PaymentConstants.STATUS_PAYING);
                        //如果是微信支付,返回一个账单单号给前端，只是为了方便前端，没别的
                        if (PayConstants.PAYTYPE_WEIXIN.equals(ext.getMode())) {
                            PaymentTradeOrderBo paramBo = new PaymentTradeOrderBo();
                            paramBo.setTradeid(ext.getTradeIds().get(0));
                            String tradeNumber = service.get(paramBo).getTradeNumber();
                            resultMap.put("tradeNumber", tradeNumber);
                        }

                        return resultMap;
                    } else {
                        msg = payResultVo.getReturn_msg();
                    }
                }

                //错误处理
                LOGGER.error("请求支付平台失败：" + msg);
                service.handleError(payCallBack, msg);
                resultMap.put("status", PaymentConstants.STATUS_ERROR);
                resultMap.put("err_msg", msg);
                return resultMap;
            default:
                throw new FrameworkRuntimeException(ResultCode.Fail, "未知的下单方式");
        }
    }

    @Override
    public void handleCallBack(PushVo pushVo) {
        String extraId = pushVo.getExtra();
        if (StringUtils.isBlank(extraId)) {
            LOGGER.info("没有extraid");
            return;
        }

        PaymentTradeExtraVo vo = service.getExtra(extraId);
        if (vo == null) {
            LOGGER.info("没有找到extra信息");
            return;
        }

        PayCallBack payCallBack = JSONUtils.parseObject(vo.getDetail(), PayCallBack.class);
        if (payCallBack == null) {
            LOGGER.info("没有payCallBack信息");
            return;
        }

        if (PayConstants.TRADE_PAYSTATUS_SUCCESS.equals(pushVo.getStatus())) {
            Date payTime = DateUtils.parseDatetime(pushVo.getPayTime());
            if (payCallBack.getProIds() != null && payCallBack.getProIds().size() > 0) {
                for (String proId : payCallBack.getProIds()) {
                    PaymentTradeProBo paymentTradeProBo = new PaymentTradeProBo();
                    paymentTradeProBo.setTransactionProId(proId);
                    paymentTradeProBo.setPayTime(payTime);
                    paymentTradeProBo.setDescription(pushVo.getDescription());
                    paymentTradeProBo.setStatus(PaymentConstants.STATUS_PAYED);
                    proService.edit(paymentTradeProBo);
                }
            }

            if (payCallBack.getResIds() != null && payCallBack.getResIds().size() > 0) {
                for (String resId : payCallBack.getResIds()) {
                    PaymentTradeResultBo paymentTradeResultBo = new PaymentTradeResultBo();
                    paymentTradeResultBo.setTransactionid(resId);
                    paymentTradeResultBo.setPayTime(payTime);
                    paymentTradeResultBo.setDescription(pushVo.getDescription());
                    paymentTradeResultBo.setStatus(PaymentConstants.STATUS_PAYED);
                    resultService.edit(paymentTradeResultBo);
                }
            }
            for (String tradeId : payCallBack.getExt().getTradeIds()) {
                PaymentTradeOrderBo paymentTradeOrderBo = new PaymentTradeOrderBo();
                paymentTradeOrderBo.setStatus(PaymentConstants.PATMENT_STATUS_PAYED);
                paymentTradeOrderBo.setPayTime(payTime);
                paymentTradeOrderBo.setTradeid(tradeId);
                service.edit(paymentTradeOrderBo);
            }
            LOGGER.info("订单 [{}] 支付成功", pushVo.getTradeNumber());


            //增加异步任务推送到子系统
            iPushFactory.push(payCallBack.getExt());
            return;
        }

        //以下为失败
        service.handleError(payCallBack, pushVo.getDescription());
        LOGGER.info("订单 [{}] 支付失败", pushVo.getTradeNumber());
    }

    @Override
    public Map<String, String> cancel(PaymentTradeOrderForm paymentTradeOrderForm) {
        PaymentTradeOrderBo paymentTradeOrderBo = new PaymentTradeOrderBo();
        paymentTradeOrderBo.setEnterpriseid(paymentTradeOrderForm.getEnterpriseid());
        paymentTradeOrderBo.setTradeNumber(paymentTradeOrderForm.getTradeNumber());
        PaymentTradeOrderVo paymentTradeOrderVo = service.findByEidAndTradeNum(paymentTradeOrderBo);
        if (paymentTradeOrderVo == null) {
            throw new FrameworkRuntimeException(ResultCode.Fail, "不存在的账单");
        }

        if (!PaymentConstants.PATMENT_STATUS_PAYING.equals(paymentTradeOrderVo.getStatus())) {
            throw new FrameworkRuntimeException(ResultCode.Fail, "不是支付中的状态不支持撤销");
        }

        PaymentTradeOrderExtVo extOrderVo = extService.findByTradeid(paymentTradeOrderVo.getTradeid());
        if (extOrderVo == null) {
            throw new FrameworkRuntimeException(ResultCode.Fail, "错误的账单");
        }

        Map<String, String> result = new HashMap<>(5);
        switch (extOrderVo.getMode()) {
            case PayConstants.PAYTYPE_PAYCARD:
            case PayConstants.PAYTYPE_WEIXIN:
            case PayConstants.PAYTYPE_QRCODE:
                PaymentTradeResultBo paymentTradeResultBo = new PaymentTradeResultBo();
                paymentTradeResultBo.setTradeid(paymentTradeOrderVo.getTradeid());
                paymentTradeResultBo.setStatus(PaymentConstants.STATUS_PAYING);
                paymentTradeResultBo.setMode(extOrderVo.getMode());
                paymentTradeResultBo.setEnterpriseid(paymentTradeOrderForm.getEnterpriseid());
                List<PaymentTradeResultVo> list = resultService.list(paymentTradeResultBo);
                if (list.size() != 1) {
                    throw new FrameworkRuntimeException(ResultCode.Fail, "账单出错,不能撤销");
                }

                PaymentTradeResultVo paymentTradeResultVo = list.get(0);

                PaymentOrderBo paymentOrderBo = new PaymentOrderBo();
                paymentOrderBo.setEnterpriseid(paymentTradeResultVo.getEnterpriseid());
                paymentOrderBo.setTradeNumber(paymentTradeResultVo.getTransactionNumber());
                String data = iPayFeginClient.ordercancel(paymentOrderBo);
                Map<Object, Object> map = JSONUtils.parseObject(data, Object.class, Object.class);

                String code = map.get("code").toString();
                String msg = map.get("msg").toString();
                String obj = map.get("data").toString();

                if (ResultCode.Success.equals(code)) {
                    PayResultVo payResultVo = JSONUtils.parseObject(obj, PayResultVo.class);
                    //下单成功
                    if (payResultVo != null && PayConstants.WECHAT_ORDER_CANCEL_SUCCESS.equals(payResultVo.getReturn_code())) {
                        PaymentTradeExtraVo extra = service.getExtra(paymentTradeResultVo.getThirdPartyMsg());
                        if (StringUtils.isNotBlank(extra.getDetail())) {
                            PayCallBack payCallBack = JSONUtils.parseObject(extra.getDetail(), PayCallBack.class);
                            service.handleError(payCallBack, payResultVo.getReturn_msg());
                        }
                        result.put("status", PaymentConstants.CANCEL_SUCCESS);
                        return result;
                    } else {
                        msg = payResultVo.getReturn_msg();
                    }
                }

                result.put("status", PaymentConstants.CANCEL_FAIL);
                result.put("err_msg", msg);
                return result;
            default:
                throw new FrameworkRuntimeException(ResultCode.Fail, "不支持撤销的方式");
        }
    }

    @Override
    public Map<String, String> query(PaymentTradeOrderForm paymentTradeOrderForm) {
        PaymentTradeOrderBo paymentTradeOrderBo = new PaymentTradeOrderBo();
        paymentTradeOrderBo.setEnterpriseid(paymentTradeOrderForm.getEnterpriseid());
        paymentTradeOrderBo.setTradeNumber(paymentTradeOrderForm.getTradeNumber());
        PaymentTradeOrderVo paymentTradeOrderVo = service.findByEidAndTradeNum(paymentTradeOrderBo);
        if (paymentTradeOrderVo == null) {
            throw new FrameworkRuntimeException(ResultCode.Fail, "不存在的账单");
        }
        LOGGER.info("查询" + JSONUtils.toJSONString(paymentTradeOrderVo));
        Map<String, String> result = new HashMap<>(5);
        if (!PaymentConstants.PATMENT_STATUS_PAYING.equals(paymentTradeOrderVo.getStatus())) {
            switch (paymentTradeOrderVo.getStatus()) {
                case PaymentConstants.PATMENT_STATUS_NOPAY:
                    PaymentTradeResultVo resultVo = resultService.findByTradeId(paymentTradeOrderVo.getTradeid());
                    if (resultVo != null) {
                        result.put("status", resultVo.getStatus());
                        result.put("err_msg", resultVo.getDescription());
                    } else {
                        result.put("status", PaymentConstants.STATUS_NOT_PAY);
                    }

                    break;
                case PaymentConstants.PATMENT_STATUS_PAYED:
                    result.put("status", PaymentConstants.STATUS_PAYED);
                    break;
                default:
                    result.put("status", PaymentConstants.STATUS_ERROR);
                    break;
            }
            return result;
        }
        //支付中的状态
        else {
            PaymentTradeOrderExtVo extOrderVo = extService.findByTradeid(paymentTradeOrderVo.getTradeid());
            if (extOrderVo == null) {
                throw new FrameworkRuntimeException(ResultCode.Fail, "错误的账单");
            }

            switch (extOrderVo.getMode()) {
                case PayConstants.PAYTYPE_PAYCARD:
                case PayConstants.PAYTYPE_WEIXIN:
                case PayConstants.PAYTYPE_QRCODE:
                    PaymentTradeResultBo paymentTradeResultBo = new PaymentTradeResultBo();
                    paymentTradeResultBo.setTradeid(paymentTradeOrderVo.getTradeid());
                    paymentTradeResultBo.setStatus(PaymentConstants.STATUS_PAYING);
                    paymentTradeResultBo.setMode(extOrderVo.getMode());
                    paymentTradeResultBo.setEnterpriseid(paymentTradeOrderForm.getEnterpriseid());
                    List<PaymentTradeResultVo> list = resultService.list(paymentTradeResultBo);
                    if (list.size() != 1) {
                        throw new FrameworkRuntimeException(ResultCode.Fail, "账单出错,不能查询");
                    }

                    PaymentTradeResultVo paymentTradeResultVo = list.get(0);

                    PaymentOrderBo paymentOrderBo = new PaymentOrderBo();
                    paymentOrderBo.setEnterpriseid(paymentTradeResultVo.getEnterpriseid());
                    paymentOrderBo.setTradeNumber(paymentTradeResultVo.getTransactionNumber());
                    String data = iPayFeginClient.orderquery(paymentOrderBo);
                    Map<Object, Object> map = JSONUtils.parseObject(data, Object.class, Object.class);

                    String code = map.get("code").toString();
                    String msg = map.get("msg").toString();
                    String obj = map.get("data").toString();

                    if (ResultCode.Success.equals(code)) {
                        PayResultVo payResultVo = JSONUtils.parseObject(obj, PayResultVo.class);
                        LOGGER.info(JSONUtils.toJSONString(payResultVo));
                        if (payResultVo != null) {
                            switch (payResultVo.getReturn_code()) {
                                //支付中
                                case PayConstants.TRADE_PAYSTATUS_IN:
                                    result.put("status", PaymentConstants.STATUS_PAYING);
                                    return result;
                                //支付完成
                                case PayConstants.TRADE_PAYSTATUS_SUCCESS:
                                    result.put("status", PaymentConstants.STATUS_PAYED);
                                    handleCallBack(payResultVo.getPushVo());
                                    return result;
                                //支付失败
                                case PayConstants.TRADE_PAYSTATUS_ERROR:
                                    result.put("status", PaymentConstants.STATUS_ERROR);
                                    result.put("err_msg", payResultVo.getReturn_msg());

                                    PaymentTradeExtraVo extra = service.getExtra(paymentTradeResultVo.getThirdPartyMsg());
                                    if (StringUtils.isNotBlank(extra.getDetail())) {
                                        PayCallBack payCallBack = JSONUtils.parseObject(extra.getDetail(), PayCallBack.class);
                                        service.handleError(payCallBack, payResultVo.getReturn_msg());
                                    }

                                    return result;
                                case PayConstants.WECHAT_ORDER_NOT_EXIST:
                                    result.put("status", PaymentConstants.STATUS_ERROR);
                                    result.put("err_msg", payResultVo.getReturn_msg());
                                    return result;
                            }
                        } else {
                            msg = payResultVo.getReturn_msg();
                        }
                    }

                    result.put("status", PaymentConstants.CANCEL_FAIL);
                    result.put("err_msg", msg);
                    return result;
                default:
                    throw new FrameworkRuntimeException(ResultCode.Fail, "未知的支付方式");
            }
        }
    }

    @Override
    public List<LadderPriceDetailVo> detail(PaymentTradeOrderForm paymentTradeOrderForm) {
        if (paymentTradeOrderForm.getTradeNumber() == null) {
            throw new FrameworkRuntimeException(ResultCode.Fail, "请传账单号");
        }

        OrderExtVo vo = iOrderService.findOrderExtByTradeno(paymentTradeOrderForm.getTradeNumber());
        if (vo != null && vo.getChargeinfo() != null) {
            List<LadderPriceDetailVo> list = JSONUtils.parseArray(vo.getChargeinfo(), LadderPriceDetailVo.class);
            return list;
        }
        return null;
    }

    @Override
    public PaymentTradeOrderDetailVo orderTradeDetail(PaymentTradeOrderForm paymentTradeOrderForm) {
        PaymentTradeOrderBo bo = BeanUtils.copy(paymentTradeOrderForm, PaymentTradeOrderBo.class);
        PaymentTradeOrderVo vo = service.get(bo);
        if (vo == null) {
            throw new FrameworkRuntimeException(ResultCode.Fail, "没有该账单");
        }
        PaymentTradeOrderDetailVo paymentTradeOrderDetailVo = BeanUtils.copy(vo, PaymentTradeOrderDetailVo.class);


        //判断是否合并缴费的账单
        if (vo.getTradeid().equals(vo.getMerge()) || vo.getMerge() == null) {
            paymentTradeOrderDetailVo.setIsMerge("0");

            if (PaymentConstants.PATMENT_STATUS_PAYED.equals(vo.getStatus())) {
                PaymentTradeOrderExtVo extVo = extService.findByTradeid(vo.getTradeid());
                if (extVo != null) {
                    paymentTradeOrderDetailVo.setActualamount(extVo.getActualamount());
                    paymentTradeOrderDetailVo.setCost(extVo.getCost());
                    paymentTradeOrderDetailVo.setPenalty(extVo.getPenalty());
                    paymentTradeOrderDetailVo.setTotalPenalty(extVo.getPenalty());
                    paymentTradeOrderDetailVo.setCouponMoney(extVo.getCouponMoney());
                    paymentTradeOrderDetailVo.setBalance(extVo.getBalance());
                    paymentTradeOrderDetailVo.setChange(extVo.getChange());
                }
            }

        } else {
            paymentTradeOrderDetailVo.setIsMerge("1");

            PaymentTradeOrderBo param = new PaymentTradeOrderBo();
            param.setMerge(vo.getMerge());
            List<PaymentTradeOrderVo> orders = service.list(param);
            paymentTradeOrderDetailVo.setOrders(orders);
            if (orders.size() > 1 && PaymentConstants.PATMENT_STATUS_PAYED.equals(vo.getStatus())) {

                //平账
                double couponMoney = 0.0;
                double balance = 0.0;
                double actualAmount = 0.0;
                double change = 0.0;
                double cost = 0.0;
                double penalty = 0.0;

                for (PaymentTradeOrderVo paymentTradeOrderVo : orders) {
                    PaymentTradeOrderExtVo extVo = extService.findByTradeid(paymentTradeOrderVo.getTradeid());
                    if (extVo != null) {
                        couponMoney = CalUtil.add(couponMoney, Double.parseDouble(extVo.getCouponMoney()));
                        balance = CalUtil.add(balance, Double.parseDouble(extVo.getBalance()));
                        actualAmount = CalUtil.add(actualAmount, Double.parseDouble(extVo.getActualamount()));
                        change = CalUtil.add(change, Double.parseDouble(extVo.getChange()));
                        cost = CalUtil.add(cost, Double.parseDouble(extVo.getCost()));
                        penalty = CalUtil.add(penalty, Double.parseDouble(extVo.getPenalty()));
                    }
                }
                paymentTradeOrderDetailVo.setActualamount(String.valueOf(actualAmount));
                paymentTradeOrderDetailVo.setCost(String.valueOf(cost));
                paymentTradeOrderDetailVo.setTotalPenalty(String.valueOf(penalty));
                paymentTradeOrderDetailVo.setCouponMoney(String.valueOf(couponMoney));
                paymentTradeOrderDetailVo.setBalance(String.valueOf(balance));
                paymentTradeOrderDetailVo.setChange(String.valueOf(change));
            }
        }

        //交易流水
        PaymentTradeProBo proParam = new PaymentTradeProBo();
        proParam.setTradeid(vo.getTradeid());
        List<PaymentTradeProVo> proVos = proService.list(proParam);
        paymentTradeOrderDetailVo.setProVos(proVos);

        if (PaymentConstants.BUSSINESS_TYPE_WATERFEE.equals(paymentTradeOrderDetailVo.getBusinessId())) {
            OrderExtVo detail = iOrderService.findOrderExtByTradeno(paymentTradeOrderDetailVo.getTradeNumber());
            if (detail != null && detail.getChargeinfo() != null) {
                List<LadderPriceDetailVo> list = JSONUtils.parseArray(detail.getChargeinfo(), LadderPriceDetailVo.class);
                paymentTradeOrderDetailVo.setDetails(list);

                if (detail.getPaytypeinfo() != null) {
                    PayTypeVo payTypeVo = JSONUtils.parseObject(detail.getPaytypeinfo(), PayTypeVo.class);
                    paymentTradeOrderDetailVo.setPayTypeVo(payTypeVo);
                }
            }
        }

        return paymentTradeOrderDetailVo;
    }

    @Override
    public Map<String, String> recharge(PaymentTradeOrderExtForm ext) {

        //第一步：先生成支付账单
        PaymentTradeOrderForm paymentTradeOrderForm = new PaymentTradeOrderForm();
        paymentTradeOrderForm.setBusinessId(PaymentConstants.BUSSINESS_TYPE_RECHARGE);
        paymentTradeOrderForm.setTradeNumber(String.valueOf(Config.Generator.nextId()));
        paymentTradeOrderForm.setTradeName("用户充值-" + DateUtils.format(new Date(), DateUtils.YYYYMMDDHHMMSS));
        paymentTradeOrderForm.setAmount(ext.getAmount());
        paymentTradeOrderForm.setEnterpriseid(ext.getEnterpriseid());

        OwnerBo ownerBo = new OwnerBo();
        ownerBo.setOwnerid(ext.getOwnerid());
        OwnerVo ownerVo = iOwnerService.findByOwnerId(ownerBo);
        if (ownerVo == null) {
            throw new FrameworkRuntimeException(ResultCode.OWNER_NO_EXIST_ERROR, ResultCode.getMessage(ResultCode.OWNER_NO_EXIST_ERROR));
        }
        paymentTradeOrderForm.setUserid(ext.getOwnerid());
        paymentTradeOrderForm.setUserName(ownerVo.getUsername());
        paymentTradeOrderForm.setUserno(ownerVo.getUserno());
        paymentTradeOrderForm.setUserAddr(ownerVo.getUseraddr());

        PaymentTradeOrderVo paymentTradeOrderVo = add(paymentTradeOrderForm);

        //第二步 提交到支付平台
        List<String> tradeIds = new ArrayList<>();
        tradeIds.add(paymentTradeOrderVo.getTradeid());
        ext.setTradeIds(tradeIds);
        Map<String, String> result = pay(ext);

        //不是现金要返回tradeNumber给前端
        result.put("tradeNumber", paymentTradeOrderVo.getTradeNumber());

        return result;
    }

    @Override
    public void handleRecharge(String ownerId, String rechargeMoney, String tradeNumber) {
        OwnerBo ownerBo = new OwnerBo();
        ownerBo.setOwnerid(ownerId);
        OwnerVo ownerVo = iOwnerService.findByOwnerId(ownerBo);
        if (ownerVo == null) {
            throw new FrameworkRuntimeException(ResultCode.OWNER_NO_EXIST_ERROR, ResultCode.getMessage(ResultCode.OWNER_NO_EXIST_ERROR));
        }

        Double afterMoney = CalUtil.add(ownerVo.getAlreadypay(), Double.parseDouble(rechargeMoney));

        //更新余额
        PayDetailBo payDetail = new PayDetailBo();
        payDetail.setId(UuidUtils.getUuid());
        payDetail.setOwnerid(ownerVo.getOwnerid());
        payDetail.setOwnerno(ownerVo.getUserno());
        payDetail.setOwnername(ownerVo.getUsername());
        payDetail.setPaymoney(new BigDecimal(rechargeMoney));
        payDetail.setCreatetime(new Date());

        payDetail.setType(WaterConstants.PAY_DETAIL_TYPE_IN);
        payDetail.setBeforemoney(new BigDecimal(ownerVo.getAlreadypay()));
        payDetail.setAftermoney(new BigDecimal(afterMoney));
        payDetail.setPayno(tradeNumber);
        payDetail.setTradeno(tradeNumber);
        payDetail.setRemark("用户充值");
        iPayDetailService.addPayDetail(payDetail);

        iOwnerService.updateOwnerAccount(afterMoney, ownerVo.getOwnerid());


        //勾选自动扣费
        if (ownerVo.getIschargebacks() != null && ownerVo.getIschargebacks() == 1) {
            //TODO 找出欠费账单
            List<PaymentTradeOrderVo> list;
            PaymentTradeOrderBo paymentTradeOrderBo = new PaymentTradeOrderBo();
            paymentTradeOrderBo.setStatus(PaymentConstants.PATMENT_STATUS_NOPAY);
            paymentTradeOrderBo.setBusinessId(PaymentConstants.BUSSINESS_TYPE_WATERFEE);
            paymentTradeOrderBo.setUserid(ownerId);
            list = service.list(paymentTradeOrderBo);
            List<String> tradeIds = new ArrayList<>();
            LOGGER.info(ownerVo.getUsername() + "共[" + list.size() + "]个未缴账单...");
            // 拖欠费用
            Double arrears = 0.0;
            for (PaymentTradeOrderVo noPayOrder : list) {
                arrears = CalUtil.add(CalUtil.add(noPayOrder.getPenalty(), noPayOrder.getAmount()).doubleValue(), arrears);
                tradeIds.add(noPayOrder.getTradeid());
            }
            if (arrears <= 0.0) {
                LOGGER.info(ownerVo.getUsername() + " 没有欠费");
                stringListCache.leftPush(QueueName, JSONUtils.toJSONString(ownerVo));
                return;
            }

            //充值后金额大于或等于欠费账单
            if (afterMoney >= arrears) {
                PaymentTradeOrderExtForm ext = new PaymentTradeOrderExtForm();
                ext.setTradeIds(tradeIds);
                ext.setActualamount("0");
                //余额抵扣
                ext.setBalance(String.valueOf(arrears));
                ext.setChange("0");
                ext.setMode(PayConstants.PAYTYPE_MONEY);
                ext.setEnterpriseid(ownerVo.getEnterpriseid());
                ext.setOwnerid(ownerVo.getOwnerid());

                Map<String, String> result = pay(ext);
                if (result != null && PaymentConstants.STATUS_PAYED.equals(result.get("status"))) {
                    LOGGER.info(ownerVo.getUsername() + " 充值后余额成功抵扣水费");

                    ownerVo.setArrears(arrears);
                    stringListCache.leftPush(QueueName, JSONUtils.toJSONString(ownerVo));

                }

            } else {
                LOGGER.info(ownerVo.getUsername() + " 充值后还是余额不足，不能自动扣费");
            }
        } else {
            //看看是不是低于系统设置的金额，低于发短信通知
            stringListCache.leftPush(QueueName, JSONUtils.toJSONString(ownerVo));
        }
    }

    @Override
    public void receive() {
        LOGGER.info("监听队列开始...");
        while (true) {
            String data = stringListCache.rightPop(QueueName, 0L);
            if (data != null) {
                try {
                    OwnerVo ownerVo = JSONUtils.parseObject(data, OwnerVo.class);
                    rechargeSmsSend(ownerVo.getArrears(), ownerVo);
                    sendBalanceSms(ownerVo.getOwnerid(), ownerVo.getEnterpriseid());
                } catch (Exception ex) {
                    LOGGER.error("redis队列出错", ex.getMessage());
                }
            }
        }
    }

    private void rechargeSmsSend(Double amount, OwnerVo owner) {
        try {
            if (amount > 0 && VerificationUtils.regex(owner.getUserphone(), VerificationUtils.REG_PHONE)) {
                OwnerBo ownerBo = new OwnerBo();
                ownerBo.setUsername(owner.getUsername());
                OrderBo orderBo = new OrderBo();
                orderBo.setMonth(String.valueOf(new Date().getMonth() + 1));
                orderBo.setAmount(amount);
                iPushMsgFactory.sendMsg(null, owner.getUserphone(), 1, owner.getEnterpriseid(),
                        SmsEnum.pay_fee.intValue(),
                        ownerBo, orderBo);
            }
        } catch (Exception ex) {
            LOGGER.error(owner.getUsername() + "缴费成功短信发送失败");
        }

    }

    /**
     * 发送余额不足通知
     *
     * @param ownerId
     * @param enterpriseId
     * @author YangKe
     * @date 2019-12-12 15:37
     */
    private void sendBalanceSms(String ownerId, String enterpriseId) {
        try {
            if (StringUtils.isNotEmpty(ownerId) && StringUtils.isNotEmpty(enterpriseId)) {
                //获取水司是否配置开启余额不足通知
                SettlementVo settlementVo = iSettlementService.getSettlement(enterpriseId);
                if (settlementVo != null) {
                    if (settlementVo.getIsNotice() == 1 && settlementVo.getBalance() != null && StringUtils.isNotBlank(settlementVo.getSmsTemplate())) {
                        //获取短信模板信息
                        SmsTemplateBo smsTemplateBo = new SmsTemplateBo();
                        smsTemplateBo.setId(settlementVo.getSmsTemplate());
                        SmsTemplateVo smsTemplateVo = iSmsTemplateService.getSmsTemplateVo(smsTemplateBo);
                        if (smsTemplateVo != null && smsTemplateVo.getSmstype() != null) {
                            //获取业主信息
                            OwnerBo ownerBo = new OwnerBo();
                            ownerBo.setOwnerid(ownerId);
                            OwnerVo ownerVo = iOwnerService.findByOwnerId(ownerBo);
                            if (ownerVo != null && StringUtils.isNotEmpty(ownerVo.getUserphone()) && ownerVo.getAlreadypay() != null) {
                                //如果设置的最低通知余额大于用户所剩余额则发送通知
                                if (settlementVo.getBalance().compareTo(new BigDecimal(ownerVo.getAlreadypay())) == 1) {
                                    // 生成批次号
                                    String batchNo = String.valueOf(Config.Generator.nextId());
                                    Map<String, String> map = new HashMap<>();
                                    map.put("title", "余额不足通知");
                                    map.put("name", ownerVo.getUsername());
                                    map.put("money", ownerVo.getAlreadypay().toString());
                                    map.put("phone", ownerVo.getUserphone());
                                    iSmsToolService.sendSMS(enterpriseId, smsTemplateVo.getSmstype(), map, batchNo);
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) {
            LOGGER.error("发送余额不足通知失败", ex.getMessage());
        }
    }
}

