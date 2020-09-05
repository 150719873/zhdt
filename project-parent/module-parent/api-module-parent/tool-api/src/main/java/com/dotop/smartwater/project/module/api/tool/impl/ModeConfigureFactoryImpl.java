package com.dotop.smartwater.project.module.api.tool.impl;

import java.util.ArrayList;
import java.util.List;

import com.dotop.smartwater.project.module.api.tool.IModeConfigureFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dotop.smartwater.dependence.core.utils.UuidUtils;
import com.dotop.smartwater.project.module.core.auth.local.AuthCasClient;
import com.dotop.smartwater.project.module.core.auth.vo.UserVo;
import com.dotop.smartwater.project.module.core.water.bo.ModeBindBo;
import com.dotop.smartwater.project.module.core.water.form.ModeBindForm;
import com.dotop.smartwater.project.module.core.water.vo.ModeBindVo;
import com.dotop.smartwater.project.module.service.tool.IModeConfigureService;

/**
 * 

 * @description 通讯方式配置
 * @date 2019-10-17 15:31
 *
 */
@Component
public class ModeConfigureFactoryImpl implements IModeConfigureFactory {

	@Autowired
	private IModeConfigureService iModeConfigureService;
	
	@Override
	public String configureMode(List<ModeBindForm> modeBindForms) {
		// TODO Auto-generated method stub
		UserVo user = AuthCasClient.getUser();
		
		List<ModeBindBo> modeBindBos = new ArrayList<ModeBindBo>();
		for(ModeBindForm modeBindForm: modeBindForms) {
	        ModeBindBo modeBindBo = new ModeBindBo();
	        BeanUtils.copyProperties(modeBindForm, modeBindBo);
	        modeBindBo.setId(UuidUtils.getUuid());
	        modeBindBo.setEnterpriseid(user.getEnterpriseid());
	        modeBindBos.add(modeBindBo);
		}
		return iModeConfigureService.configureMode(modeBindBos);
	}

	@Override
	public List<ModeBindVo> listModeConfigure(ModeBindForm modeBindForm) {
		// TODO Auto-generated method stub
		UserVo user = AuthCasClient.getUser();
		ModeBindBo modeBindBo = new ModeBindBo();
        BeanUtils.copyProperties(modeBindForm, modeBindBo);
        modeBindBo.setEnterpriseid(user.getEnterpriseid());
        
		return iModeConfigureService.listModeConfigure(modeBindBo);
	}

}
