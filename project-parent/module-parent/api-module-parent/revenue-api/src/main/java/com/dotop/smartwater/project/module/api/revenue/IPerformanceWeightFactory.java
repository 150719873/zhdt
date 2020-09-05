package com.dotop.smartwater.project.module.api.revenue;

import com.dotop.smartwater.dependence.core.common.BaseFactory;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.project.module.core.water.form.PerforWeightForm;
import com.dotop.smartwater.project.module.core.water.vo.PerforWeightVo;

public interface IPerformanceWeightFactory extends BaseFactory<PerforWeightForm, PerforWeightVo>{
	
	/**
	 * 权重分页查询
	 * @param perforWeightForm
	 * @return
	 * @
	 */
	@Override
	Pagination<PerforWeightVo> page(PerforWeightForm perforWeightForm) ;
	
	/**
	 * 权重新增
	 * @param perforWeightForm
	 * @return
	 * @
	 */
	int save(PerforWeightForm perforWeightForm) ;
	
	/**
	 * 修改权重
	 * @param perforWeightForm
	 * @return
	 * @
	 */
	int update(PerforWeightForm perforWeightForm) ;
	
	/**
	 * 删除权重
	 * @param perforWeightForm
	 * @return
	 * @
	 */
	int delete(PerforWeightForm perforWeightForm) ;
	
	/**
	 * 获取权重详情
	 * @param perforWeightForm
	 * @return
	 * @
	 */
	PerforWeightVo getWeight(PerforWeightForm perforWeightForm) ;
	
	
}
