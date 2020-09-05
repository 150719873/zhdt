package com.dotop.smartwater.project.module.api.pay;

import com.dotop.smartwater.dependence.core.common.BaseFactory;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.project.module.core.pay.form.PaymentOrderForm;
import com.dotop.smartwater.project.module.core.pay.vo.PayResultVo;
import com.dotop.smartwater.project.module.core.pay.vo.PaymentOrderVo;

import java.util.Map;

/**
 * 支付平台接口
 * @date 2019年7月19日
 */
public interface IPayFactory extends BaseFactory<PaymentOrderForm,PaymentOrderVo>{

    /**
     * 统一下单接口
     * @param paymentOrderForm 提交的参数
     * @param realIP 调用者IP
     * @return 根据支付方式返回结果
     */
    PayResultVo submit(PaymentOrderForm paymentOrderForm, String realIP) throws FrameworkRuntimeException;

    /**
     * 微信回调结果处理
     * @param resultMap 微信回调结果
     * @throws Exception
     */
    void handleCallBack(Map<String,String> resultMap) throws Exception;

    /**
     * 查询账单结果
     * @param paymentOrderForm 查询参数
     * @return 账单结果
     * @throws Exception
     */
    PayResultVo orderquery(PaymentOrderForm paymentOrderForm) throws Exception;

    PayResultVo ordercancel(PaymentOrderForm paymentOrderForm) throws Exception;
}
