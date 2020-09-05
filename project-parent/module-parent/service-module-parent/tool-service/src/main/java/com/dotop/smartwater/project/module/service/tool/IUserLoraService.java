package com.dotop.smartwater.project.module.service.tool;

import com.dotop.smartwater.dependence.core.common.BaseService;
import com.dotop.smartwater.project.module.core.auth.bo.UserLoraBo;
import com.dotop.smartwater.project.module.core.auth.vo.UserLoraVo;

public interface IUserLoraService extends BaseService<UserLoraBo, UserLoraVo> {

	UserLoraVo findByEnterpriseId(String eid);

	@Override
	UserLoraVo add(UserLoraBo userLoraBo);

	@Override
	UserLoraVo edit(UserLoraBo userLoraBo);

}
