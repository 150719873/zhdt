package com.dotop.smartwater.project.module.dao.revenue;


import com.dotop.smartwater.dependence.core.common.BaseDao;
import com.dotop.smartwater.project.module.core.water.dto.PaymentTradeOrderExtDto;
import com.dotop.smartwater.project.module.core.water.vo.PaymentTradeOrderExtVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**

 */
public interface IPaymentTradeOrderExtDao extends BaseDao<PaymentTradeOrderExtDto, PaymentTradeOrderExtVo> {


	@Override
	void add(PaymentTradeOrderExtDto paymentTradeOrderExtDto);

	@Override
	PaymentTradeOrderExtVo get(PaymentTradeOrderExtDto paymentTradeOrderExtDto);

	@Override
	Integer del(PaymentTradeOrderExtDto paymentTradeOrderExtDto);

	@Override
	Integer edit(PaymentTradeOrderExtDto paymentTradeOrderExtDto);

	@Override
	List<PaymentTradeOrderExtVo> list(PaymentTradeOrderExtDto paymentTradeOrderExtDto);

    List<PaymentTradeOrderExtVo> findNotPayListByTradeIds(@Param("tradeIds")List<String> tradeIds,
	                                                      @Param("enterpriseid")String enterpriseid);

	int handleExtError(@Param("tradeIds")List<String> tradeIds);

	PaymentTradeOrderExtVo findByTradeid(@Param("tradeid")String tradeid);

	void addList(@Param("list") List<PaymentTradeOrderExtDto> extDtoList);
}
