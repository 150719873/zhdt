package com.dotop.smartwater.project.module.api.wechat;

import com.dotop.smartwater.dependence.core.common.BaseFactory;
import com.dotop.smartwater.dependence.core.common.BaseVo;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.project.module.core.water.form.customize.WechatParamForm;
import com.dotop.smartwater.project.module.core.water.vo.OrderVo;
import com.dotop.smartwater.project.module.core.water.vo.PaymentTradeOrderVo;
import com.dotop.smartwater.project.module.core.water.vo.customize.QueryBillVo;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

public interface IWechatBillFactory extends BaseFactory<WechatParamForm, BaseVo> {

	/**
	 * 查询未缴费账单列表
	 * 
	 * @param request
	 * @param wechatParamForm
	 * @return
	 * @throws FrameworkRuntimeException
	 */
	Pagination<PaymentTradeOrderVo> page(HttpServletRequest request, WechatParamForm wechatParamForm);

	/**
	 * 查询已缴费账单列表
	 * 
	 * @param request
	 * @param wechatParamForm
	 * @return
	 * @throws FrameworkRuntimeException
	 */
	Pagination<OrderVo> queryPaybill(HttpServletRequest request, WechatParamForm wechatParamForm);

	/**
	 * 查询过去12个月的账单
	 * 
	 * @param request
	 * @param wechatParamForm
	 * @return
	 * @throws FrameworkRuntimeException
	 */
	List<QueryBillVo> getOrderTrend(HttpServletRequest request, WechatParamForm wechatParamForm);

	/**
	 * 账单的账单记录
	 * 
	 * @param request
	 * @param wechatParamForm
	 * @return
	 * @throws FrameworkRuntimeException
	 */
	Pagination<OrderVo> getOrderList(HttpServletRequest request, WechatParamForm wechatParamForm);

	/**
	 * 根据账单流水号 查询账单详情
	 * 
	 * @param tradeno
	 * @return
	 * @throws FrameworkRuntimeException
	 */
	Map<String, Object> getbillDetail(String tradeno);

}
