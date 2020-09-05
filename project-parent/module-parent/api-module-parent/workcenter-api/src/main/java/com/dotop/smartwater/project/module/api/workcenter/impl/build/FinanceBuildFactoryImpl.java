package com.dotop.smartwater.project.module.api.workcenter.impl.build;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dotop.smartwater.dependence.cache.api.AbstractValueCache;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.project.module.api.workcenter.IWorkCenterBuildFactory;
import com.dotop.smartwater.project.module.core.auth.local.AuthCasClient;
import com.dotop.smartwater.project.module.core.auth.vo.UserVo;
import com.dotop.smartwater.project.module.core.water.bo.customize.WorkCenterBuildBo;

@Component("FinanceBuildFactoryImpl")
public class FinanceBuildFactoryImpl implements IWorkCenterBuildFactory {

	@Autowired
	private AbstractValueCache<WorkCenterBuildBo> avc;

	@Override
	public AbstractValueCache<WorkCenterBuildBo> getCache() throws FrameworkRuntimeException {
		return avc;
	}

	@Override
	public void build(WorkCenterBuildBo buildBo) throws FrameworkRuntimeException {
		UserVo user = AuthCasClient.getUser();
		buildBo.getSqlParams().put("enterpriseid", user.getEnterpriseid());
		buildBo.getSqlParams().put("isBad", "1");
	}

}
