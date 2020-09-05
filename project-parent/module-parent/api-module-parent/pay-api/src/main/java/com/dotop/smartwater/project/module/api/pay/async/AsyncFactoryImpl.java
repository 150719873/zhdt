package com.dotop.smartwater.project.module.api.pay.async;

import com.dotop.smartwater.dependence.core.utils.BeanUtils;
import com.dotop.smartwater.dependence.core.utils.DateUtils;
import com.dotop.smartwater.dependence.core.utils.JSONUtils;
import com.dotop.smartwater.project.module.api.pay.mode.MoneyPay;
import com.dotop.smartwater.project.module.api.pay.mode.WeChatCardPay;
import com.dotop.smartwater.project.module.api.pay.IAsyncFactory;
import com.dotop.smartwater.project.module.core.pay.bo.PaymentCallbackBo;
import com.dotop.smartwater.project.module.core.pay.bo.PaymentOrderBo;
import com.dotop.smartwater.project.module.core.pay.bo.PaymentPushRecordBo;
import com.dotop.smartwater.project.module.core.pay.bo.PaymentResultBo;
import com.dotop.smartwater.project.module.core.pay.constants.PayConstants;
import com.dotop.smartwater.project.module.core.pay.utils.PushUtil;
import com.dotop.smartwater.project.module.core.pay.vo.PayResultVo;
import com.dotop.smartwater.project.module.core.pay.vo.PaymentOrderVo;
import com.dotop.smartwater.project.module.core.pay.vo.PaymentResultVo;
import com.dotop.smartwater.project.module.core.pay.vo.PushVo;
import com.dotop.smartwater.project.module.service.pay.IPaymentCallbackService;
import com.dotop.smartwater.project.module.service.pay.IPaymentOrderService;
import com.dotop.smartwater.project.module.service.pay.IPaymentPushRecordService;
import com.dotop.smartwater.project.module.service.pay.IPaymentResultService;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;

/**
 * 支付平台接口
 *

 * @date 2019年2月23日
 */

@Component
public class AsyncFactoryImpl implements IAsyncFactory {

    private static final Logger LOGGER = LogManager.getLogger(AsyncFactoryImpl.class);

    @Autowired
    private MoneyPay moneyPay;

    @Autowired
    private WeChatCardPay weChatCardPay;

    @Autowired
    private IPaymentOrderService iPaymentOrderService;

    @Autowired
    private IPaymentResultService iPaymentResultService;

    @Autowired
    private IPaymentPushRecordService iPaymentPushRecordService;

    @Autowired
    private IPaymentCallbackService iPaymentCallbackService;

    @Async("moneyPayExecutor")
    @Override
    public void payByMoney(PaymentOrderBo paymentOrderBo) {

        PayResultVo data = moneyPay.pay(paymentOrderBo);
        isHavePush(paymentOrderBo.getCallbackUrl(), data.getPushVo(), paymentOrderBo);
    }

    @Async("cardPayExecutor")
    @Override
    public void payByCard(PaymentOrderBo paymentOrderBo, String realIP) {
        PayResultVo data = weChatCardPay.pay(paymentOrderBo, realIP);
        isHavePush(paymentOrderBo.getCallbackUrl(), data.getPushVo(), paymentOrderBo);
    }

    @Async("handleExecutor")
    @Override
    public void handleCallBack(PaymentResultBo paymentResultBo, Map<String, String> resultMap) {
        PaymentResultVo paymentResultVo = iPaymentResultService.get(paymentResultBo);
        if (paymentResultVo == null) {
            LOGGER.info("没有找到paymentResultVo : " + paymentResultBo.getPayResId());
            return;
        }
        if (paymentResultVo.getPayid() == null) {
            LOGGER.info("没有找到PayId : " + JSONUtils.toJSONString(paymentResultVo));
        }
        PaymentOrderBo paymentOrderBo = new PaymentOrderBo();
        paymentOrderBo.setPayid(paymentResultVo.getPayid());
        PaymentOrderVo paymentOrderVo = iPaymentOrderService.get(paymentOrderBo);
        if (paymentResultVo == null) {
            LOGGER.info("没有找到paymentOrderVo : " + paymentResultVo.getPayid());
            return;
        }

        PushVo pushVo = new PushVo();
        pushVo.setDescription(paymentResultVo.getDescription());
        pushVo.setStatus(paymentResultVo.getStatus());
        pushVo.setEnterpriseid(paymentOrderVo.getEnterpriseid());
        pushVo.setExtra(paymentOrderVo.getExtra());
        pushVo.setAmount(paymentOrderVo.getAmount());
        pushVo.setMode(paymentOrderVo.getMode());
        pushVo.setTradeName(paymentOrderVo.getTradeName());
        pushVo.setTradeNumber(paymentOrderVo.getTradeNumber());
        pushVo.setPayNumber(paymentResultVo.getPayNumber());
        if (PayConstants.TRADE_PAYSTATUS_SUCCESS.equals(paymentResultBo.getStatus())) {
            pushVo.setPayTime(DateUtils.formatDatetime(paymentResultBo.getPayTime()));
        }

        isHavePush(paymentOrderVo.getCallbackUrl(), pushVo, BeanUtils.copy(paymentOrderVo,PaymentOrderBo.class));

        //写入第三方回调记录表
        PaymentCallbackBo paymentCallbackBo = new PaymentCallbackBo();
        paymentCallbackBo.setDetail(JSONUtils.toJSONString(resultMap));
        paymentCallbackBo.setEnterpriseid(paymentOrderVo.getEnterpriseid());
        paymentCallbackBo.setMode(paymentOrderVo.getMode());
        paymentCallbackBo.setPayResId(paymentResultVo.getPayResId());
        paymentCallbackBo.setTradeNumber(paymentOrderVo.getTradeNumber());
        iPaymentCallbackService.add(paymentCallbackBo);
    }


    private void isHavePush(String url, PushVo pushVo, PaymentOrderBo paymentOrderBo) {
        if (StringUtils.isNotBlank(url) && pushVo != null && paymentOrderBo != null) {
            String result = PushUtil.pushDataToOtherServer(url,
                    JSONUtils.toJSONString(pushVo));

            PaymentPushRecordBo paymentPushRecordBo = new PaymentPushRecordBo();
            paymentPushRecordBo.setTimes(1);
            paymentPushRecordBo.setEnterpriseid(paymentOrderBo.getEnterpriseid());
            paymentPushRecordBo.setPayid(paymentOrderBo.getPayid());
            paymentPushRecordBo.setPushData(JSONUtils.toJSONString(pushVo));
            paymentPushRecordBo.setPushTime(new Date());
            paymentPushRecordBo.setPushUrl(url);

            if (PayConstants.RESULT_RESPONSE_SUCCESS_CODE.equals(result)) {
                LOGGER.info("推送成功 : " + result);
                paymentPushRecordBo.setStatus(PayConstants.PUSH_STATUS_SUCCESS);
            } else {
                LOGGER.info("推送失败 : " + result);
                paymentPushRecordBo.setStatus(PayConstants.PUSH_STATUS_FAIL);
            }
            iPaymentPushRecordService.add(paymentPushRecordBo);
        }
    }
}
