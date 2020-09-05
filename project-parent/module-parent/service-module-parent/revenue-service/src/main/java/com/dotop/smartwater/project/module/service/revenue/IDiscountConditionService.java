package com.dotop.smartwater.project.module.service.revenue;

import java.util.List;

import com.dotop.smartwater.dependence.core.common.BaseService;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.project.module.core.water.bo.ConditionBo;
import com.dotop.smartwater.project.module.core.water.vo.ConditionVo;

public interface IDiscountConditionService extends BaseService<ConditionBo, ConditionVo> {

	void savecondition(ConditionBo conditionBo);

	void editcondition(ConditionBo conditionBo);

	ConditionVo getCondition(ConditionBo conditionBo);

	void deleteCond(ConditionBo conditionBo);

	Pagination<ConditionVo> findcondition(ConditionBo conditionBo);

	List<ConditionVo> getcondtions(ConditionBo conditionBo);
}
