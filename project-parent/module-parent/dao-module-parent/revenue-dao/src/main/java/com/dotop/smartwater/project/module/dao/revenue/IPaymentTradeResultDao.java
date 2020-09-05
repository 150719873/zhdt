package com.dotop.smartwater.project.module.dao.revenue;


import com.dotop.smartwater.dependence.core.common.BaseDao;
import com.dotop.smartwater.project.module.core.water.dto.PaymentTradeResultDto;
import com.dotop.smartwater.project.module.core.water.vo.PaymentTradeResultVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**

 */
public interface IPaymentTradeResultDao extends BaseDao<PaymentTradeResultDto, PaymentTradeResultVo> {


	@Override
	void add(PaymentTradeResultDto paymentTradeResultDto);

	@Override
	PaymentTradeResultVo get(PaymentTradeResultDto paymentTradeResultDto);

	PaymentTradeResultVo findByTradeId(@Param("tradeid")String tradeid);

	@Override
	Integer del(PaymentTradeResultDto paymentTradeResultDto);

	@Override
	Integer edit(PaymentTradeResultDto paymentTradeResultDto);

	@Override
	List<PaymentTradeResultVo> list(PaymentTradeResultDto paymentTradeResultDto);

}
