package com.dotop.smartwater.project.module.service.revenue;

import com.dotop.smartwater.dependence.core.common.BaseService;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.project.module.core.water.bo.PerforWeightBo;
import com.dotop.smartwater.project.module.core.water.vo.PerforWeightVo;

public interface IPerformanceWeightService extends BaseService<PerforWeightBo, PerforWeightVo> {

	@Override
	Pagination<PerforWeightVo> page(PerforWeightBo perforWeightBo);

	int save(PerforWeightBo perforWeightBo);

	int update(PerforWeightBo perforWeightBo);

	int delete(PerforWeightBo perforWeightBo);

	PerforWeightVo getWeight(PerforWeightBo perforWeightBo);

}
