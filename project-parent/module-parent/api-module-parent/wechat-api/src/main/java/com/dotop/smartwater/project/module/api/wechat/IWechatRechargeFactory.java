package com.dotop.smartwater.project.module.api.wechat;

import com.dotop.smartwater.dependence.core.common.BaseFactory;
import com.dotop.smartwater.dependence.core.common.BaseVo;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.project.module.core.water.form.customize.RechargeForm;
import com.dotop.smartwater.project.module.core.water.form.customize.WechatParamForm;
import com.dotop.smartwater.project.module.core.water.vo.PayDetailVo;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public interface IWechatRechargeFactory extends BaseFactory<WechatParamForm, BaseVo> {

	Map<String, Object> recharge(HttpServletRequest request, RechargeForm rechargeForm);

	void query(HttpServletRequest request, RechargeForm rechargeForm);

	Pagination<PayDetailVo> getRechargeLit(HttpServletRequest request, RechargeForm rechargeForm);

}
