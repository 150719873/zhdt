package com.dotop.smartwater.project.module.api.revenue;

import com.dotop.smartwater.dependence.core.common.BaseFactory;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.project.module.core.water.bo.TradeDetailBo;
import com.dotop.smartwater.project.module.core.water.form.TradeDetailForm;
import com.dotop.smartwater.project.module.core.water.form.TradeOrderForm;
import com.dotop.smartwater.project.module.core.water.vo.TradeDetailVo;
import com.dotop.smartwater.project.module.core.water.vo.TradeOrderVo;

public interface ITradeOrderFactory extends BaseFactory<TradeOrderForm, TradeOrderVo> {

	/**
	 * 费用管理分页查询
	 */
	Pagination<TradeOrderVo> page(TradeOrderForm form);

	/**
	 * 添加费用信息
	 */
	int save(TradeOrderForm form);

	/**
	 * 获取缴费详情
	 */
	TradeOrderVo get(TradeOrderForm form);

	/**
	 * 获取交易详情
	 * 
	 * @param tform
	 * @return
	 */
	TradeDetailVo getDetail(TradeDetailForm tform);

	/**
	 * 修改费用信息
	 */
	int update(TradeOrderForm form);

	/**
	 * 更新交易状态
	 */
	boolean updatePayStatus(TradeOrderForm form);

	/**
	 * 缴费
	 */
	TradeDetailBo tradeOrderPay(TradeDetailForm form);

}
