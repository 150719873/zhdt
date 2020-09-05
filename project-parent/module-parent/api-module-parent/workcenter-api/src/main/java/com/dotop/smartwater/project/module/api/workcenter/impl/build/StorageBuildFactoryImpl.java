package com.dotop.smartwater.project.module.api.workcenter.impl.build;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dotop.smartwater.dependence.cache.api.AbstractValueCache;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.project.module.api.workcenter.IWorkCenterBuildFactory;
import com.dotop.smartwater.project.module.core.auth.local.IAuthCasClient;
import com.dotop.smartwater.project.module.core.water.bo.customize.WorkCenterBuildBo;

@Component("StorageBuildFactoryImpl")
public class StorageBuildFactoryImpl implements IWorkCenterBuildFactory, IAuthCasClient {

	private static final Logger logger = LogManager.getLogger(StorageBuildFactoryImpl.class);

	@Autowired
	private AbstractValueCache<WorkCenterBuildBo> avc;

	@Override
	public AbstractValueCache<WorkCenterBuildBo> getCache() throws FrameworkRuntimeException {
		// TODO Auto-generated method stub
		return avc;
	}

	@Override
	public void build(WorkCenterBuildBo buildBo) throws FrameworkRuntimeException {
		// TODO Auto-generated method stub
		logger.info(LogMsg.to("buildBo", buildBo));
	}

}
