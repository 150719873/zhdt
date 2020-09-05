package com.dotop.smartwater.project.module.dao.revenue;


import com.dotop.smartwater.dependence.core.common.BaseDao;
import com.dotop.smartwater.project.module.core.water.dto.PaymentTradeExtraDto;
import com.dotop.smartwater.project.module.core.water.dto.PaymentTradeOrderDto;
import com.dotop.smartwater.project.module.core.water.vo.PaymentTradeExtraVo;
import com.dotop.smartwater.project.module.core.water.vo.PaymentTradeOrderVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**

 */
public interface IPaymentTradeOrderDao extends BaseDao<PaymentTradeOrderDto, PaymentTradeOrderVo> {


	@Override
	void add(PaymentTradeOrderDto paymentTradeOrderDto);

	@Override
	PaymentTradeOrderVo get(PaymentTradeOrderDto paymentTradeOrderDto);

	@Override
	Integer del(PaymentTradeOrderDto paymentTradeOrderDto);

	@Override
	Integer edit(PaymentTradeOrderDto paymentTradeOrderDto);

	@Override
	List<PaymentTradeOrderVo> list(PaymentTradeOrderDto paymentTradeOrderDto);

	PaymentTradeOrderVo findByEidAndTradeNum(@Param("enterpriseid")String enterpriseid,
											 @Param("tradeNumber")String tradeNumber);

    List<PaymentTradeOrderVo> findListByIds(@Param("tradeIds")List<String> tradeIds);

    void addExtra(PaymentTradeExtraDto paymentTradeExtraDto);

	PaymentTradeExtraVo getExtra(@Param("extraid")String extraid);

	int handleError(@Param("tradeIds")List<String> tradeIds);

	void addList(@Param("list")List<PaymentTradeOrderDto> dtoList);
}
