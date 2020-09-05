package com.dotop.smartwater.project.module.api.workcenter.impl.build;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dotop.smartwater.dependence.cache.api.AbstractValueCache;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.project.module.api.workcenter.IWorkCenterBuildFactory;
import com.dotop.smartwater.project.module.core.water.bo.customize.WorkCenterBuildBo;

@Component("TestBuildFactoryImpl")
public class TestBuildFactoryImpl implements IWorkCenterBuildFactory {

	private static final Logger logger = LogManager.getLogger(TestBuildFactoryImpl.class);

	@Autowired
	private AbstractValueCache<WorkCenterBuildBo> avc;

	@Override
	public AbstractValueCache<WorkCenterBuildBo> getCache() throws FrameworkRuntimeException {
		return avc;
	}

	@Override
	public void build(WorkCenterBuildBo buildBo) throws FrameworkRuntimeException {
		logger.info(LogMsg.to("buildBo", buildBo));
	}

}
