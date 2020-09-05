package com.dotop.smartwater.project.module.dao.revenue;

import com.dotop.smartwater.dependence.core.common.BaseDao;
import com.dotop.smartwater.project.module.core.water.dto.AccountingDto;
import com.dotop.smartwater.project.module.core.water.dto.MarkOrderDto;
import com.dotop.smartwater.project.module.core.water.vo.AccountingVo;
import com.dotop.smartwater.project.module.core.water.vo.OrderVo;

import java.util.List;

/**

 * @date 2019年2月25日
 */
public interface IAccountingDao extends BaseDao<AccountingDto, AccountingVo> {

	List<OrderVo> getList(AccountingDto accountingDto);

	/**
	 * 查询收银核算信息
	 *
	 * @param accountingDto
	 * @return
	 */
	@Override
	AccountingVo get(AccountingDto accountingDto);

	/**
	 * 计算数据
	 *
	 * @param accountingDto
	 * @return
	 */
	Double getUserDayMoney(AccountingDto accountingDto);

	/**
	 * 保存核算信息
	 *
	 * @param accountingDto
	 * @return
	 * @
	 */
	@Override
	void add(AccountingDto accountingDto);

	/**
	 * 删除数据
	 */
	@Override
	Integer del(AccountingDto accountingDto);

	/**
	 * 删除工单异常
	 *
	 * @param markOrderDto
	 * @
	 */
	Integer delMarkOrder(MarkOrderDto markOrderDto);

	/**
	 * 记录工单异常
	 *
	 * @param markOrderDto
	 * @
	 */
	void saveMarkOrder(MarkOrderDto markOrderDto);

	/**
	 * 删除工单异常
	 *
	 * @param markOrderDto
	 * @return
	 * @
	 */
	Integer deleteMark(MarkOrderDto markOrderDto);

}
