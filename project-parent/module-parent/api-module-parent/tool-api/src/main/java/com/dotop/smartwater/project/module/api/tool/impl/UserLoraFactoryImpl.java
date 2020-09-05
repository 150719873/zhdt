package com.dotop.smartwater.project.module.api.tool.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.utils.BeanUtils;
import com.dotop.smartwater.project.module.api.tool.IUserLoraFactory;
import com.dotop.smartwater.project.module.core.auth.bo.UserLoraBo;
import com.dotop.smartwater.project.module.core.auth.form.UserLoraForm;
import com.dotop.smartwater.project.module.core.auth.local.IAuthCasClient;
import com.dotop.smartwater.project.module.core.auth.vo.UserLoraVo;
import com.dotop.smartwater.project.module.service.tool.IUserLoraService;

@Component
public class UserLoraFactoryImpl implements IUserLoraFactory, IAuthCasClient {

	@Resource
	private IUserLoraService iUserLoraService;

	@Override
	public UserLoraVo findByEnterpriseId(String eid)  {
		return iUserLoraService.findByEnterpriseId(eid);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public void saveUserlora(UserLoraForm userloraForm)  {
		UserLoraVo lora = findByEnterpriseId(userloraForm.getEnterpriseid());
		UserLoraBo userloraBo = BeanUtils.copy(userloraForm, UserLoraBo.class);
		if (lora == null) {
			iUserLoraService.add(userloraBo);
		} else {
			iUserLoraService.edit(userloraBo);
		}
	}

}
