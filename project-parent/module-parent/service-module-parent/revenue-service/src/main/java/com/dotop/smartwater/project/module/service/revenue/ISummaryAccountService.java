package com.dotop.smartwater.project.module.service.revenue;

import java.util.List;

import com.dotop.smartwater.dependence.core.common.BaseBo;
import com.dotop.smartwater.dependence.core.common.BaseService;
import com.dotop.smartwater.project.module.core.water.vo.AccountingVo;
import com.dotop.smartwater.project.module.core.water.vo.customize.SummaryVo;

/**
 * 收银核算 和 汇总核算
 * 

 * @date 2019年2月25日
 */
public interface ISummaryAccountService extends BaseService<BaseBo, SummaryVo> {

	/**
	 * 计算
	 * 
	 * @param year
	 * @param enterpriseid
	 * @return
	 */
	SummaryVo summaryData(String year, String enterpriseid);

	List<SummaryVo> summaryDetail(String string, String enterpriseid);

	/**
	 * 汇总当月某个收营员每天核算数据
	 * 
	 * @param string
	 * @param userid
	 * @param enterpriseid
	 * @return @
	 */
	List<AccountingVo> summarySelfDetail(String string, String userid, String enterpriseid);

	/**
	 * 汇总当月某个收营员核算数据
	 * 
	 * @param string
	 * @param userid
	 * @param enterpriseid
	 * @return @
	 */
	SummaryVo summarySelf(String string, String userid, String enterpriseid);

}
