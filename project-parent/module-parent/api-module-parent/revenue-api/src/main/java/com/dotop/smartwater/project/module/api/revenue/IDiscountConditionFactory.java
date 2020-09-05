package com.dotop.smartwater.project.module.api.revenue;

import com.dotop.smartwater.dependence.core.common.BaseFactory;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.project.module.core.water.form.ConditionForm;
import com.dotop.smartwater.project.module.core.water.vo.ConditionVo;

/**
 * 折扣接口

 */
public interface IDiscountConditionFactory extends BaseFactory<ConditionForm, ConditionVo> {

	/**
	 * 保存
	 * @param conditionForm 参数对象
	 */
	void savecondition(ConditionForm conditionForm);

	/**
	 * 编辑对象
	 * @param conditionForm 参数对象
	 */
	void editcondition(ConditionForm conditionForm);

	/**
	 * 获取
	 * @param conditionForm 参数对象
	 * @return 获取对象
	 */
	ConditionVo getCondition(ConditionForm conditionForm);

	/**
	 * 删除
	 * @param conditionForm 参数对象
	 */
	void deleteCond(ConditionForm conditionForm);

	/**
	 * 折扣分页
	 * @param conditionForm 参数对象
	 * @return 折扣分页
	 */
	Pagination<ConditionVo> findcondition(ConditionForm conditionForm);

}
