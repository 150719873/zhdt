package com.dotop.smartwater.project.module.service.revenue;

import com.dotop.smartwater.dependence.core.common.BaseService;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.project.module.core.water.bo.PaymentTradeProBo;
import com.dotop.smartwater.project.module.core.water.vo.PaymentTradeProVo;

import java.util.List;

/**
 *
 *

 * @date 2019年8月5日
 */
public interface IPaymentTradeProService extends BaseService<PaymentTradeProBo, PaymentTradeProVo> {

	@Override
	PaymentTradeProVo add(PaymentTradeProBo paymentTradeProBo);

	@Override
	PaymentTradeProVo edit(PaymentTradeProBo paymentTradeProBo);

	@Override
	PaymentTradeProVo get(PaymentTradeProBo paymentTradeProBo);

	@Override
	String del(PaymentTradeProBo paymentTradeProBo);

	@Override
	List<PaymentTradeProVo> list(PaymentTradeProBo paymentTradeProBo);

	@Override
	Pagination<PaymentTradeProVo> page(PaymentTradeProBo paymentTradeProBo);
}
