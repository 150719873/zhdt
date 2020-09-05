package com.dotop.smartwater.project.module.api.workcenter.impl.build;

import com.dotop.smartwater.dependence.cache.api.AbstractValueCache;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.project.module.api.workcenter.IWorkCenterBuildFactory;
import com.dotop.smartwater.project.module.core.water.bo.customize.WorkCenterBuildBo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("DeviceWarningBuildFactoryImpl")
public class DeviceWarningBuildFactoryImpl implements IWorkCenterBuildFactory {
	private  static final Logger LOGGER = LogManager.getLogger(OperationBuildFactoryImpl.class);

	@Autowired
	private AbstractValueCache<WorkCenterBuildBo> avc;

	@Override
	public AbstractValueCache<WorkCenterBuildBo> getCache() {
		return avc;
	}

	@Override
	public void build(WorkCenterBuildBo buildBo) {
		LOGGER.info(LogMsg.to("buildBo", buildBo));
	}
}
