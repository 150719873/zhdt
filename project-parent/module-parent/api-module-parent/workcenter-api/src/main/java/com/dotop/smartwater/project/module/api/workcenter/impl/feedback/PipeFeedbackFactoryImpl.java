package com.dotop.smartwater.project.module.api.workcenter.impl.feedback;

import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.project.module.api.workcenter.IWorkCenterFeedbackFactory;
import com.dotop.smartwater.project.module.client.third.fegin.pipe.IPipeFeginClient;
import com.dotop.smartwater.project.module.core.auth.local.AuthCasClient;
import com.dotop.smartwater.project.module.core.auth.vo.UserVo;
import com.dotop.smartwater.project.module.core.third.bo.pipe.PipeWorkOrder;
import com.dotop.smartwater.project.module.core.water.bo.customize.WorkCenterFeedbackBo;
import com.dotop.smartwater.project.module.core.water.constants.DictionaryCode;
import com.dotop.smartwater.project.module.core.water.constants.WaterConstants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component("PipeFeedBackFactoryImpl")
public class PipeFeedbackFactoryImpl implements IWorkCenterFeedbackFactory {

	private static final Logger logger = LogManager.getLogger(PipeFeedbackFactoryImpl.class);

	@Autowired
	IPipeFeginClient pipeFeginClient;

	@Override
	public void add(WorkCenterFeedbackBo feedbackBo) throws FrameworkRuntimeException {
		UserVo user = AuthCasClient.getUser();
		String operEid = user.getEnterpriseid();
		Date curr = new Date();
		PipeWorkOrder pipeWorkOrder = new PipeWorkOrder();
		// 业务id
		pipeWorkOrder.setProcessId(feedbackBo.getProcessId());
		pipeWorkOrder.setWorkOrderId(feedbackBo.getBusinessId());
		pipeWorkOrder.setEnterpriseId(operEid);
		pipeWorkOrder.setCurr(curr);
		pipeWorkOrder.setUserBy(user.getName());
		pipeWorkOrder.setStatus("-1");
		// 发起工单
		// 更新工单状态
		pipeFeginClient.edit(pipeWorkOrder);
		logger.info(LogMsg.to("feedbackBo", feedbackBo));
	}

	@Override
	public void exchange(WorkCenterFeedbackBo feedbackBo) throws FrameworkRuntimeException {
		// 处理中 状态是2
		UserVo user = AuthCasClient.getUser();
		String operEid = user.getEnterpriseid();
		Date curr = new Date();
		PipeWorkOrder pipeWorkOrder = new PipeWorkOrder();
		// 业务id
		pipeWorkOrder.setWorkOrderId(feedbackBo.getBusinessId());
		pipeWorkOrder.setEnterpriseId(operEid);
		pipeWorkOrder.setCurr(curr);
		pipeWorkOrder.setUserBy(user.getName());
		if (WaterConstants.WORK_CENTER_PROCESS_HANDLE.equals(feedbackBo.getProcessStatus())) {
			// 处理中
			pipeWorkOrder.setStatus("0");
			// 更新工单状态
			pipeFeginClient.edit(pipeWorkOrder);
		} else if (WaterConstants.WORK_CENTER_PROCESS_HANG.equals(feedbackBo.getProcessStatus())) {
			// 挂起
			pipeWorkOrder.setStatus("2");
			// 更新工单状态
			pipeFeginClient.edit(pipeWorkOrder);
		}
		logger.info(LogMsg.to("feedbackBo", feedbackBo));
	}

	@Override
	public void end(WorkCenterFeedbackBo feedbackBo) throws FrameworkRuntimeException {
		UserVo user = AuthCasClient.getUser();
		String operEid = user.getEnterpriseid();
		Date curr = new Date();
		PipeWorkOrder pipeWorkOrder = new PipeWorkOrder();
		// 业务id
		pipeWorkOrder.setWorkOrderId(feedbackBo.getBusinessId());
		pipeWorkOrder.setEnterpriseId(operEid);
		pipeWorkOrder.setCurr(curr);
		pipeWorkOrder.setUserBy(user.getName());
		if (WaterConstants.WORK_CENTER_PROCESS_OVER.equals(feedbackBo.getProcessStatus())) {
			// 流程是否定的结束
			if (WaterConstants.DICTIONARY_0.equals(DictionaryCode.getChildValue(feedbackBo.getHandleDictChildId()))) {
				// 流程是否定的结束 标记审批状态 为失败
				// 处理中
				pipeWorkOrder.setStatus("1");
				pipeWorkOrder.setResult("不通过");
				// 更新工单状态
				pipeFeginClient.edit(pipeWorkOrder);

			} else if (WaterConstants.DICTIONARY_1
					.equals(DictionaryCode.getChildValue(feedbackBo.getHandleDictChildId()))) {
				// 流程审批通过
				pipeWorkOrder.setStatus("1");
				pipeWorkOrder.setResult("通过");
				// 更新工单状态
				pipeFeginClient.edit(pipeWorkOrder);
			}
		}
		logger.info(LogMsg.to("feedbackBo", feedbackBo));
	}

}
