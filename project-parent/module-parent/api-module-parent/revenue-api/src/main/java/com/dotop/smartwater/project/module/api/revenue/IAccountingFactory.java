package com.dotop.smartwater.project.module.api.revenue;

import com.dotop.smartwater.dependence.core.common.BaseFactory;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.project.module.core.water.form.AccountingForm;
import com.dotop.smartwater.project.module.core.water.form.MarkOrderForm;
import com.dotop.smartwater.project.module.core.water.vo.AccountingVo;
import com.dotop.smartwater.project.module.core.water.vo.CommunityVo;
import com.dotop.smartwater.project.module.core.water.vo.OrderVo;

import java.util.List;

/**
 * 收银

 * @date 2019年2月25日
 */
public interface IAccountingFactory extends BaseFactory<AccountingForm, AccountingVo> {

	/**
	 * 收银分管区域
	 * 
	 * @return 区域
	 * @throws FrameworkRuntimeException
	 */
	List<CommunityVo> getCommunity();

	/**
	 * 个人核算 - 查看当天个人核算列表功能
	 * @param accountingForm 参数对象
	 * @return 分页
	 */
	Pagination<OrderVo> getPage(AccountingForm accountingForm);

	/**
	 * 收银员某天核算信息
	 * 
	 * @param accountingForm 参数对象
	 * @return 对象
	 * @throws FrameworkRuntimeException
	 */
	AccountingVo userDayMoney(AccountingForm accountingForm);

	/**
	 * 保存个人收银数据
	 * 
	 * @param accountingForm 参数对象
	 * @return 对象
	 * @throws FrameworkRuntimeException
	 */
	@Override
	AccountingVo add(AccountingForm accountingForm);

	/**
	 * 标记账单异常
	 * 
	 * @param markOrderForm 参数对象
	 * @throws FrameworkRuntimeException
	 */
	void markorder(MarkOrderForm markOrderForm);

}
