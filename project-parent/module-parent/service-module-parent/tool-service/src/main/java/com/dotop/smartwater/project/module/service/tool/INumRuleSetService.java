package com.dotop.smartwater.project.module.service.tool;

import java.util.List;

import com.dotop.smartwater.dependence.core.common.BaseService;
import com.dotop.smartwater.project.module.core.water.bo.NumRuleSetBo;
import com.dotop.smartwater.project.module.core.water.vo.NumRuleSetVo;

/**

 * @date 2019年2月23日
 */
public interface INumRuleSetService extends BaseService<NumRuleSetBo, NumRuleSetVo> {

	List<NumRuleSetVo> findByEnterpriseId(NumRuleSetBo numRuleSetBo);

	List<NumRuleSetVo> getBaseRuleList();

	@Override
	NumRuleSetVo add(NumRuleSetBo numRuleSetBo);

	@Override
	NumRuleSetVo edit(NumRuleSetBo numRuleSetBo);

	NumRuleSetVo findByEnterpriseIdAndRuleId(NumRuleSetBo numRuleSetBo);

	int deleteByEnterpriseIdAndRuleId(NumRuleSetBo numRuleSetBo);

}
