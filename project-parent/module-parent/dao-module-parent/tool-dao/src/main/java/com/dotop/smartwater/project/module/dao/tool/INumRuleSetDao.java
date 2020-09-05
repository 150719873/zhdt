package com.dotop.smartwater.project.module.dao.tool;

import com.dotop.smartwater.dependence.core.common.BaseDao;
import com.dotop.smartwater.project.module.core.water.dto.NumRuleSetDto;
import com.dotop.smartwater.project.module.core.water.vo.NumRuleSetVo;

import java.util.List;

/**
 * 打印设计
 *

 * @date 2019-03-07 10:02
 */

public interface INumRuleSetDao extends BaseDao<NumRuleSetDto, NumRuleSetVo> {

	List<NumRuleSetVo> findByEnterpriseId(NumRuleSetDto numRuleSetDto);

	List<NumRuleSetVo> getBaseRuleList();

	@Override
	void add(NumRuleSetDto numRuleSetDto);

	@Override
	Integer edit(NumRuleSetDto numRuleSetDto);

	NumRuleSetVo findByEnterpriseIdAndRuleId(NumRuleSetDto numRuleSetDto);

	int deleteByEnterpriseIdAndRuleId(NumRuleSetDto numRuleSetDto);

}
