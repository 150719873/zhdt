package com.dotop.smartwater.project.module.api.revenue;

import com.dotop.smartwater.dependence.core.common.BaseFactory;
import com.dotop.smartwater.project.module.core.water.form.customize.SummaryForm;
import com.dotop.smartwater.project.module.core.water.vo.AccountingVo;
import com.dotop.smartwater.project.module.core.water.vo.customize.SummaryVo;

import java.util.List;

/**
 * 收银核算 -- 汇总核算
 * 

 * @date 2019年2月25日
 */
public interface ISummaryAccountFactory extends BaseFactory<SummaryForm, SummaryVo> {

	/**
	 * 当年水司所有收银核算
	 * 
	 * @param summaryForm
	 * @return
	 * @
	 */
	SummaryVo summaryYear(SummaryForm summaryForm) ;

	/**
	 * 汇总当月所有收营员核算信息
	 * 
	 * @param summaryForm
	 * @return
	 * @
	 */
	SummaryVo summaryMonth(SummaryForm summaryForm) ;

	/**
	 * 汇总当月所有收营员核算明细
	 * 
	 * @param summaryForm
	 * @return
	 */
	List<SummaryVo> summaryMonthDetail(SummaryForm summaryForm) ;

	/**
	 * 汇总当月某个收营员每天核算数据
	 * 
	 * @param summaryForm
	 * @return
	 * @
	 */
	List<AccountingVo> summarySelfMonthDetail(SummaryForm summaryForm) ;

	/**
	 * 汇总当月某个收营员核算数据
	 * 
	 * @param summaryForm
	 * @return
	 * @
	 */
	SummaryVo summarySelf(SummaryForm summaryForm) ;

}
