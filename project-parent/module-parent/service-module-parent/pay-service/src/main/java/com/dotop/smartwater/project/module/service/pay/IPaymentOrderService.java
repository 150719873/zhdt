package com.dotop.smartwater.project.module.service.pay;

import com.dotop.smartwater.dependence.core.common.BaseService;
import com.dotop.smartwater.project.module.core.pay.bo.PaymentOrderBo;
import com.dotop.smartwater.project.module.core.pay.vo.PaymentOrderVo;

import java.util.List;


public interface IPaymentOrderService extends BaseService<PaymentOrderBo, PaymentOrderVo> {


    PaymentOrderVo findByTradeNumberAndEid(String tradeNumber, String enterpriseid);

    List<PaymentOrderVo> getPayInList(String status, Integer minutes);
}
