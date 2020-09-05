package com.dotop.smartwater.project.module.dao.revenue;

import java.util.List;

import com.dotop.smartwater.dependence.core.common.BaseDao;
import com.dotop.smartwater.project.module.core.water.dto.TradeOrderDto;
import com.dotop.smartwater.project.module.core.water.vo.TradeOrderVo;

public interface ITradeOrderDao extends BaseDao<TradeOrderDto, TradeOrderVo> {

	List<TradeOrderVo> getList(TradeOrderDto dto);

	int save(TradeOrderDto dto);

	@Override
	TradeOrderVo get(TradeOrderDto dto);

	int update(TradeOrderDto dto);

	int updatePayStatus(TradeOrderDto dto);

	@Override
	Integer del(TradeOrderDto dto);

}
