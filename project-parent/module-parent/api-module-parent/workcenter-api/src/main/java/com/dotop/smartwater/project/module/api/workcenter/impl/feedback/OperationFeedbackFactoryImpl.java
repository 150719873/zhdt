package com.dotop.smartwater.project.module.api.workcenter.impl.feedback;

import java.util.Date;
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
import com.dotop.smartwater.project.module.core.auth.local.AuthCasClient;
import com.dotop.smartwater.project.module.core.auth.vo.UserVo;
import com.dotop.smartwater.project.module.core.water.bo.DataCalibrationBo;
import com.dotop.smartwater.project.module.core.water.bo.OperationLogBo;
import com.dotop.smartwater.project.module.core.water.bo.customize.WorkCenterFeedbackBo;
import com.dotop.smartwater.project.module.service.operation.IDataCalibrationService;
import com.dotop.smartwater.project.module.service.operation.IOperationLogService;

@Component("OperationFeedbackFactoryImpl")
public class OperationFeedbackFactoryImpl implements IWorkCenterFeedbackFactory {

	private static final Logger LOGGER = LogManager.getLogger(OperationFeedbackFactoryImpl.class);

	@Autowired
	private IOperationLogService iOperationLogService;

	@Autowired
	private IDataCalibrationService iDataCalibrationService;

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public void add(WorkCenterFeedbackBo feedbackBo) {
		LOGGER.info(feedbackBo);
		UserVo user = AuthCasClient.getUser();
		if (!CollectionUtils.isEmpty(feedbackBo.getCarryParams())) {
			if (feedbackBo.getCarryParams().containsKey("type")) {
				if ("OperationLog".equals(feedbackBo.getCarryParams().get("type"))) {
					if (feedbackBo.getSqlParams().containsKey("ids")) {
						String idsStr = feedbackBo.getSqlParams().get("ids");
						List<String> ids = JSONUtils.parseArray(idsStr, String.class);
						for (String id : ids) {
							OperationLogBo operationLogBo = new OperationLogBo();
							operationLogBo.setId(id);
							operationLogBo.setStatus(2);
							operationLogBo.setBusinessId(feedbackBo.getBusinessId());
							iOperationLogService.edit(operationLogBo);
						}
					}
				} else if ("DataCalibration".equals(feedbackBo.getCarryParams().get("type"))) {
					if (!CollectionUtils.isEmpty(feedbackBo.getFillParams())) {
						DataCalibrationBo dataCalibrationBo = new DataCalibrationBo();
						if (feedbackBo.getFillParams().containsKey("type")) {
							dataCalibrationBo.setId(feedbackBo.getBusinessId());
							dataCalibrationBo.setStatus(1);
							dataCalibrationBo.setEnterpriseid(user.getEnterpriseid());
							dataCalibrationBo
									.setType(Integer.valueOf(feedbackBo.getFillParams().get("type").split(",")[2]));
							dataCalibrationBo.setCreateBy(user.getName());
							dataCalibrationBo.setCreateDate(new Date());
							iDataCalibrationService.edit(dataCalibrationBo);
						}
					}
				}
			}
		}

		LOGGER.info("OperationFeedbackFactoryImpl add ...");
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public void exchange(WorkCenterFeedbackBo feedbackBo) {
		LOGGER.info(feedbackBo);
		LOGGER.info("OperationFeedbackFactoryImpl exchange ...");
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public void end(WorkCenterFeedbackBo feedbackBo) {
		LOGGER.info(feedbackBo);
		if (!CollectionUtils.isEmpty(feedbackBo.getCarryParams())) {
			if (feedbackBo.getCarryParams().containsKey("type")) {
				if ("OperationLog".equals(feedbackBo.getCarryParams().get("type"))) {
					if (feedbackBo.getSqlParams().containsKey("ids")) {
						String idsStr = feedbackBo.getSqlParams().get("ids");
						List<String> ids = JSONUtils.parseArray(idsStr, String.class);
						for (String id : ids) {
							OperationLogBo operationLogBo = new OperationLogBo();
							operationLogBo.setId(id);
							operationLogBo.setStatus(3);
							iOperationLogService.edit(operationLogBo);
						}
					}
				} else if ("DataCalibration".equals(feedbackBo.getCarryParams().get("type"))) {
					if (!CollectionUtils.isEmpty(feedbackBo.getFillParams())) {
						DataCalibrationBo dataCalibrationBo = new DataCalibrationBo();
						if (feedbackBo.getFillParams().containsKey("type")) {
							dataCalibrationBo.setId(feedbackBo.getBusinessId());
							dataCalibrationBo.setStatus(2);
							iDataCalibrationService.edit(dataCalibrationBo);
						}
					}
				}
			}
		}
		LOGGER.info("OperationFeedbackFactoryImpl end ...");
	}
}
