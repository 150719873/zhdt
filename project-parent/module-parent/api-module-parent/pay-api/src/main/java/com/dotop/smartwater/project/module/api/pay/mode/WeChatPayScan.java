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

import static com.dotop.smartwater.project.module.api.pay.mode.WeChatCom.payScan;

/**
 * @program: project-parent
 * @description: 微信刷卡支付

 * @create: 2019-07-22 11:14
 **/
@Component
public class WeChatPayScan {
    @Autowired
    private IPaymentOrderService iPaymentOrderService;

    @Autowired
    private IPaymentResultService iPaymentResultService;

    @Autowired
    private IWechatPublicSettingService iWechatPublicSettingService;

    private static final Logger LOGGER = LogManager.getLogger(WeChatPayScan.class);

    public PayResultVo generateQRcode(PaymentOrderBo paymentOrderBo, String realIP) {

        PayResultVo data = new PayResultVo();

        iPaymentOrderService.edit(paymentOrderBo);

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

        weChatParam.setTradeName(paymentOrderBo.getTradeName());
        Map<String, String> resultMap = payScan(weChatParam, weixinConfig);
        if (resultMap.get(MessageManager.SIGN_FLAG) == null) {
            throw new FrameworkRuntimeException(ResultCode.Fail, "签名不合法");
        }

        if (MessageManager.SUCCESS.equals(resultMap.get(MessageManager.RESP_RETURN_CODE))) {
            if (MessageManager.SUCCESS.equals(resultMap.get(MessageManager.RESP_RESULT_CODE))) {
                //生成二维码
                LOGGER.info("[payNumber] = {},二维码生成成功,返回的resultMap {}", payNumber, JSONUtils.toJSONString(resultMap));

                Map<String, String> dataMap = new HashMap<>(10);
                dataMap.put("code_url", resultMap.get("code_url"));
                dataMap.put("prepay_id", resultMap.get("prepay_id"));
                dataMap.put("trade_type", resultMap.get("trade_type"));
                dataMap.put("amount", weChatParam.getAmount());

                String reData = JSONUtils.toJSONString(dataMap);

                paymentResultBo = new PaymentResultBo();
                paymentResultBo.setPayResId(paymentResultVo.getPayResId());
                paymentResultBo.setThirdPartyNum(reData);
                iPaymentResultService.edit(paymentResultBo);


                data.setReturn_code(PayConstants.WECHAT_ORDER_STATUS_SUCCESS);
                data.setReturn_msg(PayConstants.WECHAT_ORDER_STATUS_SUCCESS_MSG);
                data.setReturn_data(dataMap);
                return data;
            } else {
                /**过期则要重新扫码,请把订单修改为失败
                 微信服务器错误也要重新扫码支付*/
                LOGGER.error("[payNumber] = {},二维码生成失败,错误返回的resultMap {}", payNumber, JSONUtils.toJSONString(resultMap));
            }
        } else {
            LOGGER.error("[payNumber] = {},二维码生成失败,微信内部错误返回的resultMap {}", payNumber, JSONUtils.toJSONString(resultMap));
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
