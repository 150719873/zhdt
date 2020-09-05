package com.dotop.smartwater.project.module.api.workcenter.impl.feedback;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.utils.JSONUtils;
import com.dotop.smartwater.project.module.api.workcenter.IWorkCenterFeedbackFactory;
import com.dotop.smartwater.project.module.core.water.bo.customize.WorkCenterFeedbackBo;
import com.dotop.smartwater.project.module.service.device.IDeviceWarningService;

@Component("DeviceWarningFeedbackFactoryImpl")
public class DeviceWarningFeedbackFactoryImpl implements IWorkCenterFeedbackFactory {

	private static final Logger LOGGER = LogManager.getLogger(DeviceWarningFeedbackFactoryImpl.class);

	@Autowired
	private IDeviceWarningService iDeviceWarningService;

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public void add(WorkCenterFeedbackBo feedbackBo) {
		LOGGER.info(feedbackBo);
		if (!CollectionUtils.isEmpty(feedbackBo.getSqlParams()) && feedbackBo.getSqlParams().containsKey("ids")) {
			String ids = feedbackBo.getSqlParams().get("ids");
			List<String> idList = JSONUtils.parseArray(ids, String.class);
			iDeviceWarningService.handleWarning(feedbackBo.getUserBy(), idList);
		}
		LOGGER.info("DeviceWarningFeedbackFactoryImpl add ...");
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public void exchange(WorkCenterFeedbackBo feedbackBo) {
		LOGGER.info(feedbackBo);
		LOGGER.info("DeviceWarningFeedbackFactoryImpl exchange ...");
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public void end(WorkCenterFeedbackBo feedbackBo) {
		LOGGER.info(feedbackBo);
		LOGGER.info("DeviceWarningFeedbackFactoryImpl end ...");
	}
}
