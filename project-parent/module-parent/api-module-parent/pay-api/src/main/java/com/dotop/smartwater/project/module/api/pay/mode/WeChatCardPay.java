package com.dotop.smartwater.project.module.api.pay.mode;

import com.dotop.smartwater.dependence.core.utils.DateUtils;
import com.dotop.smartwater.dependence.core.utils.JSONUtils;
import com.dotop.smartwater.project.module.core.pay.bo.PaymentOrderBo;
import com.dotop.smartwater.project.module.core.pay.bo.PaymentResultBo;
import com.dotop.smartwater.project.module.core.pay.constants.PayConstants;
import com.dotop.smartwater.project.module.core.pay.form.WeChatParam;
import com.dotop.smartwater.project.module.core.pay.vo.PayResultVo;
import com.dotop.smartwater.project.module.core.pay.vo.PaymentResultVo;
import com.dotop.smartwater.project.module.core.pay.vo.PushVo;
import com.dotop.smartwater.project.module.core.pay.wechat.MessageManager;
import com.dotop.smartwater.project.module.core.water.config.Config;
import com.dotop.smartwater.project.module.core.water.vo.customize.WechatPublicSettingVo;
import com.dotop.smartwater.project.module.service.pay.IPaymentOrderService;
import com.dotop.smartwater.project.module.service.pay.IPaymentResultService;
import com.dotop.smartwater.project.module.service.tool.IWechatPublicSettingService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;

import static com.dotop.smartwater.project.module.api.pay.mode.WeChatCom.payByCard;

/**
 * @program: project-parent
 * @description: 微信刷卡支付

 * @create: 2019-07-22 11:14
 **/
@Component
public class WeChatCardPay {
    @Autowired
    private IPaymentOrderService iPaymentOrderService;

    @Autowired
    private IPaymentResultService iPaymentResultService;

    @Autowired
    private IWechatPublicSettingService iWechatPublicSettingService;

    private static final Logger LOGGER = LogManager.getLogger(WeChatCardPay.class);

    private static final String SuccessRemark = "微信刷卡支付成功";

    public PayResultVo pay(PaymentOrderBo paymentOrderBo, String realIP) {

        PayResultVo data = new PayResultVo();

        //交易流水号
        String payNumber = String.valueOf(Config.Generator.nextId());

        //插入交易结果表
        PaymentResultBo paymentResultBo = new PaymentResultBo();
        paymentResultBo.setPayid(paymentOrderBo.getPayid());
        paymentResultBo.setPayNumber(payNumber);
        paymentResultBo.setAmount(paymentOrderBo.getAmount());
        paymentResultBo.setMode(paymentOrderBo.getMode());
        paymentResultBo.setStatus(PayConstants.TRADE_PAYSTATUS_IN);
        paymentResultBo.setDescription(PayConstants.TRADE_PAYSTATUS_IN_MSG);
        paymentResultBo.setEnterpriseid(paymentOrderBo.getEnterpriseid());

        PaymentResultVo paymentResultVo = iPaymentResultService.add(paymentResultBo);


        WechatPublicSettingVo weixinConfig = iWechatPublicSettingService
                .getByenterpriseId(paymentOrderBo.getEnterpriseid());

        WeChatParam weChatParam = new WeChatParam();
        weChatParam.setAmount(paymentOrderBo.getAmount());
        weChatParam.setIp(realIP);
        weChatParam.setOutTradeNo(payNumber);
        weChatParam.setPayCard(paymentOrderBo.getPayCard());
        weChatParam.setEnterpriseid(paymentOrderBo.getEnterpriseid());
        weChatParam.setResId(paymentResultVo.getPayResId());

        Map<String, String> resultMap = payByCard(weChatParam, weixinConfig);
        if (resultMap.get(MessageManager.SIGN_FLAG) == null) {
            LOGGER.info("[payNumber] = {}, 签名不合法", payNumber);
        }

        if (MessageManager.SUCCESS.equals(resultMap.get(MessageManager.RESP_RETURN_CODE))) {
            if (MessageManager.SUCCESS.equals(resultMap.get(MessageManager.RESP_RESULT_CODE))) {
                LOGGER.info("[payNumber] = {}, 微信刷卡支付完成,返回的resultMap {}", payNumber, JSONUtils.toJSONString(resultMap));

                String payTime = resultMap.get("time_end");

                Date payDate = new Date();
                if (payTime != null) {
                    DateTime dateTime = DateTimeFormat.forPattern("yyyyMMddHHmmss").parseDateTime(payTime);
                    payDate = dateTime.toDate();
                    LOGGER.info("微信支付返回的支付时间 payDate :" + payDate);
                }

                paymentResultBo.setStatus(PayConstants.TRADE_PAYSTATUS_SUCCESS);
                paymentResultBo.setDescription(PayConstants.TRADE_PAYSTATUS_SUCCESS_MSG);
                paymentResultBo.setPayTime(payDate);
                paymentResultBo.setPayResId(paymentResultVo.getPayResId());
                iPaymentResultService.edit(paymentResultBo);

                paymentOrderBo.setPayTime(payDate);
                paymentOrderBo.setStatus(PayConstants.TRADE_PAYSTATUS_SUCCESS);
                iPaymentOrderService.edit(paymentOrderBo);

                PushVo pushVo = new PushVo();
                pushVo.setDescription(paymentResultBo.getDescription());
                pushVo.setStatus(paymentResultBo.getStatus());
                pushVo.setEnterpriseid(paymentOrderBo.getEnterpriseid());
                pushVo.setExtra(paymentOrderBo.getExtra());
                pushVo.setAmount(paymentOrderBo.getAmount());
                pushVo.setMode(paymentOrderBo.getMode());
                pushVo.setTradeName(paymentOrderBo.getTradeName());
                pushVo.setTradeNumber(paymentOrderBo.getTradeNumber());
                pushVo.setPayNumber(paymentResultBo.getPayNumber());
                pushVo.setPayTime(DateUtils.formatDatetime(paymentResultBo.getPayTime()));

                data.setReturn_code(PayConstants.TRADE_PAYSTATUS_SUCCESS);
                data.setReturn_msg(PayConstants.TRADE_PAYSTATUS_SUCCESS_MSG);
                data.setPushVo(pushVo);
                return data;
            } else {
                /**过期则要重新扫码,请把订单修改为失败
                 微信服务器错误也要重新扫码支付*/
                LOGGER.error("[payNumber] = {},微信刷卡支付失败,错误返回的resultMap {}", payNumber, JSONUtils.toJSONString(resultMap));
            }
        } else {
            LOGGER.error("[payNumber] = {},微信刷卡支付失败,微信内部错误返回的resultMap {}", payNumber, JSONUtils.toJSONString(resultMap));
        }

        paymentResultBo.setStatus(PayConstants.TRADE_PAYSTATUS_ERROR);
        paymentResultBo.setDescription(resultMap.get(MessageManager.RESP_ERR_CODE_DES));
        paymentResultBo.setPayTime(null);
        paymentResultBo.setPayResId(paymentResultVo.getPayResId());
        iPaymentResultService.edit(paymentResultBo);

        paymentOrderBo.setStatus(PayConstants.TRADE_PAYSTATUS_ERROR);
        iPaymentOrderService.edit(paymentOrderBo);

        PushVo pushVo = new PushVo();
        pushVo.setDescription(paymentResultBo.getDescription());
        pushVo.setStatus(paymentResultBo.getStatus());
        pushVo.setEnterpriseid(paymentOrderBo.getEnterpriseid());
        pushVo.setExtra(paymentOrderBo.getExtra());
        pushVo.setAmount(paymentOrderBo.getAmount());
        pushVo.setMode(paymentOrderBo.getMode());
        pushVo.setTradeName(paymentOrderBo.getTradeName());
        pushVo.setTradeNumber(paymentOrderBo.getTradeNumber());
        pushVo.setPayNumber(paymentResultBo.getPayNumber());
        pushVo.setPayTime(null);

        data.setReturn_code(PayConstants.TRADE_PAYSTATUS_ERROR);
        data.setReturn_msg(resultMap.get(MessageManager.RESP_ERR_CODE_DES));
        data.setPushVo(pushVo);
        return data;
    }


}
