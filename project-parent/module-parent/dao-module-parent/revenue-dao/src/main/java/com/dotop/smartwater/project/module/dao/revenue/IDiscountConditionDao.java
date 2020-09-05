package com.dotop.smartwater.project.module.dao.revenue;

import com.dotop.smartwater.dependence.core.common.BaseDao;
import com.dotop.smartwater.project.module.core.water.bo.ConditionBo;
import com.dotop.smartwater.project.module.core.water.dto.ConditionDto;
import com.dotop.smartwater.project.module.core.water.vo.ConditionVo;
import com.dotop.smartwater.project.module.core.water.vo.DiscountVo;

import java.util.List;

public interface IDiscountConditionDao extends BaseDao<ConditionDto, DiscountVo> {

	List<ConditionVo> findcondition(ConditionBo conditionBo);

	void savecondition(ConditionDto conditionDto);

	void editcondition(ConditionDto conditionDto);

	ConditionVo getCondition(ConditionDto conditionDto);

	void deleteCond(ConditionDto conditionDto);

	List<ConditionVo> getcondtions(ConditionBo conditionBo);

}
