package com.dotop.smartwater.project.module.api.tool;

import com.dotop.smartwater.dependence.core.common.BaseFactory;
import com.dotop.smartwater.project.module.core.auth.form.UserForm;
import com.dotop.smartwater.project.module.core.auth.form.UserLoraForm;
import com.dotop.smartwater.project.module.core.auth.vo.UserLoraVo;
import com.dotop.smartwater.project.module.core.auth.vo.UserVo;

public interface IUserLoraFactory extends BaseFactory<UserForm, UserVo> {

	UserLoraVo findByEnterpriseId(String eid) ;

	void saveUserlora(UserLoraForm userloraForm) ;
}
