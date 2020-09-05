package com.dotop.smartwater.project.module.api.workcenter.impl.feedback;

import java.util.Date;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.project.module.api.workcenter.IWorkCenterFeedbackFactory;
import com.dotop.smartwater.project.module.core.auth.local.AuthCasClient;
import com.dotop.smartwater.project.module.core.auth.vo.UserVo;
import com.dotop.smartwater.project.module.core.water.bo.BillBadBo;
import com.dotop.smartwater.project.module.core.water.bo.BillCheckBo;
import com.dotop.smartwater.project.module.core.water.bo.customize.WorkCenterFeedbackBo;
import com.dotop.smartwater.project.module.core.water.constants.DictionaryCode;
import com.dotop.smartwater.project.module.core.water.constants.WaterConstants;
import com.dotop.smartwater.project.module.service.revenue.IBillBadService;
import com.dotop.smartwater.project.module.service.revenue.IBillCheckService;

@Component("FinanceFeedBackFactoryImpl")
public class FinanceFeedBackFactoryImpl implements IWorkCenterFeedbackFactory {

	@Autowired
	private IBillCheckService iBillCheckService;

	@Autowired
	private IBillBadService iBillBadService;

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public void add(WorkCenterFeedbackBo workCenterFeedbackBo) throws FrameworkRuntimeException {

		UserVo user = AuthCasClient.getUser();
		String operEid = user.getEnterpriseid();
		Date curr = new Date();
		BillCheckBo billCheckBo = new BillCheckBo();
		// 业务id
		billCheckBo.setBillCheckId(workCenterFeedbackBo.getBusinessId());
		billCheckBo.setEnterpriseid(operEid);
		billCheckBo.setCurr(curr);
		billCheckBo.setUserBy(user.getName());
		BillBadBo billBadBo = new BillBadBo();
		BeanUtils.copyProperties(billCheckBo, billBadBo);
		billCheckBo.setProcessId(workCenterFeedbackBo.getProcessId());
		// TODO 需要流程状态 workCenterFeedbackBo.get
		billCheckBo.setProcessStatus(workCenterFeedbackBo.getProcessStatus()); // 已申请 APPLY
		billCheckBo.setProcessCreateBy(user.getName());
		// 发起工单
		// 更新对账账单信息
		iBillCheckService.edit(billCheckBo);
		// 更新状态 坏账流程id
		billBadBo.setProcessId(workCenterFeedbackBo.getProcessId());
		// 更新坏账账单的流程id
		iBillBadService.edit(billBadBo);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public void exchange(WorkCenterFeedbackBo workCenterFeedbackBo) throws FrameworkRuntimeException {
		// 处理中 状态是2
		UserVo user = AuthCasClient.getUser();
		String operEid = user.getEnterpriseid();
		Date curr = new Date();
		BillCheckBo billCheckBo = new BillCheckBo();
		// 业务id
		billCheckBo.setBillCheckId(workCenterFeedbackBo.getBusinessId());
		billCheckBo.setEnterpriseid(operEid);
		billCheckBo.setCurr(curr);
		billCheckBo.setUserBy(user.getName());
		if (WaterConstants.WORK_CENTER_PROCESS_HANDLE.equals(workCenterFeedbackBo.getProcessStatus())) {
			// 处理中
			billCheckBo.setProcessStatus(workCenterFeedbackBo.getProcessStatus()); // 已申请 HANDLE
			// 更新流程状态
			iBillCheckService.edit(billCheckBo);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public void end(WorkCenterFeedbackBo feedbackBo) throws FrameworkRuntimeException {
		// 处理结束 5
		UserVo user = AuthCasClient.getUser();
		String operEid = user.getEnterpriseid();
		Date curr = new Date();
		BillCheckBo billCheckBo = new BillCheckBo();
		// 业务id
		billCheckBo.setBillCheckId(feedbackBo.getBusinessId());
		billCheckBo.setEnterpriseid(operEid);
		billCheckBo.setCurr(curr);
		billCheckBo.setUserBy(user.getName());

		BillBadBo billBadBo = new BillBadBo();
		billBadBo.setEnterpriseid(operEid);
		billBadBo.setUserBy(user.getName());
		billBadBo.setCurr(curr);
		billBadBo.setBillCheckId(feedbackBo.getBusinessId());
		billBadBo.setProcessId(feedbackBo.getProcessId());

		if (WaterConstants.WORK_CENTER_PROCESS_OVER.equals(feedbackBo.getProcessStatus())) {
			// 流程是否定的结束
			if (WaterConstants.DICTIONARY_0.equals(DictionaryCode.getChildValue(feedbackBo.getHandleDictChildId()))) {
				// 流程是否定的结束 标记审批状态 为失败
				billBadBo.setProcessStatus(WaterConstants.WORK_CENTER_PROCESS_ERROR_OVER); // 审批失败的结束工单
				iBillBadService.editProcessOver(billBadBo);
				billCheckBo.setProcessStatus(WaterConstants.WORK_CENTER_PROCESS_ERROR_OVER); // 审批失败的结束工单
				iBillCheckService.edit(billCheckBo);

			} else if (WaterConstants.DICTIONARY_1
					.equals(DictionaryCode.getChildValue(feedbackBo.getHandleDictChildId()))) {
				// 流程审批通过
				billCheckBo.setProcessStatus(WaterConstants.WORK_CENTER_PROCESS_SUCCESS_OVER);
				iBillCheckService.edit(billCheckBo);
				billBadBo.setProcessStatus(WaterConstants.WORK_CENTER_PROCESS_SUCCESS_OVER);
				iBillBadService.editProcessOver(billBadBo);
			}
		}
	}
}
