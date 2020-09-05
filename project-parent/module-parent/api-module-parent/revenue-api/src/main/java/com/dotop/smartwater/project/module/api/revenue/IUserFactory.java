package com.dotop.smartwater.project.module.api.revenue;

import com.dotop.smartwater.dependence.core.common.BaseFactory;
import com.dotop.smartwater.dependence.core.common.BaseForm;
import com.dotop.smartwater.dependence.core.common.BaseVo;
import com.dotop.smartwater.project.module.core.auth.form.UserForm;
import com.dotop.smartwater.project.module.core.auth.form.UserLoginForm;
import com.dotop.smartwater.project.module.core.auth.vo.EnterpriseVo;
import com.dotop.smartwater.project.module.core.third.form.iot.UserEntryForm;

import java.util.List;
import java.util.Map;

/**
 * user
 * 

 * @date 2019年2月27日
 */
public interface IUserFactory extends BaseFactory<BaseForm, BaseVo> {

	void logout(String userid,String ticket);

	String login(UserEntryForm userEntryForm);

	Map<String, Object> childrenLogin(Map<String, String> map);

	String getCasKey(UserLoginForm userLogin);

	String checkUserInvalid(UserForm user);

	List<EnterpriseVo> loginEnterprise();

}
