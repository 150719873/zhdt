package com.dotop.smartwater.project.module.api.pay.mode;

import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.utils.JSONUtils;
import com.dotop.smartwater.project.module.core.pay.bo.PaymentOrderBo;
import com.dotop.smartwater.project.module.core.pay.bo.PaymentResultBo;
import com.dotop.smartwater.project.module.core.pay.constants.PayConstants;
import com.dotop.smartwater.project.module.core.pay.form.WeChatParam;
import com.dotop.smartwater.project.module.core.pay.vo.PayResultVo;
import com.dotop.smartwater.project.module.core.pay.vo.PaymentResultVo;
import com.dotop.smartwater.project.module.core.pay.wechat.MessageManager;
import com.dotop.smartwater.project.module.core.water.config.Config;
import com.dotop.smartwater.project.module.core.water.constants.ResultCode;
import com.dotop.smartwater.project.module.core.water.vo.customize.WechatPublicSettingVo;
import com.dotop.smartwater.project.module.service.pay.IPaymentOrderService;
import com.dotop.smartwater.project.module.service.pay.IPaymentResultService;
import com.dotop.smartwater.project.module.service.tool.IWechatPublicSettingService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.dotop.smartwater.project.module.api.pay.mode.WeChatCom.unifiedorder;

/**
 * @program: project-parent
 * @description: 微信公众号支付

 * @create: 2019-07-22 11:14
 **/
@Component
public class WeChatOfficialAccountsPay {
    @Autowired
    private IPaymentOrderService iPaymentOrderService;

    @Autowired
    private IPaymentResultService iPaymentResultService;

    @Autowired
    private IWechatPublicSettingService iWechatPublicSettingService;

    private static final Logger LOGGER = LogManager.getLogger(WeChatOfficialAccountsPay.class);

    public PayResultVo unifiedOrder(PaymentOrderBo paymentOrderBo, String realIP) {

        PayResultVo data = new PayResultVo();
        iPaymentOrderService.edit(paymentOrderBo);

        //交易流水号
        String payNumber = String.valueOf(Config.Generator.nextId());

        PaymentResultBo paymentResultBo = new PaymentResultBo();
        paymentResultBo.setPayid(paymentOrderBo.getPayid());
        paymentResultBo.setPayNumber(payNumber);
        paymentResultBo.setAmount(paymentOrderBo.getAmount());
        paymentResultBo.setMode(paymentOrderBo.getMode());
        paymentResultBo.setStatus(PayConstants.TRADE_PAYSTATUS_IN);
        paymentResultBo.setDescription(PayConstants.TRADE_PAYSTATUS_IN_MSG);
        paymentResultBo.setEnterpriseid(paymentOrderBo.getEnterpriseid());

        //插入交易结果表
        PaymentResultVo paymentResultVo = iPaymentResultService.add(paymentResultBo);


        WechatPublicSettingVo weixinConfig = iWechatPublicSettingService
                .getByenterpriseId(paymentOrderBo.getEnterpriseid());

        WeChatParam weChatParam = new WeChatParam();
        weChatParam.setAmount(paymentOrderBo.getAmount());
        weChatParam.setIp(realIP);
        weChatParam.setOutTradeNo(payNumber);
        weChatParam.setOpenid(paymentOrderBo.getOpenid());

        weChatParam.setEnterpriseid(paymentOrderBo.getEnterpriseid());
        weChatParam.setResId(paymentResultVo.getPayResId());

        Map<String, String> resultMap = unifiedorder(weChatParam, weixinConfig);
        if (resultMap.get(MessageManager.SIGN_FLAG) == null) {
            throw new FrameworkRuntimeException(ResultCode.Fail, "签名不合法");
        }

        if (MessageManager.SUCCESS.equals(resultMap.get(MessageManager.RESP_RETURN_CODE))) {
            if (MessageManager.SUCCESS.equals(resultMap.get(MessageManager.RESP_RESULT_CODE))) {
                Map<String, String> signMap = new HashMap<>(10);
                signMap.put("appId", resultMap.get("appId"));
                signMap.put("timeStamp", resultMap.get("timeStamp"));
                signMap.put("package", resultMap.get("package"));
                signMap.put("nonceStr", resultMap.get("nonceStr"));
                signMap.put("signType", resultMap.get("signType"));
                signMap.put("paySign", resultMap.get("paySign"));
                signMap.put("amount", weChatParam.getAmount());

                String reData = JSONUtils.toJSONString(signMap);

                paymentResultBo = new PaymentResultBo();
                paymentResultBo.setPayResId(paymentResultVo.getPayResId());
                paymentResultBo.setThirdPartyNum(reData);
                iPaymentResultService.edit(paymentResultBo);

                data.setReturn_code(PayConstants.WECHAT_ORDER_STATUS_SUCCESS);
                data.setReturn_msg(PayConstants.WECHAT_ORDER_STATUS_SUCCESS_MSG);
                data.setReturn_data(signMap);
                return data;
            } else {
                LOGGER.error("[payNumber] = {},微信支付预下单失败,错误返回的resultMap {}", payNumber, JSONUtils.toJSONString(resultMap));
            }
        } else {
            LOGGER.error("[payNumber] = {},微信支付预下单失败,微信内部错误返回的resultMap {}", payNumber, JSONUtils.toJSONString(resultMap));
        }

        paymentResultBo.setStatus(PayConstants.TRADE_PAYSTATUS_ERROR);
        paymentResultBo.setDescription(resultMap.get(MessageManager.RESP_ERR_CODE_DES));
        paymentResultBo.setPayTime(null);
        paymentResultBo.setPayResId(paymentResultVo.getPayResId());
        iPaymentResultService.edit(paymentResultBo);

        paymentOrderBo.setPayTime(new Date());
        paymentOrderBo.setStatus(PayConstants.TRADE_PAYSTATUS_ERROR);
        iPaymentOrderService.edit(paymentOrderBo);

        data.setReturn_code(PayConstants.WECHAT_ORDER_STATUS_FAIL);
        data.setReturn_msg(resultMap.get(MessageManager.RESP_ERR_CODE_DES));
        return data;
    }

}
