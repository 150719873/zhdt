package com.dotop.smartwater.project.module.service.revenue;

import com.dotop.smartwater.dependence.core.common.BaseService;
import com.dotop.smartwater.project.module.core.water.bo.TradeDetailBo;
import com.dotop.smartwater.project.module.core.water.vo.TradeDetailVo;

/**

 */
public interface ITradeDetailService extends BaseService<TradeDetailBo, TradeDetailVo> {

	@Override
	TradeDetailVo add(TradeDetailBo bo);

	@Override
	TradeDetailVo edit(TradeDetailBo bo);

	@Override
	TradeDetailVo get(TradeDetailBo bo);

	TradeDetailVo getDetail(TradeDetailBo bo);

}
