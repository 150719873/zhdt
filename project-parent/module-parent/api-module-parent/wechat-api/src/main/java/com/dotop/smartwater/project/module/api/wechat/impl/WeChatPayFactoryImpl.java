package com.dotop.smartwater.project.module.api.wechat.impl;

import com.alibaba.fastjson.JSONObject;
import com.dotop.smartwater.dependence.cache.api.AbstractValueCache;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.utils.DateUtils;
import com.dotop.smartwater.dependence.core.utils.JSONUtils;
import com.dotop.smartwater.project.module.api.revenue.INumRuleSetFactory;
import com.dotop.smartwater.project.module.api.revenue.IPaymentFactory;
import com.dotop.smartwater.project.module.api.wechat.IWechatCommonFactory;
import com.dotop.smartwater.project.module.api.wechat.IWechatPayFactory;
import com.dotop.smartwater.project.module.service.revenue.*;
import com.dotop.smartwater.project.module.client.third.http.wechat.CaptchaUtil;
import com.dotop.smartwater.project.module.client.third.http.wechat.WechatUtil;
import com.dotop.smartwater.project.module.core.auth.bo.SendMsgBo;
import com.dotop.smartwater.project.module.core.auth.bo.WechatMessageParamBo;
import com.dotop.smartwater.project.module.core.auth.enums.SmsEnum;
import com.dotop.smartwater.project.module.core.auth.vo.EnterpriseVo;
import com.dotop.smartwater.project.module.core.auth.vo.UserVo;
import com.dotop.smartwater.project.module.core.auth.wechat.WechatAuthClient;
import com.dotop.smartwater.project.module.core.auth.wechat.WechatUser;
import com.dotop.smartwater.project.module.core.third.bo.wechat.TradeRequestBo;
import com.dotop.smartwater.project.module.core.third.bo.wechat.WechatOrderBo;
import com.dotop.smartwater.project.module.core.third.enums.wechat.TradeState;
import com.dotop.smartwater.project.module.core.third.enums.wechat.WechatOrderState;
import com.dotop.smartwater.project.module.core.third.enums.wechat.WechatOrderType;
import com.dotop.smartwater.project.module.core.third.utils.wechat.MessageManager;
import com.dotop.smartwater.project.module.core.third.vo.wechat.WechatOrderVo;
import com.dotop.smartwater.project.module.core.third.vo.wechat.WeixinOrderQueryVo;
import com.dotop.smartwater.project.module.core.water.bo.*;
import com.dotop.smartwater.project.module.core.water.bo.customize.LadderPriceDetailBo;
import com.dotop.smartwater.project.module.core.water.bo.customize.WechatParamBo;
import com.dotop.smartwater.project.module.core.water.constants.*;
import com.dotop.smartwater.project.module.core.water.form.OrderExtForm;
import com.dotop.smartwater.project.module.core.water.form.PaymentTradeOrderForm;
import com.dotop.smartwater.project.module.core.water.form.customize.MakeNumRequest;
import com.dotop.smartwater.project.module.core.water.form.customize.OrderPayParamForm;
import com.dotop.smartwater.project.module.core.water.form.customize.WechatParamForm;
import com.dotop.smartwater.project.module.core.water.utils.BusinessUtil;
import com.dotop.smartwater.project.module.core.water.utils.CalUtil;
import com.dotop.smartwater.project.module.core.water.utils.IpAdrressUtil;
import com.dotop.smartwater.project.module.core.water.vo.*;
import com.dotop.smartwater.project.module.core.water.vo.customize.JSApiTicketVo;
import com.dotop.smartwater.project.module.core.water.vo.customize.LadderPriceDetailVo;
import com.dotop.smartwater.project.module.core.water.vo.customize.MakeNumVo;
import com.dotop.smartwater.project.module.core.water.vo.customize.WechatPublicSettingVo;
import com.dotop.smartwater.project.module.service.revenue.*;
import com.dotop.smartwater.project.module.service.tool.ISmsToolService;
import com.dotop.smartwater.project.module.service.tool.IWechatPublicSettingService;
import com.dotop.smartwater.project.module.service.wechat.IWechatPayService;
import com.dotop.water.tool.service.BaseInf;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * 迁移改造
 *

 * @date 2019年3月22日
 */
@Component
public class WeChatPayFactoryImpl implements IWechatPayFactory {

    private static final Logger logger = LogManager.getLogger(WeChatPayFactoryImpl.class);

    @Autowired
    private WechatUtil wechatUtil;

    @Autowired
    private IOwnerService iOwnerService;

    @Autowired
    private IOrderService iOrderService;

    @Autowired
    private IPayDetailService iPayDetailService;

    @Autowired
    private ICouponService iCouponService;

    @Autowired
    private IWechatPayService iWechatPayService;

    @Autowired
    private IWechatPublicSettingService iWechatPublicSettingService;

    @Autowired
    protected AbstractValueCache<String> avc;

    @Autowired
    private ISmsToolService iSmsToolService;

    @Autowired
    private INumRuleSetFactory iNumRuleSetFactory;

    @Autowired
    private IWechatCommonFactory iWechatCommonFactory;

    @Autowired
    private IPaymentTradeOrderExtService extService;

    @Autowired
    private IPaymentTradeOrderService service;

    @Autowired
    private IPaymentFactory iPaymentFactory;

    @Override
    public JSApiTicketVo jSApiTicket(HttpServletRequest request) {
        String url = IpAdrressUtil.getIpAdrress(request);
        JSApiTicketVo jsapiTicket = null;

        String token = request.getHeader(WechatConstants.token);
        // 校验当前登录业主是否过户或者销户
        String ownerId = avc.get(CacheKey.WaterWechatOwnerid + token, String.class);

        if (StringUtils.isBlank(ownerId)) {
            throw new FrameworkRuntimeException(ResultCode.Fail, "ownerId 缓存没有获取到");
        }

        OwnerBo ownerBo = new OwnerBo();
        ownerBo.setOwnerid(ownerId);

        // 调用存在的方法
        OwnerVo currentOwner = iOwnerService.findByOwnerId(ownerBo);
        if (currentOwner == null) {
            throw new FrameworkRuntimeException(ResultCode.WECHAT_NOT_OWNER_ERROR, "未有绑定业主");
            // 校验当前登录业主是否过户或者销户
        } else if (currentOwner.getStatus().intValue() == 0) {
            throw new FrameworkRuntimeException(ResultCode.WECHAT_OWNER_NOT_EXIST_ERROR, "业主已经过户或者销户");
        }

        WechatPublicSettingVo weixinConfig = iWechatPublicSettingService
                .getByenterpriseId(currentOwner.getEnterpriseid());
        WechatPublicSettingBo wechatPublicSettingbo = new WechatPublicSettingBo();
        BeanUtils.copyProperties(weixinConfig, wechatPublicSettingbo);

        String ticket = wechatUtil.getJsApiTicket(wechatPublicSettingbo);

        if (ticket != null) {
            jsapiTicket = new JSApiTicketVo();
            Map<String, String> configMap = new HashMap<>();
            String timestamp = String.valueOf(System.currentTimeMillis() / 1000);
            String nonceStr = CaptchaUtil.getAlphabetCaptcha(30);
            configMap.put("jsapi_ticket", ticket);
            configMap.put("timestamp", timestamp);
            configMap.put("nonceStr", nonceStr);
            configMap.put("url", url);
            String sign = WechatUtil.sign(configMap, null);
            jsapiTicket.setAppId(weixinConfig.getAppid());
            jsapiTicket.setNonceStr(nonceStr);
            jsapiTicket.setSignature(sign);
            jsapiTicket.setTimestamp(timestamp);
        }
        return jsapiTicket;
    }

    /**
     * 检测支付前是否有账单状态已经改变，不是等待支付的状态
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
    public void checkOrderStatus(WechatParamForm wechatParamForm) {
        logger.debug("======================================");
        try {
            if (StringUtils.isNotBlank(wechatParamForm.getOrderids())) {

                String[] idsarr = wechatParamForm.getOrderids().split(",");
                List<String> idList = Arrays.asList(idsarr);

                List<PaymentTradeOrderVo> list = service.findListByIds(idList);

                List<PaymentTradeOrderVo> cancellist = new ArrayList<>();

                for (PaymentTradeOrderVo vo : list) {
                    if (PaymentConstants.PATMENT_STATUS_PAYED.equals(vo.getStatus())) {
                        throw new FrameworkRuntimeException(ResultCode.Fail, "账单状态已改变，请刷新账单列表");
                    }

                    //找出需要撤单的单
                    if (PaymentConstants.PATMENT_STATUS_PAYING.equals(vo.getStatus())) {
                        cancellist.add(vo);
                    }
                }

                if(cancellist.size() == 0){
                    return;
                }

                HashSet<String> set = new HashSet<>();
                for (PaymentTradeOrderVo vo : cancellist) {
                    if (StringUtils.isNotBlank(vo.getMerge())) {
                        set.add(vo.getMerge());
                    }
                }
                //合并缴费的账单撤单
                for(String meger : set){
                    PaymentTradeOrderBo param = new PaymentTradeOrderBo();
                    param.setMerge(meger);
                    List<PaymentTradeOrderVo> orders = service.list(param);
                    if(orders.size() > 0){
                        PaymentTradeOrderForm form = new PaymentTradeOrderForm();
                        BeanUtils.copyProperties(orders.get(0),form);
                        try{
                            iPaymentFactory.cancel(form);
                        }catch (FrameworkRuntimeException ex){
                            logger.error(ex.getMsg());
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.info(e.getMessage());
            logger.debug("Exception", e);
            throw new FrameworkRuntimeException(ResultCode.Fail, "检测账单信息时出现异常");
        }

    }

    private boolean revokePay(OrderVo order, PayDetailVo paydetail, OwnerVo owner) {
        boolean temp = false;
        try {
            // 修改业主信息 的 已付账单金额
            if (paydetail != null && owner != null) {
                owner.setAlreadypay(CalUtil.add(owner.getAlreadypay(), paydetail.getPaymoney().doubleValue()));
                iOwnerService.updateOwnerAccount(owner.getAlreadypay(), owner.getOwnerid());
            }

            // 删除账户变动信息
            iPayDetailService.deletePayDetail(order.getTradeno());

            // 删除交易记录信息
            iOrderService.deleteTradePay(order.getPayno());

            // 修改代金券状态为未使用
            iOrderService.updateCouponStatus(order.getTradeno());

            order.setPayno("");
            order.setCouponmoney(0.0);
            order.setBalance(0.0);
            order.setRealamount(0.0);
            order.setPaystatus(0);
            order.setPaytime("");

            OrderBo orderBo = new OrderBo();
            BeanUtils.copyProperties(order, orderBo);

            // 修改账单信息
            iOrderService.revokeOrder(orderBo);
            temp = true;
        } catch (Exception e) {
            logger.debug("Exception", e);
        }
        return temp;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
    public Map<String, Object> orderPayment(HttpServletRequest request, OrderPayParamForm orderPayParamForm) {

        TradeRequestBo tradeRequest = null;
        // Map<String, Object> map = new HashMap<>();
        Map<String, Object> map = null;
        WechatUser wechatUser = WechatAuthClient.get();
        String ownerId = wechatUser.getOwnerid();
        String enterpriseid = wechatUser.getEnterpriseid();
        String openid = wechatUser.getOpenid();
        OwnerBo ownerBo = new OwnerBo();
        ownerBo.setOwnerid(ownerId);

        // 调用存在的方法
        OwnerVo currentOwner = iOwnerService.findByOwnerId(ownerBo);
        logger.info(currentOwner);
        if (currentOwner == null) {
            throw new FrameworkRuntimeException(ResultCode.WECHAT_NOT_OWNER_ERROR, "未有绑定业主");
            // 校验当前登录业主是否过户或者销户
        } else if (currentOwner.getStatus().intValue() == 0) {
            throw new FrameworkRuntimeException(ResultCode.WECHAT_OWNER_NOT_EXIST_ERROR, "业主已经过户或者销户");
        }

        // 前端请求ip地址
        if (StringUtils.isBlank(orderPayParamForm.getIp())) {
            String ip = IpAdrressUtil.getIpAdrress(request);
            orderPayParamForm.setIp(ip);
        }

        WechatPublicSettingVo weixinConfig = iWechatPublicSettingService
                .getByenterpriseId(currentOwner.getEnterpriseid());
        OrderVo order = null;
        if (StringUtils.isBlank(orderPayParamForm.getTradeno())) {
            String[] ids = orderPayParamForm.getOrderids().split(",");
            // 验证单个账单或者是多个账单，如果多账单支付则合并账单为新的账单，原账单隐藏
            if (ids.length == 1) {
                orderPayParamForm.setId(orderPayParamForm.getOrderids());
            } else if (ids.length > 1) {
                UserVo user = new UserVo();
                user.setName("微信合并缴费");
                orderPayParamForm.setId(iOrderService.mergeOrders(orderPayParamForm.getOrderids(), user));
            }

            order = iOrderService.findById(orderPayParamForm.getId());
        } else {
            order = iOrderService.findOrderByTradeNo(orderPayParamForm.getTradeno());
        }

        if (order == null) {
            throw new FrameworkRuntimeException(ResultCode.WECHATORDERERROR, "账单不存在");
        } else {
            if (order.getTradestatus() == 0) { // （1-正常 0-异常）
                throw new FrameworkRuntimeException(ResultCode.WECHATORDERERROR, "账单状态异常");
            } else {
                if (order.getPaystatus() == 1) { // 缴费状态（0-未缴 1-已缴）
                    throw new FrameworkRuntimeException(ResultCode.WECHAT_ORDER_PAY_SUCCESS_ERROR, "账单已经缴清");
                }
            }
        }

        OrderExtVo orderExt = iOrderService.findOrderExtByTradeno(order.getTradeno());
        if (orderExt == null) {
            throw new FrameworkRuntimeException(ResultCode.ParamIllegal, "错误的账单");
        }

        PayTypeVo payType = JSONUtils.parseObject(orderExt.getPaytypeinfo(), PayTypeVo.class);
        if (payType == null) {
            throw new FrameworkRuntimeException(ResultCode.ParamIllegal, "不存在的收费种类");
        }
        if (orderPayParamForm.getPaytype() == null) {
            // 设置为微信支付
            orderPayParamForm.setPaytype(WaterConstants.ORDER_PAYTYPE_WEIXIN);
        }
        // 校验业主账号余额
        // currentOwner
        // 注释 OwnerVo owner = iOwnerService.findByOwnerId(order.getOwnerid());
        logger.info("orderPayParamForm.getAlreadypay(): {}", orderPayParamForm.getAlreadypay());
        logger.info("currentOwner.getAlreadypay(): {}", currentOwner.getAlreadypay());
        if (!orderPayParamForm.getAlreadypay().equals(currentOwner.getAlreadypay())) {
            throw new FrameworkRuntimeException(ResultCode.WECHAT_ACCOUNT_ERROR_ERROR, "业主账号余额不正确");
        }

        // 检验余额抵扣
        Double ownerBalance = CalUtil.sub(orderPayParamForm.getAlreadypay(), orderPayParamForm.getBalance());
        if (ownerBalance < 0.0) {
            throw new FrameworkRuntimeException(ResultCode.WECHAT_ACCOUNT_ERROR_ERROR, "抵扣值不能大于业主余额");
        }
        Double amount = order.getAmount();
        // 水费组成明细
        List<LadderPriceDetailVo> payDetailList = BusinessUtil.getLadderPriceDetailVo(payType.getLadders(),
                order.getWater());
        // 校验滞纳金

        List<LadderPriceDetailBo> payDetailListBo = new ArrayList<>();
        for (LadderPriceDetailVo ladderPriceDetailVo : payDetailList) {
            LadderPriceDetailBo ladderPriceDetailBo = new LadderPriceDetailBo();
            BeanUtils.copyProperties(ladderPriceDetailVo, ladderPriceDetailBo);
            payDetailListBo.add(ladderPriceDetailBo);
        }

        List<CompriseBo> compriseBolist = new ArrayList<>();
        if (payType.getComprises() != null) {
            for (CompriseVo compriseVo : payType.getComprises()) {
                CompriseBo compriseBo = new CompriseBo();
                BeanUtils.copyProperties(compriseVo, compriseBo);
                compriseBolist.add(compriseBo);
            }
        }

        Double penalty = BusinessUtil.getPenalty(payDetailListBo, compriseBolist, order.getGeneratetime(),
                payType.getOverdueday());
        if (CalUtil.sub(penalty, orderPayParamForm.getPenalty()) != 0.0) {
            throw new FrameworkRuntimeException(ResultCode.WECHAT_ACCOUNT_ERROR_ERROR, "滞纳金金额计算不正确,不能进行缴费");
        }

        amount = CalUtil.add(amount, penalty);

        CouponVo coupon = null;
        if (orderPayParamForm.getCouponid() != null) {
            CouponBo couponBo = new CouponBo();
            couponBo.setCouponid(orderPayParamForm.getCouponid());
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
        amount = CalUtil.sub(amount, orderPayParamForm.getBalance());
        if (amount < 0) {
            amount = 0.0;
        }
        if (!amount.equals(orderPayParamForm.getRealamount())) {
            throw new FrameworkRuntimeException(ResultCode.ParamIllegal, "剩余应缴金额计算不正确,不能进行缴费");
        }
        if (orderPayParamForm.getOwnerpay().doubleValue() - amount < 0) {
            throw new FrameworkRuntimeException(ResultCode.ParamIllegal, "实缴金额少于剩余应缴金额,不能进行缴费");
        }

        orderPayParamForm.setPayMode(WaterConstants.ORDER_PAYTYPE_WEIXIN + "");

        OrderExtForm orderExtForm = new OrderExtForm();
        BeanUtils.copyProperties(orderPayParamForm, orderExtForm);
        // 支付前 先修改一些有关账单数据的信息
        Map<String, String> orderRequestData = iOrderService.orderPay(orderExtForm, order, null, ownerBalance,
                WaterConstants.ORDER_PAYTYPE_WEIXIN, coupon);

        MakeNumRequest makeNumRequest = new MakeNumRequest();
        makeNumRequest.setEnterpriseid(enterpriseid);
        makeNumRequest.setRuleid(WaterConstants.NUM_RULE_PAY);
        makeNumRequest.setCount(1);
        MakeNumVo makeNumVo = iNumRuleSetFactory.wechatMakeNo(makeNumRequest);
        String outTradeNO = makeNumVo.getNumbers().get(0);

        tradeRequest = new TradeRequestBo();
        tradeRequest.setAmount(amount);
        tradeRequest.setOperaterOwnerid(ownerId);
        tradeRequest.setWechatmchno(outTradeNO);
        tradeRequest.setIp(orderPayParamForm.getIp());
        OrderBo orderBo = new OrderBo();
        BeanUtils.copyProperties(order, orderBo);
        tradeRequest.setOrder(orderBo);

        // paycode为1，需要跳转微信支付页面支付 paycode为0，不用跳转微信支付页面支付
        if (amount > 0.0d) {
            map = this.weixinpay(tradeRequest, weixinConfig, openid, orderPayParamForm, order, ownerBalance, coupon,
                    orderRequestData, WaterConstants.WECHAT_PAY_AMOUNT);
            map.put("paycode", 1);
            map.put("tradeno", order.getTradeno());
        } else {
            map = this.weixinpay(tradeRequest, weixinConfig, openid, orderPayParamForm, order, ownerBalance, coupon,
                    orderRequestData, WaterConstants.ALREADY_PAY_AMOUNT);
            map.put("paycode", 0);
            map.put("tradeno", order.getTradeno());

            sendOrderMsg(new Date(), order.getOwnerid(), enterpriseid, order, openid);

            // 发送短信通知
            // Map<String, String> params = new HashMap<>();
            // params.put("date", order.getMonth());
            // params.put("name", currentOwner.getUsername());
            // params.put("money", String.valueOf(order.getAmount()));
            // params.put("phone", currentOwner.getUserphone());
            // iSmsToolService.sendSMS(enterpriseid, SmsEnum.pay_fee.intValue(), params,
            // null);

            // 微信推送 调用第三方接口
            // EnterpriseVo enterprise =
            // BaseInf.GetCompanyInfo(Long.parseLong(currentOwner.getEnterpriseid()));
            // if (enterprise != null) {
            // SendMsgBo sendMsgBo = new SendMsgBo();
            // WechatMessageParamBo wechatMessageParamBo = new WechatMessageParamBo();
            // wechatMessageParamBo.setMessageState(SmsEnum.pay_fee.intValue());
            // wechatMessageParamBo.setOwnerid(String.valueOf(currentOwner.getOwnerid()));
            // wechatMessageParamBo.setUserName(currentOwner.getUsername());
            // wechatMessageParamBo.setEnterpriseid(String.valueOf(currentOwner.getEnterpriseid()));
            // wechatMessageParamBo.setTradeno(order.getTradeno());
            // wechatMessageParamBo.setCreatetime(order.getGeneratetime());
            // wechatMessageParamBo.setEnterpriseName(enterprise == null ? "" :
            // enterprise.getName());
            // wechatMessageParamBo.setTradetime(DateUtils.formatDate(new Date()));
            // wechatMessageParamBo.setTrademoney(order.getRealamount());
            // sendMsgBo.setWechatMessageParam(wechatMessageParamBo);
            // sendMsgBo.setOpenId(openid);
            // sendMsgBo.setModeltype(2);
            // iSmsToolService.sendWeChatMsg(sendMsgBo);
            // } else {
            // logger.info("微信推送消息无法找到先关的企业信息");
            // }
        }
        return map;
    }

    @SuppressWarnings("unused")
    private Map<String, Object> weixinpay(TradeRequestBo tradeRequest, WechatPublicSettingVo weixinConfig,
                                          String openid, OrderPayParamForm orderPayParamForm, OrderVo order, Double ownerBalance, CouponVo coupon,
                                          Map<String, String> orderRequestData, int wechatPayAmountStatus) {

        String orderid = tradeRequest.getOrder().getId();
        String ownerid = tradeRequest.getOrder().getOwnerid();
        // 应缴金额
        Double amount = tradeRequest.getAmount();
        // 商户订单号
        String outTradeNO = tradeRequest.getWechatmchno();
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("amount", String.valueOf(amount));

        List<WechatOrderVo> orderWechatList = null;

        // 只使用余额支付
        if (wechatPayAmountStatus == WaterConstants.ALREADY_PAY_AMOUNT) {

            order.setPaystatus(WaterConstants.ORDER_PAYSTATUS_PAID);
            order.setPaytime(DateUtils.formatDate(new Date()));

            OrderBo orderBo = new OrderBo();
            BeanUtils.copyProperties(order, orderBo);
            // 修改账单
            iOrderService.updateOrder(orderBo);

            order.setPaystatus(WaterConstants.ORDER_PAYSTATUS_PAID);
            order.setPaytime(DateUtils.formatDate(new Date()));
            iOrderService.updateTradePayStatus(order.getPayno(), "" + WaterConstants.TRADE_PAYSTATUS_SUCCESS, "支付成功");

            // 验证当前账单下是否有子账单，如果存在则修改成已缴费
            iOrderService.updateSubOrder(order.getTradeno());

            resultMap.put("toPay", WaterConstants.HAD_PAID);

            // 夹杂使用微信支付
        } else if (wechatPayAmountStatus == WaterConstants.WECHAT_PAY_AMOUNT) {
            WechatOrderVo orderWechat = null;
            logger.debug("[Weixin]微信预下单检查是否已下单: orderid[{}]", orderid);
            orderWechatList = iWechatPayService.findPayingByOrderid(ownerid, orderid);
            for (WechatOrderVo orderWechatTemp : orderWechatList) {
                // 有支付成功的订单，返回异常
                if (orderWechatTemp.getWechatorderstatus().intValue() == TradeState.SUCCESS.intValue()) {
                    throw new FrameworkRuntimeException(ResultCode.WECHAT_ORDER_PAY_SUCCESS_ERROR, "该笔微信订单已经支付");
                    // 未支付的订单
                } else if (orderWechatTemp.getWechatorderstatus().intValue() == TradeState.NOTPAY.intValue()) {
                    orderWechat = orderWechatTemp;
                    outTradeNO = orderWechat.getWechatmchno();
                    // 用户支付中的订单
                } else if (orderWechatTemp.getWechatorderstatus().intValue() == TradeState.USERPAYING.intValue()) {
                    orderWechat = orderWechatTemp;
                    outTradeNO = orderWechat.getWechatmchno();
                    break;
                }
            }
            // 微信订单超过15分钟未支付，该笔单销掉,重新生成一笔新的订单
            if (orderWechat != null) {
                long afterMinute;
                afterMinute = getDateDiff(orderWechat.getCreatetime(), new Date());
                if (afterMinute > 15) {
                    orderWechat.setUpdatetime(new Date());
                    orderWechat.setWechatorderstatus(TradeState.PAYERROR.intValue());
                    orderWechat.setRemark("该笔订单超过15分钟未支付，系统自动关单");

                    WechatOrderBo orderWechatBo = new WechatOrderBo();
                    BeanUtils.copyProperties(orderWechat, orderWechatBo);
                    iWechatPayService.updateOrderStatus(orderWechatBo);
                }
                orderWechat = null;
            }
            /*if (orderWechat != null && !StringUtils.isEmpty(orderWechat.getPrepayid())) {
                logger.debug("[Weixin]微信预下单已下单: wechatmchno[{}] --> prepayId[{}]", outTradeNO,
                        orderWechat.getPrepayid());
                // 检查已下单的状态，查询订单
                // WechatOrder postTmp = orderWechat;

                WechatPublicSettingBo wechatPublicSettingBo = new WechatPublicSettingBo();
                BeanUtils.copyProperties(weixinConfig, wechatPublicSettingBo);
                WeixinOrderQueryVo orderQuery = WechatUtil.orderQuery(outTradeNO, wechatPublicSettingBo);
                if (orderQuery != null) {
                    if (MessageManager.SUCCESS.equalsIgnoreCase(orderQuery.getResultCode())) {

                        if (TradeState.NOTPAY.equals(orderQuery.getTradeState())
                                || TradeState.USERPAYING.equals(orderQuery.getTradeState())) {
                            // 订单处于待支付或者用户正在支付
                            logger.debug("[Weixin]微信订单查询 --> 等待支付: smOid[{}]", outTradeNO);
                            resultMap.putAll(unifiedorderSign(orderWechat.getPrepayid(), weixinConfig.getAppid(),
                                    weixinConfig.getPaysecret()));
                            resultMap.put("toPay", WaterConstants.CAN_NORMAL_PAY);
                            resultMap.put("wechatmchno", orderWechat.getWechatmchno());
                            return resultMap;
                        } else {
                            if (TradeState.SUCCESS.equals(orderQuery.getTradeState())) {
                                // 支付成功
                                logger.debug("[Weixin]微信订单查询 --> 支付成功: smOid[{}]", outTradeNO);
                                resultMap.put("toPay", WaterConstants.HAD_PAID);

                            } else if (TradeState.CLOSED.equals(orderQuery.getTradeState())
                                    || TradeState.PAYERROR.equals(orderQuery.getTradeState())) {
                                // 订单关闭 或者 订单支付失败
                                logger.debug("[Weixin]微信订单查询 --> 订单关闭或支付失败: smOid[{}]", outTradeNO);
                            } else if (TradeState.REVOKED.equals(orderQuery.getTradeState())) {
                                // 订单已撤销
                                logger.debug("[Weixin]微信订单查询 --> 订单已撤销: smOid[{}]", outTradeNO);
                            } else if (TradeState.REFUND.equals(orderQuery.getTradeState())) {
                                // 订单转入退款
                                logger.debug("[Weixin]微信订单查询 --> 订单转入退款: smOid[{}]", outTradeNO);
                            } else {
                                logger.debug("[Weixin]微信订单查询 --> 未知交易状态: smOid[{}]", outTradeNO);
                            }
                        }
                        logger.debug("[Weixin]微信订单查询 --> 数据库状态: wechatorderstatus:[{}],微信状态:[{}]",
                                orderWechat.getWechatorderstatus().intValue(), orderQuery.getTradeState().intValue());
                        if (orderQuery.getTradeState().intValue() != orderWechat.getWechatorderstatus().intValue()) {
                            orderWechat.setWechatorderstatus(orderQuery.getTradeState().intValue());
                            orderWechat.setWechatmchno(tradeRequest.getWechatmchno());
                            orderWechat.setWechatpaytime(new Date());
                            orderWechat.setUpdatetime(new Date());

                            WechatOrderBo wechatOrderBo = new WechatOrderBo();
                            BeanUtils.copyProperties(orderWechat, wechatOrderBo);
                            iWechatPayService.updateOrderRecord(wechatOrderBo);
                            // 更新账单表记录
                            if (orderQuery.getTradeState().intValue() == TradeState.SUCCESS.intValue()) {
                                if (orderRequestData.get("return_code") != null
                                        && orderRequestData.get("return_code").equals("SUCCESS")) {
                                    order.setPaystatus(WaterConstants.ORDER_PAYSTATUS_PAID);
                                    order.setPaytime(DateUtils.formatDate(new Date()));
                                    OrderBo orderBo = new OrderBo();
                                    BeanUtils.copyProperties(order, orderBo);
                                    iOrderService.updateOrder(orderBo);
                                    // 验证当前账单下是否有子账单，如果存在则修改成已缴费
                                    iOrderService.updateSubOrder(order.getTradeno());
                                } else {
                                    logger.debug(
                                            "请求内部服务：orderPay失败--> orderPayParam[{}] order[{}] ownerBalance[{}] payType[{}] coupon[{}]",
                                            orderPayParamForm, order, ownerBalance, WaterConstants.ORDER_PAYTYPE_WEIXIN,
                                            coupon);
                                }
                            }
                        }
                        return resultMap;
                    } else {
                        if ("ORDERNOTEXIST".equalsIgnoreCase(orderQuery.getErrCode())) {
                            logger.warn("[Weixin]微信订单查询 --> 订单号不存在[{}]", outTradeNO);
                        } else {
                            logger.warn("[Weixin]微信订单查询 --> 未处理错误[errCode{[]} errCodeDes[{}] tradeNO[{}] ]",
                                    orderQuery.getErrCode(), orderQuery.getErrCodeDes(), outTradeNO);
                        }
                    }
                } else {
                    logger.warn("[Weixin]微信订单查询失败: smOid[{}]", outTradeNO);
                }
            }*/

            if (orderWechat == null) {
                WechatPublicSettingBo weixinConfigBo = new WechatPublicSettingBo();
                BeanUtils.copyProperties(weixinConfig, weixinConfigBo);
                orderWechat = WechatUtil.unifiedorder(tradeRequest, weixinConfigBo, openid);
                if (MessageManager.SUCCESS.equalsIgnoreCase(orderWechat.getWechatresultcode())) {
                    logger.debug("[Weixin]微信支付预下单成功: smOid[{}] --> prepayId[{}]", outTradeNO,
                            orderWechat.getPrepayid());
                    resultMap.putAll(unifiedorderSign(orderWechat.getPrepayid(), weixinConfig.getAppid(),
                            weixinConfig.getPaysecret()));
                    resultMap.put("toPay", WaterConstants.CAN_NORMAL_PAY);
                    resultMap.put("wechatmchno", orderWechat.getWechatmchno());
                } else if (MessageManager.FAIL.equalsIgnoreCase(orderWechat.getWechatresultcode())) {
                    resultMap.put("toPay", WaterConstants.EXCEPTION);
                    if ("OUT_TRADE_NO_USED".equalsIgnoreCase(orderWechat.getWechatretruncode())) {
                        // 订单号重复，重新下单
                        // 生成新的订单号
                        logger.debug("[Weixin]微信支付预下单失败 --> 订单号重复[{}]", outTradeNO);
                    } else {
                        logger.debug("[Weixin]微信支付预下单失败: smOid[{}] --> tradeSN[{}] ERROR[{}]", outTradeNO,
                                orderWechat.getWechatmchno(), orderWechat.getWechatretruncode());
                        orderWechat.setWechatorderstatus(TradeState.PAYERROR.intValue());
                    }
                }
                // 默认正常
                orderWechat.setWechatorderstate(0);
                // 默认缴费
                orderWechat.setPaytype(WechatOrderType.pay_cost.intValue());
                // 无论统一下单成功与否，都要存储统一下单流水
                orderWechat.setOrderpayparam(JSONObject.toJSONString(orderPayParamForm));

                WechatOrderBo orderWechatBo = new WechatOrderBo();
                BeanUtils.copyProperties(orderWechat, orderWechatBo);
                iWechatPayService.saveTradePostAndRecord(orderWechatBo);
            }
        }
        return resultMap;

    }

    private Map<String, String> unifiedorderSign(String prepayId, String appId, String paySecret) {
        Map<String, String> signMap = new HashMap<>();
        signMap.put("appId", appId);
        signMap.put("timeStamp", String.valueOf(System.currentTimeMillis() / 1000));
        signMap.put("package", "prepay_id=" + prepayId);
        signMap.put("nonceStr", CaptchaUtil.getAlphabetCaptcha(30));
        signMap.put("signType", "MD5");
        String sign = WechatUtil.sign(signMap, paySecret);
        signMap.put("paySign", sign);
        return signMap;
    }

    /**
     * 参数 交易流水号 tradeno 微信下单编号 wechatmchno
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
    public void orderQuery(WechatParamForm wechatParamForm) throws FrameworkRuntimeException {
        WechatUser wechatUser = WechatAuthClient.get();
        String ownerId = wechatUser.getOwnerid();
        String openid = wechatUser.getOpenid();
        OwnerBo ownerBo = new OwnerBo();
        ownerBo.setOwnerid(ownerId);

        // 调用存在的方法
        OwnerVo currentOwner = iOwnerService.findByOwnerId(ownerBo);
        if (currentOwner == null) {
            throw new FrameworkRuntimeException(ResultCode.WECHAT_NOT_OWNER_ERROR, "未有绑定业主");
            // 校验当前登录业主是否过户或者销户
        } else if (currentOwner.getStatus().intValue() == 0) {
            throw new FrameworkRuntimeException(ResultCode.WECHAT_OWNER_NOT_EXIST_ERROR, "业主已经过户或者销户");
        }

        WechatPublicSettingVo weixinConfig = iWechatPublicSettingService
                .getByenterpriseId(currentOwner.getEnterpriseid());
        OrderVo order = iOrderService.findOrderByTradeNo(wechatParamForm.getTradeno());
        if (order == null) {
            throw new FrameworkRuntimeException(ResultCode.WECHATORDERERROR, "账单不存在");
        } else {
            if (order.getTradestatus() == 0) {
                throw new FrameworkRuntimeException(ResultCode.WECHATORDERERROR, "账单状态异常");
            }
        }
        String ownerid = order.getOwnerid();
        String enterpriseid = order.getEnterpriseid();

        // 微信下单保存的记录
        List<WechatOrderVo> orderWechatList = iWechatPayService.findPayingByOrderid(ownerid, order.getId());

        // 微信账单记录表
        WechatOrderVo orderWechat = null;

        // 缴费充值状态，true是缴费，false是充值，
        boolean payFlag = true;
        for (int i = 0; i < orderWechatList.size(); i++) {
            WechatOrderVo orderWechatTemp = orderWechatList.get(i);
            if (orderWechatTemp.getWechatmchno().equals(wechatParamForm.getWechatmchno())) {
                orderWechat = orderWechatTemp;
                // 1—支付成功,2-转入退款，3-未支付，4-已关闭，5-已撤销（刷卡支付),6--用户支付中,7-付失败(其他原因，如银行返回失败)
                if (orderWechatTemp.getWechatorderstatus().intValue() == TradeState.SUCCESS.intValue()) {
                    if (i > 0) {
                        payFlag = false; // 不理解 lsc
                    }
                }
                break;
            }
        }

        if (orderWechat == null) {
            throw new FrameworkRuntimeException(ResultCode.WECHATORDERERROR, "未找到下单的订单");
        } else if (orderWechat.getWechatorderstatus() == TradeState.SUCCESS.intValue()) { // 订单支付成功了
            logger.warn("前端查询微信订单前，微信回调请求已经更新支付成功");
        } else if ((orderWechat.getWechatorderstatus() == TradeState.REFUND.intValue())
                || (orderWechat.getWechatorderstatus() == TradeState.CLOSED.intValue())
                || (orderWechat.getWechatorderstatus() == TradeState.REVOKED.intValue())
                || (orderWechat.getWechatorderstatus() == TradeState.PAYERROR.intValue())) {
            logger.warn("订单异常");
            throw new FrameworkRuntimeException(ResultCode.WECHATORDERERROR, "订单异常");
        }
        WechatPublicSettingBo wechatPublicSettingBo = new WechatPublicSettingBo();
        BeanUtils.copyProperties(weixinConfig, wechatPublicSettingBo);

        WeixinOrderQueryVo orderQuery = WechatUtil.orderQuery(wechatParamForm.getWechatmchno(), wechatPublicSettingBo);
        if (orderQuery == null) {
            logger.warn("查询异常");
            throw new FrameworkRuntimeException(ResultCode.WECHATORDERERROR, "查询异常");
        } else if (orderQuery.getTradeState().intValue() != TradeState.SUCCESS.intValue()) {
            logger.warn("订单未支付成功");
            throw new FrameworkRuntimeException(ResultCode.WECHATORDERERROR, "订单未支付成功");
        } else {
            // 同一个账单微信同时下单，微信先支付的是缴费，后面支付转为充值
            if (!payFlag) { // !payFlag 代替 payFlag == false
                orderWechat.setWechatpaytime(new Date());
                orderWechat.setUpdatetime(new Date());
                orderWechat.setWechatorderstatus(TradeState.SUCCESS.intValue());
                orderWechat.setPaytype(WechatOrderType.recharge.intValue());
                orderWechat.setRemark(orderWechat.getRemark() + ";同一个账单微信同时下单，先支付的是缴费，后面支付转为充值");
                WechatOrderBo wechatOrderBo = new WechatOrderBo();
                BeanUtils.copyProperties(orderWechat, wechatOrderBo);

                if (iWechatCommonFactory.updateOrderRecord(wechatOrderBo, ownerid) == 1) {
                    logger.warn("更新充值记录wechatOrder--->成功：" + orderWechat.getId());
                    sendRechargeMsg(orderWechat, ownerid, enterpriseid, openid);
                } else {
                    logger.warn("充值失败");
                    throw new FrameworkRuntimeException(ResultCode.Fail, "充值失败");
                }
            } else {
                // 更新相关缴费记录
                orderWechat.setWechatorderstatus(TradeState.SUCCESS.intValue());
                orderWechat.setWechatpaytime(new Date());
                orderWechat.setUpdatetime(new Date());
                orderWechat.setWechatorderstatus(TradeState.SUCCESS.intValue());
                orderWechat.setWechatretruncode(orderQuery.getReturnCode());
                orderWechat.setWechatresultcode(orderQuery.getResultCode());
                orderWechat.setWechatorderstate(WechatOrderState.state_normal.intValue());

                if (this.updateRelateRecord(orderWechat, wechatParamForm.getTradeno()) == 1) {
                    logger.warn("更新wechatPaySrv.updateRelateRecord成功,发送短信呢和微信");
                    sendOrderMsg(orderWechat.getWechatpaytime(), ownerid, enterpriseid, order, openid);
                } else {
                    logger.warn("更新wechatPaySrv.updateRelateRecord失败");
                }
            }
        }
    }

    // 更新缴费成功相关记录
    public int updateRelateRecord(WechatOrderVo orderWechat, String tradeno) {
        int result = -1;
        OrderPayParamForm orderPayParam = JSONObject.parseObject(orderWechat.getOrderpayparam(),
                OrderPayParamForm.class);
        orderPayParam.setTradeno(tradeno);
        WechatOrderBo wechatOrderBo = new WechatOrderBo();
        BeanUtils.copyProperties(orderWechat, wechatOrderBo);
        result = iWechatPayService.updateOrderRecord(wechatOrderBo);
        if (result == 1) {
            logger.info("缴费成功-->更新wechat_order：wechatmchno{[]}", orderWechat.getWechatmchno());
        }
        OrderVo order = iOrderService.findById(orderWechat.getOrderid());
        CouponVo coupon = null;
        // 业主剩余金额
        Double ownerBalance = orderPayParam.getAlreadypay() - orderPayParam.getBalance();
        if (orderPayParam.getCouponid() != null) {
            CouponBo couponBo = new CouponBo();
            couponBo.setCouponid(orderPayParam.getCouponid());
            coupon = iCouponService.getCoupon(couponBo);
        }
        TradePayVo orderPay = iOrderService.getTradePay(orderPayParam.getTradeno());
        if (orderPay != null) {
            result = 1;
            order.setPaystatus(WaterConstants.ORDER_PAYSTATUS_PAID);
            order.setPaytime(DateUtils.formatDatetime(new Date()));
            iOrderService.updateTradePayStatus(orderPay.getPayno(), "" + WaterConstants.TRADE_PAYSTATUS_SUCCESS,
                    "支付成功");

            order.setPaystatus(WaterConstants.ORDER_PAYSTATUS_PAID);
            order.setPaytime(DateUtils.formatDatetime(new Date()));
            OrderBo orderBo = new OrderBo();
            BeanUtils.copyProperties(order, orderBo);
            iOrderService.updateOrder(orderBo); // 修改账单

            // 验证当前账单下是否有子账单，如果存在则修改成已缴费
            iOrderService.updateSubOrder(order.getTradeno());
            logger.debug("请求内部服务：orderPay成功--> orderPayParam[{}] order[{}] ownerBalance[{}] payType[{}] coupon[{}]",
                    orderPayParam, order, ownerBalance, WaterConstants.ORDER_PAYTYPE_WEIXIN, coupon);
        } else {
            logger.debug("请求内部服务：orderPay失败--> orderPayParam[{}] order[{}] ownerBalance[{}] payType[{}] coupon[{}]",
                    orderPayParam, order, ownerBalance, WaterConstants.ORDER_PAYTYPE_WEIXIN, coupon);
        }
        return result;
    }

    private long getDateDiff(Date beginDate, Date endDate) {
        long beginTime = beginDate.getTime();
        long endTime = endDate.getTime();
        long minute = (long) ((endTime - beginTime) / (1000 * 60));
        return minute;
    }

    // 缴费发送短信和微信信息
    private void sendOrderMsg(Date time, String ownerid, String enterpriseid, OrderVo order, String openid) {
        // 充值成功发送短信和微信消息
        OwnerBo ownerBo = new OwnerBo();
        ownerBo.setOwnerid(ownerid);
        // 调用存在的方法 必填参数 enterpriseName userName tradetime trademoney
        OwnerVo currentOwner = iOwnerService.findByOwnerId(ownerBo);
        EnterpriseVo enterprise = BaseInf.getCompanyInfo(Long.parseLong(currentOwner.getEnterpriseid()));

        Map<String, String> params = new HashMap<>();
        params.put("phone", currentOwner.getUserphone());
        params.put("date", order.getMonth());
        params.put("name", order.getUsername());
        params.put("money", String.valueOf(order.getAmount()));
        iSmsToolService.sendSMS(currentOwner.getEnterpriseid(), SmsEnum.pay_fee.intValue(), params, order.getTradeno());

        if (enterprise != null) {
            SendMsgBo sendMsgBo = new SendMsgBo();
            WechatMessageParamBo wechatMessageParamBo = new WechatMessageParamBo();
            wechatMessageParamBo.setMessageState(SmsEnum.pay_fee.intValue());
            wechatMessageParamBo.setSendType(SmsEnum.pay_fee.intValue());
            wechatMessageParamBo.setOwnerid(String.valueOf(currentOwner.getOwnerid()));
            wechatMessageParamBo.setUserName(currentOwner.getUsername());
            wechatMessageParamBo.setEnterpriseid(String.valueOf(currentOwner.getEnterpriseid()));
            wechatMessageParamBo.setTradeno(order.getTradeno());
            wechatMessageParamBo.setCreatetime(order.getGeneratetime());
            wechatMessageParamBo.setEnterpriseName(enterprise == null ? "" : enterprise.getName());
            wechatMessageParamBo.setTradetime(DateUtils.formatDate(new Date()));
            wechatMessageParamBo.setTrademoney(order.getRealamount());
            sendMsgBo.setWechatMessageParam(wechatMessageParamBo);
            sendMsgBo.setOpenId(openid);
            iSmsToolService.sendWeChatMsg(sendMsgBo);
        } else {
            logger.info("微信推送消息无法找到先关的企业信息");
        }

    }

    private void sendRechargeMsg(WechatOrderVo wechatOrder, String ownerid, String enterpriseid, String openid) {

        OwnerBo ownerBo = new OwnerBo();
        ownerBo.setOwnerid(ownerid);
        OwnerVo currentOwner = iOwnerService.findByOwnerId(ownerBo);

        WechatMessageParamBo wechatMessageParam = new WechatMessageParamBo();
        wechatMessageParam.setMessageState(SmsEnum.recharge.intValue());
        wechatMessageParam.setOwnerid(ownerid);
        wechatMessageParam.setSendType(1);
        wechatMessageParam.setEnterpriseid(enterpriseid);
        wechatMessageParam.setUserName(currentOwner.getUsername());
        wechatMessageParam.setRechargemoney(wechatOrder.getWechatamount());
        wechatMessageParam.setRechargetime(DateUtils.formatDatetime(wechatOrder.getWechatpaytime()));

        Map<String, String> params = new HashMap<>();
        params.put("name", currentOwner.getUsername());
        params.put("phone", currentOwner.getUserphone());
        params.put("money", String.valueOf(wechatOrder.getWechatamount()));
        params.put("time", DateUtils.formatDatetime(wechatOrder.getWechatpaytime()));
        params.put("title", "账户充值");
        iSmsToolService.sendSMS(enterpriseid, SmsEnum.recharge.intValue(), params, null);
        SendMsgBo sendMsgBo = new SendMsgBo();
        sendMsgBo.setWechatMessageParam(wechatMessageParam);
        sendMsgBo.setOpenId(openid);
        iSmsToolService.sendWeChatMsg(sendMsgBo);
    }

    public WechatPublicSettingVo getWechatconfig(HttpServletRequest request, boolean debug) {
        String session = request.getHeader("token");
        logger.debug("*****************************请求的Action名称：" + request.getRequestURI());
        if (debug == true && StringUtils.isBlank(session)) {
            WechatPublicSettingVo weixinConfig = new WechatPublicSettingVo();
            weixinConfig.setAppid("wx39fbb31d6723056e");
            weixinConfig.setAppsecret("a9397026b7f6279415f24907c965035e");
            weixinConfig.setDomain("http://zhtest.eastcomiot.com");
            weixinConfig.setEnterpriseid("46");
            weixinConfig.setGatewayauthorizecode(
                    "https://open.weixin.qq.com/connect/oauth2/authorize?appid={0}&redirect_uri={1}&response_type=code&scope=snsapi_base&state=jiutong#wechat_redirect");
            weixinConfig.setGatewayopenidbycode(
                    "https://api.weixin.qq.com/sns/oauth2/access_token?appid={0}&secret={1}&code={2}&grant_type=authorization_code");
            weixinConfig.setMchid("1509215371");
            weixinConfig.setOrderqueryurl("https://api.mch.weixin.qq.com/pay/orderquery");
            weixinConfig.setPaysecret("AD4654E53FC4E643E4EEE2750FA8554A");
            weixinConfig.setRequestreturnurl("https://api.weixin.qq.com/cgi-bin/user/info");
            weixinConfig.setUnifiedorderurl("https://api.mch.weixin.qq.com/pay/unifiedorder");
            weixinConfig.setWechatpublicid("1");
            logger.error("session 不能为空，测试转为默认weixinConfig");
            return weixinConfig;
        }
        if (debug && avc.get(CacheKey.WaterWechatConfig + session) == null) {
            logger.error("token不存在，测试转为默认weixinConfig");
        }
        WechatPublicSettingVo weixinConfig = avc.get(CacheKey.WaterWechatConfig + session) == null ? null
                : JSONUtils.parseObject(avc.get(CacheKey.WaterWechatConfig + session), WechatPublicSettingVo.class);

        // 测试使用暂时使用
        if (weixinConfig == null) {
            weixinConfig = new WechatPublicSettingVo();
            weixinConfig.setAppid("wx39fbb31d6723056e");
            weixinConfig.setAppsecret("a9397026b7f6279415f24907c965035e");
            weixinConfig.setDomain("http://zhtest.eastcomiot.com");
            weixinConfig.setEnterpriseid("46");
            weixinConfig.setGatewayauthorizecode(
                    "https://open.weixin.qq.com/connect/oauth2/authorize?appid={0}&redirect_uri={1}&response_type=code&scope=snsapi_base&state=jiutong#wechat_redirect");
            weixinConfig.setGatewayopenidbycode(
                    "https://api.weixin.qq.com/sns/oauth2/access_token?appid={0}&secret={1}&code={2}&grant_type=authorization_code");
            weixinConfig.setMchid("1509215371");
            weixinConfig.setOrderqueryurl("https://api.mch.weixin.qq.com/pay/orderquery");
            weixinConfig.setPaysecret("AD4654E53FC4E643E4EEE2750FA8554A");
            weixinConfig.setRequestreturnurl("https://api.weixin.qq.com/cgi-bin/user/info");
            weixinConfig.setUnifiedorderurl("https://api.mch.weixin.qq.com/pay/unifiedorder");
            weixinConfig.setWechatpublicid("1");
        }

        return weixinConfig;
    }

    @Override
    public void orderError(WechatParamForm wechatParamForm) {
        WechatParamBo wechatParamBo = new WechatParamBo();
        BeanUtils.copyProperties(wechatParamForm, wechatParamBo);
        WechatOrderVo orderWechat = iWechatPayService.orderQuery(wechatParamBo);
        if (orderWechat == null) {
            throw new FrameworkRuntimeException(ResultCode.WECHATORDERERROR, "未找到下单的订单");
        } else {
            orderWechat.setUpdatetime(new Date());
            orderWechat.setWechatorderstatus(TradeState.PAYERROR.intValue());
            orderWechat.setWechaterrormsg(wechatParamForm.getErrormsg());
            WechatOrderBo wechatOrderBo = new WechatOrderBo();
            BeanUtils.copyProperties(orderWechat, wechatOrderBo);
            iWechatPayService.updateOrderStatus(wechatOrderBo);
        }
    }
}
