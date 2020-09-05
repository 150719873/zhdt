package com.dotop.smartwater.project.module.service.revenue;

import com.dotop.smartwater.dependence.core.common.BaseService;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.project.module.core.water.bo.PaymentTradeResultBo;
import com.dotop.smartwater.project.module.core.water.vo.PaymentTradeResultVo;

import java.util.List;

/**
 *
 *

 * @date 2019年8月5日
 */
public interface IPaymentTradeResultService extends BaseService<PaymentTradeResultBo, PaymentTradeResultVo> {

	@Override
	PaymentTradeResultVo add(PaymentTradeResultBo paymentTradeResultBo);

	@Override
	PaymentTradeResultVo edit(PaymentTradeResultBo paymentTradeResultBo);

	@Override
	PaymentTradeResultVo get(PaymentTradeResultBo paymentTradeResultBo);

	@Override
	String del(PaymentTradeResultBo paymentTradeResultBo);

	@Override
	List<PaymentTradeResultVo> list(PaymentTradeResultBo paymentTradeResultBo);

	@Override
	Pagination<PaymentTradeResultVo> page(PaymentTradeResultBo paymentTradeResultBo);

	PaymentTradeResultVo findByTradeId(String tradeid);
}
