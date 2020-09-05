package com.dotop.smartwater.project.module.api.revenue;

import com.dotop.smartwater.dependence.core.common.BaseFactory;
import com.dotop.smartwater.project.module.core.water.form.WrongAccountForm;
import com.dotop.smartwater.project.module.core.water.vo.WrongAccountVo;

public interface IWrongAccountFactory extends BaseFactory<WrongAccountForm, WrongAccountVo> {

	WrongAccountVo update(WrongAccountForm wrongAccountForm);

	void complete(WrongAccountForm wrongAccountForm);

	void cancel(WrongAccountForm wrongAccountForm);

	void addCoupon(WrongAccountForm wrongAccountForm);

}
