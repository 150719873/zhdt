package com.dotop.smartwater.project.module.service.revenue;

import com.dotop.smartwater.dependence.core.common.BaseService;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.project.module.core.water.bo.TradeOrderBo;
import com.dotop.smartwater.project.module.core.water.vo.TradeOrderVo;

public interface ITradeOrderService extends BaseService<TradeOrderBo, TradeOrderVo> {

	@Override
	Pagination<TradeOrderVo> page(TradeOrderBo bo);

	int save(TradeOrderBo bo);

	int update(TradeOrderBo bo);

	int updatePayStatus(TradeOrderBo bo);

	@Override
	TradeOrderVo get(TradeOrderBo bo);

}
