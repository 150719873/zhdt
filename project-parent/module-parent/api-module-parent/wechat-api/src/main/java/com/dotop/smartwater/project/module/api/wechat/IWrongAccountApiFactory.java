package com.dotop.smartwater.project.module.api.wechat;

import com.dotop.smartwater.dependence.core.common.BaseFactory;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.project.module.core.water.form.WrongAccountForm;
import com.dotop.smartwater.project.module.core.water.vo.WrongAccountVo;

public interface IWrongAccountApiFactory extends BaseFactory<WrongAccountForm, WrongAccountVo> {

	@Override
	Pagination<WrongAccountVo> page(WrongAccountForm wrongAccountForm);

	@Override
	WrongAccountVo add(WrongAccountForm wrongAccountForm);

}
