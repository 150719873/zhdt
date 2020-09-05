package com.dotop.smartwater.project.module.dao.revenue;


import com.dotop.smartwater.dependence.core.common.BaseDao;
import com.dotop.smartwater.project.module.core.water.dto.PaymentTradeProDto;
import com.dotop.smartwater.project.module.core.water.vo.PaymentTradeProVo;

import java.util.List;

/**

 */
public interface IPaymentTradeProDao extends BaseDao<PaymentTradeProDto, PaymentTradeProVo> {


	@Override
	void add(PaymentTradeProDto paymentTradeProDto);

	@Override
	PaymentTradeProVo get(PaymentTradeProDto paymentTradeProDto);

	@Override
	Integer del(PaymentTradeProDto paymentTradeProDto);

	@Override
	Integer edit(PaymentTradeProDto paymentTradeProDto);

	@Override
	List<PaymentTradeProVo> list(PaymentTradeProDto paymentTradeProDto);

}
