package com.dotop.smartwater.project.module.api.pay.mode;

import com.dotop.smartwater.dependence.core.utils.DateUtils;
import com.dotop.smartwater.project.module.core.pay.bo.PaymentOrderBo;
import com.dotop.smartwater.project.module.core.pay.bo.PaymentResultBo;
import com.dotop.smartwater.project.module.core.pay.constants.PayConstants;
import com.dotop.smartwater.project.module.core.pay.vo.PayResultVo;
import com.dotop.smartwater.project.module.core.pay.vo.PushVo;
import com.dotop.smartwater.project.module.core.water.config.Config;
import com.dotop.smartwater.project.module.service.pay.IPaymentOrderService;
import com.dotop.smartwater.project.module.service.pay.IPaymentResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @program: project-parent
 * @description: 现金支付

 * @create: 2019-07-22 11:14
 **/
@Component
public class MoneyPay {
    @Autowired
    private IPaymentOrderService iPaymentOrderService;

    @Autowired
    private IPaymentResultService iPaymentResultService;

    private static final String SuccessRemark = "现金交易成功";

    public PayResultVo pay(PaymentOrderBo paymentOrderBo){
        Date date = new Date();
        PayResultVo data = new PayResultVo();
        paymentOrderBo.setStatus(PayConstants.TRADE_PAYSTATUS_SUCCESS);
        paymentOrderBo.setPayTime(date);


        iPaymentOrderService.edit(paymentOrderBo);

        //交易流水号
        String payNumber = String.valueOf(Config.Generator.nextId());

        //插入交易结果表
        PaymentResultBo paymentResultBo = new PaymentResultBo();
        paymentResultBo.setPayid(paymentOrderBo.getPayid());
        paymentResultBo.setPayNumber(payNumber);
        paymentResultBo.setAmount(paymentOrderBo.getAmount());
        paymentResultBo.setMode(paymentOrderBo.getMode());
        paymentResultBo.setStatus(PayConstants.TRADE_PAYSTATUS_SUCCESS);
        paymentResultBo.setDescription(SuccessRemark);
        paymentResultBo.setEnterpriseid(paymentOrderBo.getEnterpriseid());
        paymentResultBo.setPayTime(date);

        iPaymentResultService.add(paymentResultBo);


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
        data.setReturn_msg("支付成功");
        data.setPushVo(pushVo);
        return data;
    }
}
