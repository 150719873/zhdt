package com.dotop.smartwater.project.module.service.pay;

import com.dotop.smartwater.dependence.core.common.BaseService;
import com.dotop.smartwater.project.module.core.pay.bo.PaymentPushRecordBo;
import com.dotop.smartwater.project.module.core.pay.vo.PaymentPushRecordVo;

import java.util.List;

public interface IPaymentPushRecordService extends BaseService<PaymentPushRecordBo, PaymentPushRecordVo> {


    List<PaymentPushRecordVo> getFailList(String status, Integer minutes);
}
