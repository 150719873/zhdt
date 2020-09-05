package com.dotop.smartwater.project.module.api.workcenter.impl.feedback;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.project.module.api.workcenter.IWorkCenterFeedbackFactory;
import com.dotop.smartwater.project.module.core.water.bo.customize.WorkCenterFeedbackBo;

@Component("TestFeedbackFactoryImpl")
public class TestFeedbackFactoryImpl implements IWorkCenterFeedbackFactory {

	private static final Logger logger = LogManager.getLogger(TestFeedbackFactoryImpl.class);

	@Override
	public void add(WorkCenterFeedbackBo feedbackBo) throws FrameworkRuntimeException {
		logger.info(LogMsg.to("feedbackBo", feedbackBo));
	}

	@Override
	public void exchange(WorkCenterFeedbackBo feedbackBo) throws FrameworkRuntimeException {
		logger.info(LogMsg.to("feedbackBo", feedbackBo));
	}

	@Override
	public void end(WorkCenterFeedbackBo feedbackBo) throws FrameworkRuntimeException {
		logger.info(LogMsg.to("feedbackBo", feedbackBo));
	}

}
