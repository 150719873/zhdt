package com.dotop.smartwater.project.module.api.wechat;

import com.dotop.smartwater.dependence.core.common.BaseFactory;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.project.module.core.water.form.customize.WechatParamForm;
import com.dotop.smartwater.project.module.core.water.vo.CouponVo;

public interface IWechatCouponFactory extends BaseFactory<WechatParamForm, CouponVo> {

	@Override
	Pagination<CouponVo> page(WechatParamForm wechatParamForm);

}
