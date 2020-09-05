package com.dotop.smartwater.project.module.service.revenue;

import com.dotop.smartwater.dependence.core.common.BaseService;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.project.module.core.water.bo.AccountingBo;
import com.dotop.smartwater.project.module.core.water.bo.MarkOrderBo;
import com.dotop.smartwater.project.module.core.water.vo.AccountingVo;
import com.dotop.smartwater.project.module.core.water.vo.OrderVo;

/**
 * 收银核算 和人核算功能
 * 

 * @date 2019年2月25日
 */
public interface IAccountingService extends BaseService<AccountingBo, AccountingVo> {

	/**
	 * 个人核算 - 查看当天个人核算列表功能
	 */
	Pagination<OrderVo> getPage(AccountingBo accountingBo);

	/**
	 * //查询收银核算信息
	 * 
	 * @param accountingBo
	 * @return @
	 */
	@Override
	AccountingVo get(AccountingBo accountingBo);

	/**
	 * 计算
	 * 
	 * @param accountingBo
	 * @return @
	 */
	Double getUserDayMoney(AccountingBo accountingBo);

	/**
	 * 保存个人核账信息
	 * 
	 * @param accountingBo
	 * @return @
	 */
	@Override
	AccountingVo add(AccountingBo accountingBo);

	@Override
	String del(AccountingBo accountingBo);

	/**
	 * 记录工单异常
	 * 
	 * @param markOrderBo
	 * @
	 */
	void saveMarkOrder(MarkOrderBo markOrderBo);

	/**
	 * 删除工单异常
	 * 
	 * @param markOrderBo
	 */
	void deleteMark(MarkOrderBo markOrderBo);
}
