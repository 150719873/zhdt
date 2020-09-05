package com.dotop.smartwater.project.module.api.revenue;

import com.dotop.smartwater.dependence.core.common.BaseFactory;
import com.dotop.smartwater.project.module.core.water.form.PaymentTradeOrderExtForm;
import com.dotop.smartwater.project.module.core.water.form.PaymentTradeOrderForm;
import com.dotop.smartwater.project.module.core.water.vo.PaymentTradeOrderVo;

/**
 * 推送处理

 * @date 2019年8月16日
 */
public interface IPushFactory extends BaseFactory<PaymentTradeOrderForm, PaymentTradeOrderVo> {

	void push(PaymentTradeOrderExtForm ext);

}
