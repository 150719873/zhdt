package com.dotop.smartwater.project.module.service.pay;

import com.dotop.smartwater.dependence.core.common.BaseService;
import com.dotop.smartwater.project.module.core.pay.bo.PaymentResultBo;
import com.dotop.smartwater.project.module.core.pay.vo.PaymentResultVo;

public interface IPaymentResultService extends BaseService<PaymentResultBo, PaymentResultVo> {

    PaymentResultVo findByPayId(String payid);
}
