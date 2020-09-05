package com.dotop.smartwater.project.module.api.pay;

import com.dotop.smartwater.dependence.core.common.BaseFactory;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.project.module.core.pay.form.PaymentOrderForm;
import com.dotop.smartwater.project.module.core.pay.vo.PaymentOrderVo;

/**
 * 支付平台接口
 * @date 2019年7月19日
 */
public interface IPushFactory extends BaseFactory<PaymentOrderForm,PaymentOrderVo>{

    /**
     * 定时任务的推送
     */
    void push() throws FrameworkRuntimeException;


    /**
     * 定时主动查询支付状态
     */
    void getNotify() throws Exception;
}
