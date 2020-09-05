package com.dotop.smartwater.project.module.api.pay.impl;

import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.utils.DateUtils;
import com.dotop.smartwater.dependence.core.utils.JSONUtils;
import com.dotop.smartwater.project.module.api.pay.mode.WeChatCom;
import com.dotop.smartwater.project.module.api.pay.IPushFactory;
import com.dotop.smartwater.project.module.core.pay.bo.PaymentOrderBo;
import com.dotop.smartwater.project.module.core.pay.bo.PaymentPushRecordBo;
import com.dotop.smartwater.project.module.core.pay.bo.PaymentResultBo;
import com.dotop.smartwater.project.module.core.pay.constants.PayConstants;
import com.dotop.smartwater.project.module.core.pay.utils.PushUtil;
import com.dotop.smartwater.project.module.core.pay.vo.*;
import com.dotop.smartwater.project.module.core.pay.wechat.MessageManager;
import com.dotop.smartwater.project.module.core.pay.wechat.WXPayUtil;
import com.dotop.smartwater.project.module.core.water.vo.customize.WechatPublicSettingVo;
import com.dotop.smartwater.project.module.service.pay.IPaymentOrderService;
import com.dotop.smartwater.project.module.service.pay.IPaymentPushRecordService;
import com.dotop.smartwater.project.module.service.pay.IPaymentResultService;
import com.dotop.smartwater.project.module.service.tool.IWechatPublicSettingService;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.Minutes;
import org.joda.time.format.DateTimeFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 支付平台接口
 *

 * @date 2019年2月23日
 */

@Component
public class PushFactoryImpl implements IPushFactory {

    private static final Logger LOGGER = LogManager.getLogger(PushFactoryImpl.class);
    @Autowired
    private IPaymentPushRecordService iPaymentPushRecordService;

    @Autowired
    private IWechatPublicSettingService iWechatPublicSettingService;

    @Autowired
    private IPaymentResultService iPaymentResultService;

    @Autowired
    private IPaymentOrderService iPaymentOrderService;

    @Override
    public void push() throws FrameworkRuntimeException {
        //12小时后不再推送
        List<PaymentPushRecordVo> list = iPaymentPushRecordService.getFailList(PayConstants.PUSH_STATUS_FAIL, 720);

        if (list.size() > 0) {
            for (PaymentPushRecordVo vo : list) {
                String result = PushUtil.pushDataToOtherServer(vo.getPushUrl(),
                        vo.getPushData());

                PaymentPushRecordBo paymentPushRecordBo = new PaymentPushRecordBo();
                paymentPushRecordBo.setTimes(vo.getTimes() + 1);
                paymentPushRecordBo.setPushTime(new Date());
                paymentPushRecordBo.setPushId(vo.getPushId());

                if (PayConstants.RESULT_RESPONSE_SUCCESS_CODE.equals(result)) {
                    paymentPushRecordBo.setStatus(PayConstants.PUSH_STATUS_SUCCESS);
                } else {
                    paymentPushRecordBo.setStatus(PayConstants.PUSH_STATUS_FAIL);
                }
                iPaymentPushRecordService.edit(paymentPushRecordBo);
            }
        }

    }

    @Override
    public void getNotify() throws Exception {
        //查询订单提交后5分钟还在支付状态中的订单
        List<PaymentOrderVo> list = iPaymentOrderService.getPayInList(PayConstants.TRADE_PAYSTATUS_IN, 5);
        if (list.size() > 0) {
            for(PaymentOrderVo vo : list){
                PayResultVo data = new PayResultVo();
                PaymentResultVo paymentResultVo = iPaymentResultService.findByPayId(vo.getPayid());
                switch (vo.getMode()) {
                    case PayConstants.PAYTYPE_WEIXIN:
                    case PayConstants.PAYTYPE_PAYCARD:
                    case PayConstants.PAYTYPE_QRCODE:
                        //查询微信
                        WechatPublicSettingVo weiXinConfig = iWechatPublicSettingService
                                .getByenterpriseId(vo.getEnterpriseid());
                        if(weiXinConfig == null){
                            continue;
                        }

                        Map<String, String> orderMap = new HashMap<>(10);
                        String nonceStr = WXPayUtil.generateNonceStr();
                        orderMap.put("appid", weiXinConfig.getAppid());
                        orderMap.put("mch_id", weiXinConfig.getMchid());
                        orderMap.put("nonce_str", nonceStr);
                        orderMap.put("out_trade_no", paymentResultVo.getPayNumber());

                        String sign = WXPayUtil.generateSignature(orderMap, weiXinConfig.getPaysecret());
                        orderMap.put("sign", sign);

                        Map<String, String> lastResult = WeChatCom.queryOrderOnce(weiXinConfig, orderMap);

                        String returnCode = lastResult.get(MessageManager.RESP_RETURN_CODE);
                        if (returnCode.equals(MessageManager.SUCCESS)) {
                            String resultCode = lastResult.get(MessageManager.RESP_RESULT_CODE);
                            String tradeState = lastResult.get(MessageManager.RESP_TRADE_STATE);
                            if (resultCode.equals(MessageManager.SUCCESS) && tradeState.equals(MessageManager.SUCCESS)) {
                                String attach = lastResult.get("attach");
                                if (attach == null || attach.length() == 0) {
                                    LOGGER.info("数据有误：attach 为空");
                                    continue;
                                }

                                String[] extra = attach.split("\\|");
                                if (extra.length != 2) {
                                    LOGGER.info("微信回调数据[attach]有误：" + attach);
                                    continue;
                                }
                                String resId = extra[1];

                                //校验金额
                                String total_fee = lastResult.get("total_fee");

                                BigDecimal money = new BigDecimal(paymentResultVo.getAmount());
                                BigDecimal times = new BigDecimal("100");
                                BigDecimal amount = money.multiply(times);

                                if (!String.valueOf(amount.intValue()).equals(total_fee)) {
                                    LOGGER.info(paymentResultVo.getPayNumber() + " 该金额和账单金额不匹配 ：回调金额" + total_fee + " 账单金额: " + amount);
                                    continue;
                                }

                                String payTime = lastResult.get("time_end");
                                DateTime date = DateTimeFormat.forPattern("yyyyMMddHHmmss").parseDateTime(payTime);

                                PaymentResultBo paymentResultBo = new PaymentResultBo();
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

                                PushVo pushVo = new PushVo();
                                pushVo.setDescription(paymentResultBo.getDescription());
                                pushVo.setStatus(paymentResultBo.getStatus());
                                pushVo.setEnterpriseid(vo.getEnterpriseid());
                                pushVo.setExtra(vo.getExtra());
                                pushVo.setAmount(vo.getAmount());
                                pushVo.setMode(vo.getMode());
                                pushVo.setTradeName(vo.getTradeName());
                                pushVo.setTradeNumber(vo.getTradeNumber());
                                pushVo.setPayNumber(paymentResultVo.getPayNumber());
                                pushVo.setPayTime(DateUtils.formatDatetime(paymentResultBo.getPayTime()));

                                data.setReturn_msg(PayConstants.TRADE_PAYSTATUS_SUCCESS_MSG);
                                data.setReturn_code(PayConstants.TRADE_PAYSTATUS_SUCCESS);
                                data.setPushVo(pushVo);

                            } else {
                                //支付中的状态,订单生成时间相隔大于20分钟，主动关闭订单
                                int minutes = Minutes.minutesBetween(new DateTime(vo.getCreateDate()), new DateTime()).getMinutes();
                                if(minutes > 20){
                                    PaymentOrderBo paymentOrderBo = new PaymentOrderBo();
                                    paymentOrderBo.setStatus(PayConstants.TRADE_PAYSTATUS_ERROR);
                                    paymentOrderBo.setPayid(vo.getPayid());
                                    iPaymentOrderService.edit(paymentOrderBo);

                                    PaymentResultBo paymentResultBo = new PaymentResultBo();
                                    paymentResultBo.setStatus(PayConstants.TRADE_PAYSTATUS_ERROR);
                                    paymentResultBo.setDescription(PayConstants.TRADE_PAYSTATUS_TIMEOUT);
                                    paymentResultBo.setPayResId(paymentResultVo.getPayResId());
                                    iPaymentResultService.edit(paymentResultBo);

                                    PushVo pushVo = new PushVo();
                                    pushVo.setDescription(paymentResultBo.getDescription());
                                    pushVo.setStatus(paymentResultBo.getStatus());
                                    pushVo.setEnterpriseid(vo.getEnterpriseid());
                                    pushVo.setExtra(vo.getExtra());
                                    pushVo.setAmount(vo.getAmount());
                                    pushVo.setMode(vo.getMode());
                                    pushVo.setTradeName(vo.getTradeName());
                                    pushVo.setTradeNumber(vo.getTradeNumber());
                                    pushVo.setPayNumber(paymentResultVo.getPayNumber());

                                    data.setReturn_code(PayConstants.TRADE_PAYSTATUS_ERROR);
                                    data.setReturn_msg(lastResult.get(MessageManager.RESP_ERR_CODE_DES));
                                    data.setPushVo(pushVo);

                                    //关闭订单
                                    Map<String, String> reqMap = new HashMap<>(10);
                                    reqMap.put("appid", weiXinConfig.getAppid());
                                    reqMap.put("mch_id", weiXinConfig.getMchid());
                                    reqMap.put("nonce_str", WXPayUtil.generateNonceStr());
                                    reqMap.put("out_trade_no", paymentResultVo.getPayNumber());
                                    orderMap.put("sign", WXPayUtil.generateSignature(orderMap, weiXinConfig.getPaysecret()));
                                    WeChatCom.cancelOrder(orderMap);

                                }else{
                                    //支付中
                                    data.setReturn_msg(PayConstants.TRADE_PAYSTATUS_IN_MSG);
                                    data.setReturn_code(PayConstants.TRADE_PAYSTATUS_IN);
                                }
                            }
                        } else {
                            PaymentOrderBo paymentOrderBo = new PaymentOrderBo();
                            paymentOrderBo.setPayTime(new Date());
                            paymentOrderBo.setStatus(PayConstants.TRADE_PAYSTATUS_ERROR);
                            paymentOrderBo.setPayid(vo.getPayid());
                            iPaymentOrderService.edit(paymentOrderBo);

                            PaymentResultBo paymentResultBo = new PaymentResultBo();
                            paymentResultBo.setStatus(PayConstants.TRADE_PAYSTATUS_ERROR);
                            paymentResultBo.setDescription(lastResult.get(MessageManager.RESP_ERR_CODE_DES));
                            paymentResultBo.setPayResId(paymentResultVo.getPayResId());
                            iPaymentResultService.edit(paymentResultBo);

                            PushVo pushVo = new PushVo();
                            pushVo.setDescription(paymentResultBo.getDescription());
                            pushVo.setStatus(paymentResultBo.getStatus());
                            pushVo.setEnterpriseid(vo.getEnterpriseid());
                            pushVo.setExtra(vo.getExtra());
                            pushVo.setAmount(vo.getAmount());
                            pushVo.setMode(vo.getMode());
                            pushVo.setTradeName(vo.getTradeName());
                            pushVo.setTradeNumber(vo.getTradeNumber());
                            pushVo.setPayNumber(paymentResultVo.getPayNumber());

                            data.setReturn_code(PayConstants.TRADE_PAYSTATUS_ERROR);
                            data.setReturn_msg(lastResult.get(MessageManager.RESP_ERR_CODE_DES));
                            data.setPushVo(pushVo);
                        }
                        break;
                    //TODO 支付宝没接入
                    case PayConstants.PAYTYPE_ALIPAY:
                        break;
                    default:
                        break;
                }
                if(!PayConstants.TRADE_PAYSTATUS_IN.equals(data.getReturn_code())){
                    //推送
                    if (StringUtils.isNotBlank(vo.getCallbackUrl()) && data.getPushVo() != null) {
                        String result = PushUtil.pushDataToOtherServer(vo.getCallbackUrl(),
                                JSONUtils.toJSONString(data.getPushVo()));

                        PaymentPushRecordBo paymentPushRecordBo = new PaymentPushRecordBo();
                        paymentPushRecordBo.setTimes(1);
                        paymentPushRecordBo.setEnterpriseid(vo.getEnterpriseid());
                        paymentPushRecordBo.setPayid(vo.getPayid());
                        paymentPushRecordBo.setPushData(JSONUtils.toJSONString(data.getPushVo()));
                        paymentPushRecordBo.setPushTime(new Date());
                        paymentPushRecordBo.setPushUrl(vo.getCallbackUrl());

                        if (PayConstants.RESULT_RESPONSE_SUCCESS_CODE.equals(result)) {
                            paymentPushRecordBo.setStatus(PayConstants.PUSH_STATUS_SUCCESS);
                        } else {
                            paymentPushRecordBo.setStatus(PayConstants.PUSH_STATUS_FAIL);
                        }
                        iPaymentPushRecordService.add(paymentPushRecordBo);
                    }
                }
            }
        }
    }
}
