package com.dotop.smartwater.project.auth.api.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.utils.BeanUtils;
import com.dotop.smartwater.project.auth.api.IAreaFactory;
import com.dotop.smartwater.project.auth.service.IAreaService;
import com.dotop.smartwater.project.module.core.auth.bo.AreaBo;
import com.dotop.smartwater.project.module.core.auth.form.AreaForm;
import com.dotop.smartwater.project.module.core.auth.vo.AreaListVo;
import com.dotop.smartwater.project.module.core.auth.vo.AreaVo;
import com.dotop.smartwater.project.module.core.auth.vo.PermissionVo;

@Component
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
public class AreaFactoryImpl implements IAreaFactory {

	@Autowired
	private IAreaService iAreaService;

	@Override
	public String getMaxId() throws FrameworkRuntimeException {
		return iAreaService.getMaxId();
	}

	@Override
	public List<AreaVo> loadCompanyArea(String enterpriseid) throws FrameworkRuntimeException {
		return iAreaService.loadCompanyArea(enterpriseid);
	}

	@Override
	public Integer insertAreaNode(AreaForm areaForm) throws FrameworkRuntimeException {
		AreaBo areaBo = BeanUtils.copy(areaForm, AreaBo.class);
		return iAreaService.insertAreaNode(areaBo);
	}
	
	@Override
	public Integer updateAreaNode(AreaForm areaForm) throws FrameworkRuntimeException {
		AreaBo areaBo = BeanUtils.copy(areaForm, AreaBo.class);
		return iAreaService.updateAreaNode(areaBo);
	}
	
	@Override
	public Integer deleteAreaNode(AreaForm areaForm) throws FrameworkRuntimeException {
		AreaBo areaBo = BeanUtils.copy(areaForm, AreaBo.class);
		return iAreaService.deleteAreaNode(areaBo);
	}
	
	@Override
	public Integer saveCompanyArea(AreaListVo areaVo) throws FrameworkRuntimeException {
		return iAreaService.saveCompanyArea(areaVo);
	}

	@Override
	public String isUsedNode(AreaVo area) throws FrameworkRuntimeException {
		return iAreaService.isUsedNode(area);
	}

	@Override
	public List<PermissionVo> findAreasByEidAndUseId(String enterpriseid, String userid)
			throws FrameworkRuntimeException {
		return iAreaService.findAreasByEidAndUseId(enterpriseid, userid);
	}

}
