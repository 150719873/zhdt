package com.dotop.smartwater.project.auth.api.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.dotop.smartwater.project.auth.service.IInfService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.project.auth.api.IInfFactory;
import com.dotop.smartwater.project.module.core.auth.vo.AreaNodeVo;
import com.dotop.smartwater.project.module.core.auth.vo.AreaVo;
import com.dotop.smartwater.project.module.core.auth.vo.EnterpriseVo;
import com.dotop.smartwater.project.module.core.auth.vo.MenuVo;
import com.dotop.smartwater.project.module.core.auth.vo.SettlementVo;
import com.dotop.smartwater.project.module.core.auth.vo.UserVo;
import com.dotop.smartwater.project.module.core.auth.vo.select.Obj;

@Component
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
public class InfFactoryImpl implements IInfFactory {

	@Resource
	private IInfService iInfService;

	@Override
	public List<EnterpriseVo> getErpList() throws FrameworkRuntimeException {

		return iInfService.getErpList();
	}

	@Override
	public Map<String, EnterpriseVo> getEnterpriseMap() throws FrameworkRuntimeException {

		return iInfService.getEnterpriseMap();
	}

	@Override
	public Map<String, MenuVo> getPermissions(UserVo user, String modelid) throws FrameworkRuntimeException {

		return iInfService.getPermissions(user, modelid);
	}

	@Override
	public Map<String, AreaNodeVo> getAreas(String enterpriseid) throws FrameworkRuntimeException {

		return iInfService.getAreas(enterpriseid);
	}

	@Override
	public Map<String, AreaNodeVo> getAreasByUserId(String userid) throws FrameworkRuntimeException {

		return iInfService.getAreasByUserId(userid);
	}

	@Override
	public List<SettlementVo> getSettlements() throws FrameworkRuntimeException {

		return iInfService.getSettlements();
	}

	@Override
	public AreaVo getAreaById(String cid) throws FrameworkRuntimeException {

		return iInfService.getAreaById(cid);
	}

	@Override
	public Map<String, Obj> getOrganizationChartMap(String enterpriseid) throws FrameworkRuntimeException {
		return iInfService.getOrganizationChartMap(enterpriseid);
	}

}
