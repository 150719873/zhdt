package com.dotop.smartwater.project.module.dao.revenue;

import com.dotop.smartwater.dependence.core.common.BaseDao;
import com.dotop.smartwater.project.module.core.water.dto.TradeDetailDto;
import com.dotop.smartwater.project.module.core.water.vo.TradeDetailVo;

public interface ITradeDetailDao extends BaseDao<TradeDetailDto, TradeDetailVo> {

	@Override
	void add(TradeDetailDto dto);

	@Override
	TradeDetailVo get(TradeDetailDto dto);

	@Override
	Integer edit(TradeDetailDto dto);

	TradeDetailVo getDetail(TradeDetailDto dto);
}
