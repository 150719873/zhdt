package com.dotop.smartwater.project.module.api.revenue;

import com.dotop.smartwater.dependence.core.common.BaseFactory;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.project.module.core.water.form.customize.SummaryForm;
import com.dotop.smartwater.project.module.core.water.form.customize.SupplementForm;
import com.dotop.smartwater.project.module.core.water.vo.customize.SummaryVo;
import com.dotop.smartwater.project.module.core.water.vo.customize.SupplementVo;

/**
 * 收银核算 -- 平账记录
 * 

 * @date 2019年2月25日
 */
public interface ISupplementAccountFactory extends BaseFactory<SummaryForm, SummaryVo> {

	/**
	 * 平账记录
	 * 
	 * @param supplementForm
	 * @return
	 * @
	 */
	Pagination<SupplementVo> page(SupplementForm supplementForm) ;

	/**
	 * 一键平账
	 * 
	 * @param supplementForm
	 */
	void oneKeyBalance(SupplementForm supplementForm);

	/**
	 * 个人平账
	 * 
	 * @param supplementForm
	 * @return
	 */
	void supplementSelf(SupplementForm supplementForm);
}
