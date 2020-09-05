package com.dotop.smartwater.project.module.api.wechat;

import com.dotop.smartwater.dependence.core.common.BaseFactory;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.project.module.core.water.form.customize.WechatParamForm;
import com.dotop.smartwater.project.module.core.water.vo.PayDetailVo;

public interface IBalanceChangeFactory extends BaseFactory<WechatParamForm, PayDetailVo> {

	@Override
	Pagination<PayDetailVo> page(WechatParamForm wechatParamForm);

}
