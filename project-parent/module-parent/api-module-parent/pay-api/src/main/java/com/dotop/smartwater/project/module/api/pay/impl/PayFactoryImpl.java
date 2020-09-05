package com.dotop.smartwater.project.module.api.pay.impl;

import com.dotop.smartwater.dependence.core.utils.BeanUtils;
import com.dotop.smartwater.dependence.core.utils.DateUtils;
import com.dotop.smartwater.dependence.core.utils.JSONUtils;
import com.dotop.smartwater.project.module.api.pay.IPayFactory;
import com.dotop.smartwater.project.module.api.pay.mode.WeChatCom;
import com.dotop.smartwater.project.module.api.pay.mode.WeChatOfficialAccountsPay;
import com.dotop.smartwater.project.module.api.pay.mode.WeChatPayScan;
import com.dotop.smartwater.project.module.api.pay.IAsyncFactory;
import com.dotop.smartwater.project.module.core.pay.bo.PaymentOrderBo;
import com.dotop.smartwater.project.module.core.pay.bo.PaymentResultBo;
import com.dotop.smartwater.project.module.core.pay.constants.PayConstants;
import com.dotop.smartwater.project.module.core.pay.form.PaymentOrderForm;
import com.dotop.smartwater.project.module.core.pay.vo.PayResultVo;
import com.dotop.smartwater.project.module.core.pay.vo.PaymentOrderVo;
import com.dotop.smartwater.project.module.core.pay.vo.PaymentResultVo;
import com.dotop.smartwater.project.module.core.pay.vo.PushVo;
import com.dotop.smartwater.project.module.core.pay.wechat.MessageManager;
import com.dotop.smartwater.project.module.core.pay.wechat.WXPayUtil;
import com.dotop.smartwater.project.module.core.water.vo.customize.WechatPublicSettingVo;
import com.dotop.smartwater.project.module.service.pay.IPaymentOrderService;
import com.dotop.smartwater.project.module.service.pay.IPaymentResultService;
import com.dotop.smartwater.project.module.service.tool.IWechatPublicSettingService;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;


/**
 * 支付平台接口
 *

 * @date 2019年2月23日
 */

@Component
public class PayFactoryImpl implements IPayFactory {

    private static final Logger LOGGER = LogManager.getLogger(PayFactoryImpl.class);

    @Autowired
    private IPaymentOrderService iPaymentOrderService;

    @Autowired
    private IPaymentResultService iPaymentResultService;

    @Autowired
    private IAsyncFactory iAsyncFactory;

    @Autowired
    private WeChatOfficialAccountsPay weChatOfficialAccountsPay;

    @Autowired
    private WeChatPayScan weChatPayScan;

    @Autowired
    private IWechatPublicSettingService iWechatPublicSettingService;

    @Override
    public PayResultVo submit(PaymentOrderForm paymentOrderForm, String realIP) {
        PaymentOrderBo paymentOrderBo = BeanUtils.copy(paymentOrderForm, PaymentOrderBo.class);

        PayResultVo data = new PayResultVo();
        if (StringUtils.isNotBlank(paymentOrderBo.getExtra()) && paymentOrderBo.getExtra().length() > 512) {
            data.setReturn_code(PayConstants.WECHAT_ORDER_STATUS_FAIL);
            data.setReturn_msg("回调额外参数(extra)过长");
            return data;
        }

        PaymentOrderVo pOrderVo = iPaymentOrderService.findByTradeNumberAndEid(paymentOrderForm.getTradeNumber(),
                paymentOrderForm.getEnterpriseid());

        PaymentOrderVo paymentOrderVo;
        paymentOrderBo.setStatus(PayConstants.TRADE_PAYSTATUS_IN);

        if (pOrderVo != null) {
            data.setReturn_code(PayConstants.WECHAT_ORDER_EXIST);
            data.setReturn_msg(PayConstants.WECHAT_ORDER_EXIST_MSG);
            return data;
        } else {
            paymentOrderVo = iPaymentOrderService.add(paymentOrderBo);
        }

        WechatPublicSettingVo weixinConfig;
        paymentOrderBo.setPayid(paymentOrderVo.getPayid());
        switch (paymentOrderForm.getMode()) {
            // 现金(异步)
            case PayConstants.PAYTYPE_MONEY:
                iAsyncFactory.payByMoney(paymentOrderBo);
                data.setReturn_code(PayConstants.WECHAT_ORDER_STATUS_SUCCESS);
                data.setReturn_msg(PayConstants.WECHAT_ORDER_STATUS_SUCCESS_MSG);
                break;
            // 微信手机支付,生成预下单(同步)
            case PayConstants.PAYTYPE_WEIXIN:
                weixinConfig = iWechatPublicSettingService
                        .getByenterpriseId(paymentOrderForm.getEnterpriseid());
                if (weixinConfig == null) {
                    LOGGER.info("找不到企业信息：" + paymentOrderForm.getEnterpriseid());
                    data.setReturn_code(PayConstants.WECHAT_ORDER_STATUS_FAIL);
                    data.setReturn_msg("找不到企业绑定的微信信息");
                } else {
                    data = weChatOfficialAccountsPay.unifiedOrder(paymentOrderBo, realIP);
                }
                break;
            // 微信刷卡支付(异步)
            case PayConstants.PAYTYPE_PAYCARD:
                weixinConfig = iWechatPublicSettingService
                        .getByenterpriseId(paymentOrderForm.getEnterpriseid());
                if (weixinConfig == null) {
                    LOGGER.info("找不到企业信息：" + paymentOrderForm.getEnterpriseid());
                    data.setReturn_code(PayConstants.WECHAT_ORDER_STATUS_FAIL);
                    data.setReturn_msg("找不到企业绑定的微信信息");
                } else {
                    iAsyncFactory.payByCard(paymentOrderBo, realIP);
                    data.setReturn_code(PayConstants.WECHAT_ORDER_STATUS_SUCCESS);
                    data.setReturn_msg(PayConstants.WECHAT_ORDER_STATUS_SUCCESS_MSG);
                }
                break;
            // 微信生成二维码支付(同步)
            case PayConstants.PAYTYPE_QRCODE:
                weixinConfig = iWechatPublicSettingService
                        .getByenterpriseId(paymentOrderForm.getEnterpriseid());
                if (weixinConfig == null) {
                    LOGGER.info("找不到企业信息：" + paymentOrderForm.getEnterpriseid());
                    data.setReturn_code(PayConstants.WECHAT_ORDER_STATUS_FAIL);
                    data.setReturn_msg("找不到企业绑定的微信信息");
                } else {
                    data = weChatPayScan.generateQRcode(paymentOrderBo, realIP);
                }
                break;
            default:
                LOGGER.error("不明确的支付方式", JSONUtils.toJSONString(paymentOrderForm));
                data.setReturn_code(PayConstants.WECHAT_ORDER_STATUS_FAIL);
                data.setReturn_msg("不明确的支付方式");
        }
        return data;
    }

    @Override
    public void handleCallBack(Map<String, String> resultMap) throws Exception {
        String result = JSONUtils.toJSONString(resultMap);
        String attach = resultMap.get("attach");
        if (attach == null || attach.length() == 0) {
            LOGGER.info("微信回调数据有误：" + result);
            return;
        }

        String[] extra = attach.split("\\|");
        if (extra.length != 2) {
            LOGGER.info("微信回调数据[attach]有误：" + attach);
            return;
        }

        String enterpriseId = extra[0];
        String resId = extra[1];

        WechatPublicSettingVo weixinConfig = iWechatPublicSettingService
                .getByenterpriseId(enterpriseId);
        if (weixinConfig == null) {
            LOGGER.info("找不到企业信息：" + enterpriseId);
            return;
        }

        String outTradeNo = resultMap.get("out_trade_no");

        //验证签名必须做
        if (!WXPayUtil.isSignatureValid(resultMap, weixinConfig.getPaysecret())) {
            LOGGER.info(outTradeNo + " 签名不正确 ：" + result);
            return;
        }

        PaymentResultBo paymentResultBo = new PaymentResultBo();
        paymentResultBo.setPayResId(resId);
        PaymentResultVo paymentResultVo = iPaymentResultService.get(paymentResultBo);
        if (paymentResultVo == null) {
            LOGGER.info("resId 没有找到该订单 ：" + resId);
            return;
        }

        //校验金额
        String total_fee = resultMap.get("total_fee");

        BigDecimal money = new BigDecimal(paymentResultVo.getAmount());
        BigDecimal times = new BigDecimal("100");
        BigDecimal amount = money.multiply(times);

        if (!String.valueOf(amount.intValue()).equals(total_fee)) {
            LOGGER.info(outTradeNo + " 该金额和账单金额不匹配 ：回调金额" + total_fee + " 账单金额: " + amount);
            return;
        }

        if (MessageManager.SUCCESS.equals(resultMap.get(MessageManager.RESP_RETURN_CODE))) {
            if (MessageManager.SUCCESS.equals(resultMap.get(MessageManager.RESP_RESULT_CODE))) {

                String payTime = resultMap.get("time_end");
                DateTime date = DateTimeFormat.forPattern("yyyyMMddHHmmss").parseDateTime(payTime);

                paymentResultBo = new PaymentResultBo();
                paymentResultBo.setStatus(PayConstants.TRADE_PAYSTATUS_SUCCESS);
                paymentResultBo.setDescription(PayConstants.TRADE_PAYSTATUS_SUCCESS_MSG);
                paymentResultBo.setPayTime(date.toDate());
                paymentResultBo.setPayResId(resId);
                iPaymentResultService.edit(paymentResultBo);

                PaymentOrderBo paymentOrderBo = new PaymentOrderBo();
                paymentOrderBo.setPayTime(date.toDate());
                paymentOrderBo.setStatus(PayConstants.TRADE_PAYSTATUS_SUCCESS);
                paymentOrderBo.setPayid(paymentResultVo.getPayid());
                iPaymentOrderService.edit(paymentOrderBo);

            } else {
                /** 微信返回错误的结果信息*/
                String err_code = resultMap.get(MessageManager.RESP_ERR_CODE);
                String err_code_des = resultMap.get(MessageManager.RESP_ERR_CODE);
                LOGGER.error("[err_code] = {},err_code_des : {}", err_code, err_code_des);

                paymentResultBo = new PaymentResultBo();
                paymentResultBo.setStatus(PayConstants.TRADE_PAYSTATUS_ERROR);
                paymentResultBo.setDescription(err_code_des);
                paymentResultBo.setPayResId(resId);
                iPaymentResultService.edit(paymentResultBo);

                PaymentOrderBo paymentOrderBo = new PaymentOrderBo();
                paymentOrderBo.setStatus(PayConstants.TRADE_PAYSTATUS_ERROR);
                paymentOrderBo.setPayid(paymentResultVo.getPayid());
                iPaymentOrderService.edit(paymentOrderBo);
            }
        } else {
            LOGGER.error("微信回调内部错误返回的resultMap {}", JSONUtils.toJSONString(resultMap));
            return;
        }

        paymentResultBo.setPayResId(resId);
        iAsyncFactory.handleCallBack(paymentResultBo, resultMap);
    }

    @Override
    public PayResultVo orderquery(PaymentOrderForm paymentOrderForm) throws Exception {
        PaymentOrderBo paymentOrderBo = BeanUtils.copy(paymentOrderForm, PaymentOrderBo.class);

        PayResultVo data = new PayResultVo();
        PaymentOrderVo pOrderVo = iPaymentOrderService.findByTradeNumberAndEid(paymentOrderForm.getTradeNumber(),
                paymentOrderForm.getEnterpriseid());

        if (pOrderVo == null) {
            data.setReturn_code(PayConstants.WECHAT_ORDER_NOT_EXIST);
            data.setReturn_msg(PayConstants.WECHAT_ORDER_NOT_EXIST_MSG);
            return data;
        }

        PaymentResultVo paymentResultVo = iPaymentResultService.findByPayId(pOrderVo.getPayid());
        if (!PayConstants.TRADE_PAYSTATUS_IN.equals(pOrderVo.getStatus())) {
            data.setReturn_code(paymentResultVo.getStatus());
            data.setReturn_msg(paymentResultVo.getDescription());
            if (PayConstants.TRADE_PAYSTATUS_SUCCESS.equals(pOrderVo.getStatus())) {
                PushVo pushVo = new PushVo();
                pushVo.setDescription(paymentResultVo.getDescription());
                pushVo.setStatus(paymentResultVo.getStatus());
                pushVo.setEnterpriseid(pOrderVo.getEnterpriseid());
                pushVo.setExtra(pOrderVo.getExtra());
                pushVo.setAmount(pOrderVo.getAmount());
                pushVo.setMode(pOrderVo.getMode());
                pushVo.setTradeName(pOrderVo.getTradeName());
                pushVo.setTradeNumber(pOrderVo.getTradeNumber());
                pushVo.setPayNumber(paymentResultVo.getPayNumber());
                pushVo.setPayTime(DateUtils.formatDatetime(paymentResultVo.getPayTime()));
                data.setPushVo(pushVo);
            }
            return data;
        }

        switch (pOrderVo.getMode()) {
            case PayConstants.PAYTYPE_WEIXIN:
            case PayConstants.PAYTYPE_PAYCARD:
            case PayConstants.PAYTYPE_QRCODE:
                //查询微信
                WechatPublicSettingVo weixinConfig = iWechatPublicSettingService
                        .getByenterpriseId(paymentOrderBo.getEnterpriseid());
                if (weixinConfig == null) {
                    LOGGER.info("找不到企业信息：" + paymentOrderForm.getEnterpriseid());
                    data.setReturn_code(PayConstants.TRADE_PAYSTATUS_ERROR);
                    data.setReturn_msg("找不到企业绑定的微信信息");
                    return data;
                }

                Map<String, String> orderMap = new HashMap<>(10);
                String nonceStr = WXPayUtil.generateNonceStr();
                orderMap.put("appid", weixinConfig.getAppid());
                orderMap.put("mch_id", weixinConfig.getMchid());
                orderMap.put("nonce_str", nonceStr);
                orderMap.put("out_trade_no", paymentResultVo.getPayNumber());

                String sign = WXPayUtil.generateSignature(orderMap, weixinConfig.getPaysecret());
                orderMap.put("sign", sign);

                Map<String, String> lastResult = WeChatCom.queryOrderOnce(weixinConfig, orderMap);

                String returnCode = lastResult.get(MessageManager.RESP_RETURN_CODE);
                if (returnCode.equals(MessageManager.SUCCESS)) {
                    String resultCode = lastResult.get(MessageManager.RESP_RESULT_CODE);
                    String tradeState = lastResult.get(MessageManager.RESP_TRADE_STATE);
                    if (resultCode.equals(MessageManager.SUCCESS) && tradeState.equals(MessageManager.SUCCESS)) {
                        String attach = lastResult.get("attach");
                        if (attach == null || attach.length() == 0) {
                            LOGGER.info("数据有误：attach 为空");
                            return null;
                        }

                        String[] extra = attach.split("\\|");
                        if (extra.length != 2) {
                            LOGGER.info("微信回调数据[attach]有误：" + attach);
                            return null;
                        }
                        String resId = extra[1];

                        //校验金额
                        String total_fee = lastResult.get("total_fee");

                        BigDecimal money = new BigDecimal(paymentResultVo.getAmount());
                        BigDecimal times = new BigDecimal("100");
                        BigDecimal amount = money.multiply(times);

                        if (!String.valueOf(amount.intValue()).equals(total_fee)) {
                            LOGGER.info(paymentResultVo.getPayNumber() + " 该金额和账单金额不匹配 ：回调金额" + total_fee + " 账单金额: " + amount);
                            return null;
                        }

                        String payTime = lastResult.get("time_end");
                        DateTime date = DateTimeFormat.forPattern("yyyyMMddHHmmss").parseDateTime(payTime);

                        PaymentResultBo paymentResultBo = new PaymentResultBo();
                        paymentResultBo.setStatus(PayConstants.TRADE_PAYSTATUS_SUCCESS);
                        paymentResultBo.setDescription(PayConstants.TRADE_PAYSTATUS_SUCCESS_MSG);
                        paymentResultBo.setPayTime(date.toDate());
                        paymentResultBo.setPayResId(resId);
                        iPaymentResultService.edit(paymentResultBo);

                        paymentOrderBo = new PaymentOrderBo();
                        paymentOrderBo.setPayTime(date.toDate());
                        paymentOrderBo.setStatus(PayConstants.TRADE_PAYSTATUS_SUCCESS);
                        paymentOrderBo.setPayid(paymentResultVo.getPayid());
                        iPaymentOrderService.edit(paymentOrderBo);

                        PushVo pushVo = new PushVo();
                        pushVo.setDescription(paymentResultBo.getDescription());
                        pushVo.setStatus(paymentResultBo.getStatus());
                        pushVo.setEnterpriseid(pOrderVo.getEnterpriseid());
                        pushVo.setExtra(pOrderVo.getExtra());
                        pushVo.setAmount(pOrderVo.getAmount());
                        pushVo.setMode(pOrderVo.getMode());
                        pushVo.setTradeName(pOrderVo.getTradeName());
                        pushVo.setTradeNumber(pOrderVo.getTradeNumber());
                        pushVo.setPayNumber(paymentResultVo.getPayNumber());
                        pushVo.setPayTime(DateUtils.formatDatetime(paymentResultBo.getPayTime()));

                        data.setReturn_msg(PayConstants.TRADE_PAYSTATUS_SUCCESS_MSG);
                        data.setReturn_code(PayConstants.TRADE_PAYSTATUS_SUCCESS);
                        data.setPushVo(pushVo);

                    } else {
                        String trade_state = lastResult.get(MessageManager.RESP_TRADE_STATE);
                        String trade_state_desc = lastResult.get(MessageManager.RESP_TRADE_DESC);
                        if (MessageManager.RESP_TRADE_STATE_NOTPAY.equals(trade_state)
                                || MessageManager.RESP_TRADE_STATE_USERPAYING.equals(trade_state)) {
                            //支付中
                            data.setReturn_msg(PayConstants.TRADE_PAYSTATUS_IN_MSG);
                            data.setReturn_code(PayConstants.TRADE_PAYSTATUS_IN);
                        } else {
                            paymentOrderBo.setStatus(PayConstants.TRADE_PAYSTATUS_ERROR);
                            paymentOrderBo.setPayid(paymentResultVo.getPayid());
                            iPaymentOrderService.edit(paymentOrderBo);

                            PaymentResultBo paymentResultBo = new PaymentResultBo();
                            paymentResultBo.setStatus(PayConstants.TRADE_PAYSTATUS_ERROR);
                            paymentResultBo.setDescription(trade_state_desc);
                            paymentResultBo.setPayResId(paymentResultVo.getPayResId());
                            iPaymentResultService.edit(paymentResultBo);

                            data.setReturn_code(PayConstants.TRADE_PAYSTATUS_ERROR);
                            data.setReturn_msg(trade_state_desc);
                        }
                    }
                } else {
                    paymentOrderBo.setStatus(PayConstants.TRADE_PAYSTATUS_ERROR);
                    paymentOrderBo.setPayid(paymentResultVo.getPayid());
                    iPaymentOrderService.edit(paymentOrderBo);

                    PaymentResultBo paymentResultBo = new PaymentResultBo();
                    paymentResultBo.setStatus(PayConstants.TRADE_PAYSTATUS_ERROR);
                    paymentResultBo.setDescription(lastResult.get(MessageManager.RESP_ERR_CODE_DES));
                    paymentResultBo.setPayResId(paymentResultVo.getPayResId());
                    iPaymentResultService.edit(paymentResultBo);

                    data.setReturn_code(PayConstants.TRADE_PAYSTATUS_ERROR);
                    data.setReturn_msg(lastResult.get(MessageManager.RESP_ERR_CODE_DES));
                }
                break;
            //TODO 支付宝没接入
            case PayConstants.PAYTYPE_ALIPAY:
                break;
            default:
                break;
        }
        return data;
    }

    @Override
    public PayResultVo ordercancel(PaymentOrderForm paymentOrderForm) throws Exception {
        PaymentOrderBo paymentOrderBo = BeanUtils.copy(paymentOrderForm, PaymentOrderBo.class);

        PayResultVo data = new PayResultVo();
        PaymentOrderVo pOrderVo = iPaymentOrderService.findByTradeNumberAndEid(paymentOrderForm.getTradeNumber(),
                paymentOrderForm.getEnterpriseid());

        if (pOrderVo == null) {
            data.setReturn_code(PayConstants.WECHAT_ORDER_NOT_EXIST);
            data.setReturn_msg(PayConstants.WECHAT_ORDER_NOT_EXIST_MSG);
            return data;
        }
        if (!PayConstants.TRADE_PAYSTATUS_IN.equals(pOrderVo.getStatus())) {
            data.setReturn_code(PayConstants.WECHAT_ORDER_COMPLETE);
            data.setReturn_msg(PayConstants.WECHAT_ORDER_COMPLETE_MSG);
            return data;
        }

        PaymentResultVo paymentResultVo = iPaymentResultService.findByPayId(pOrderVo.getPayid());

        switch (pOrderVo.getMode()) {
            case PayConstants.PAYTYPE_WEIXIN:
            case PayConstants.PAYTYPE_PAYCARD:
            case PayConstants.PAYTYPE_QRCODE:
                WechatPublicSettingVo weixinConfig = iWechatPublicSettingService
                        .getByenterpriseId(paymentOrderBo.getEnterpriseid());
                if (weixinConfig == null) {
                    LOGGER.info("找不到企业信息：" + paymentOrderForm.getEnterpriseid());
                    data.setReturn_code(PayConstants.WECHAT_ORDER_CANCEL_FAIL);
                    data.setReturn_msg("找不到企业绑定的微信信息");
                    return data;
                }

                Map<String, String> orderMap = new HashMap<>(10);
                String nonceStr = WXPayUtil.generateNonceStr();
                orderMap.put("appid", weixinConfig.getAppid());
                orderMap.put("mch_id", weixinConfig.getMchid());
                orderMap.put("nonce_str", nonceStr);
                orderMap.put("out_trade_no", paymentResultVo.getPayNumber());

                String sign = WXPayUtil.generateSignature(orderMap, weixinConfig.getPaysecret());
                orderMap.put("sign", sign);

                Map<String, String> lastResult = WeChatCom.cancelOrder(orderMap);
                String returnCode = lastResult.get(MessageManager.RESP_RETURN_CODE);
                if (returnCode.equals(MessageManager.SUCCESS)) {
                    String resultCode = lastResult.get(MessageManager.RESP_RESULT_CODE);
                    if (resultCode.equals(MessageManager.SUCCESS)) {

                        PaymentResultBo paymentResultBo = new PaymentResultBo();
                        paymentResultBo.setStatus(PayConstants.TRADE_PAYSTATUS_ERROR);
                        paymentResultBo.setDescription(PayConstants.TRADE_PAYSTATUS_ORDERCANCEL);
                        paymentResultBo.setPayResId(paymentResultVo.getPayResId());
                        iPaymentResultService.edit(paymentResultBo);

                        paymentOrderBo = new PaymentOrderBo();
                        paymentOrderBo.setStatus(PayConstants.TRADE_PAYSTATUS_ERROR);
                        paymentOrderBo.setPayid(paymentResultVo.getPayid());
                        iPaymentOrderService.edit(paymentOrderBo);

                        data.setReturn_msg(PayConstants.WECHAT_ORDER_CANCEL_SUCCESS_MSG);
                        data.setReturn_code(PayConstants.WECHAT_ORDER_CANCEL_SUCCESS);

                    } else {
                        String err_code_des = lastResult.get(MessageManager.RESP_ERR_CODE_DES);
                        data.setReturn_code(PayConstants.WECHAT_ORDER_CANCEL_FAIL);
                        data.setReturn_msg(err_code_des);
                    }
                } else {
                    data.setReturn_code(PayConstants.WECHAT_ORDER_CANCEL_FAIL);
                    data.setReturn_msg(lastResult.get(MessageManager.RESP_RETURN_MSG));
                }
                break;
            //TODO 支付宝没接入
            case PayConstants.PAYTYPE_ALIPAY:
                break;
            default:
                break;
        }
        return data;
    }
}
