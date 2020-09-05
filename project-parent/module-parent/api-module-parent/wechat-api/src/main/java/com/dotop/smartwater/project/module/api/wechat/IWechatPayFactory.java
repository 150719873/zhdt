package com.dotop.smartwater.project.module.api.wechat;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.dotop.smartwater.dependence.core.common.BaseFactory;
import com.dotop.smartwater.dependence.core.common.BaseForm;
import com.dotop.smartwater.dependence.core.common.BaseVo;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.project.module.core.water.form.customize.OrderPayParamForm;
import com.dotop.smartwater.project.module.core.water.form.customize.WechatParamForm;
import com.dotop.smartwater.project.module.core.water.vo.customize.JSApiTicketVo;

public interface IWechatPayFactory extends BaseFactory<BaseForm, BaseVo> {

	JSApiTicketVo jSApiTicket(HttpServletRequest request);

	/**
	 * 检测账单状态
	 * 
	 * @param wechatParamForm
	 * @throws FrameworkRuntimeException
	 */
	void checkOrderStatus(WechatParamForm wechatParamForm);

	/**
	 * 缴费
	 * 
	 * @param orderPayParamForm
	 * @return
	 * @throws FrameworkRuntimeException
	 */
	Map<String, Object> orderPayment(HttpServletRequest request, OrderPayParamForm orderPayParamForm);

	/**
	 * 再次查询订单状态
	 * 
	 * @param wechatParamForm
	 * @throws FrameworkRuntimeException
	 */
	void orderQuery(WechatParamForm wechatParamForm);

	/**
	 * 请求出错 请求撤单
	 * 
	 * @param wechatParamForm
	 * @throws FrameworkRuntimeException
	 */
	void orderError(WechatParamForm wechatParamForm);

}
