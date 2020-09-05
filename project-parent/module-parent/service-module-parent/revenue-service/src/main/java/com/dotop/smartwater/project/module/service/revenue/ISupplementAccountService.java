package com.dotop.smartwater.project.module.service.revenue;

import java.util.List;

import com.dotop.smartwater.dependence.core.common.BaseBo;
import com.dotop.smartwater.dependence.core.common.BaseService;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.project.module.core.auth.vo.UserVo;
import com.dotop.smartwater.project.module.core.water.form.customize.SupplementForm;
import com.dotop.smartwater.project.module.core.water.vo.AccountingVo;
import com.dotop.smartwater.project.module.core.water.vo.customize.SummaryVo;
import com.dotop.smartwater.project.module.core.water.vo.customize.SupplementVo;

/**
 * 收银核算 和 汇总核算
 * 

 * @date 2019年2月25日
 */
public interface ISupplementAccountService extends BaseService<BaseBo, SummaryVo> {

	/**
	 * 个人平账-- 查询记录
	 * 
	 * @param year
	 * @param enterpriseid
	 * @return
	 */
	List<AccountingVo> findMonthAccounting(SupplementForm sp);

	/**
	 * 个人平账--修改
	 * 
	 * @param list
	 * @param user
	 * @
	 */
	void updateSupplement(List<AccountingVo> list, UserVo user);

	/**
	 * 查询平账记录
	 * 
	 * @param sp
	 * @param user
	 * @return
	 */
	Pagination<SupplementVo> page(SupplementForm sp, UserVo user);

}
