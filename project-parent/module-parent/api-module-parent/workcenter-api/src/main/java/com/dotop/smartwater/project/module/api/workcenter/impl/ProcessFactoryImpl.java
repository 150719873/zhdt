package com.dotop.smartwater.project.module.api.workcenter.impl;

import java.util.List;

import com.dotop.smartwater.project.module.api.workcenter.IProcessFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.dependence.core.utils.BeanUtils;
import com.dotop.smartwater.project.module.core.auth.local.IAuthCasClient;
import com.dotop.smartwater.project.module.core.water.bo.WorkCenterProcessBo;
import com.dotop.smartwater.project.module.core.water.bo.WorkCenterProcessMsgBo;
import com.dotop.smartwater.project.module.core.water.constants.WaterConstants;
import com.dotop.smartwater.project.module.core.water.form.WorkCenterProcessForm;
import com.dotop.smartwater.project.module.core.water.vo.WorkCenterProcessVo;
import com.dotop.smartwater.project.module.service.workcenter.IProcessMsgService;
import com.dotop.smartwater.project.module.service.workcenter.IProcessService;

@Component("IWorkCenterProcessFactory")
public class ProcessFactoryImpl implements IProcessFactory, IAuthCasClient {

	@Autowired
	private IProcessService iProcessService;

	@Autowired
	private IProcessMsgService iProcessMsgService;

	@Override
	public Pagination<WorkCenterProcessVo> page(WorkCenterProcessForm processForm) throws FrameworkRuntimeException {
		String status = processForm.getStatus();
		// 查询自己userid
		String userid = null;
		List<String> userids = null;
		List<String> roleids = null;
		if (getUsertype() == WaterConstants.USER_TYPE_ENTERPRISE_NORMAL) {
			userid = getUserid();
			// 查询自己userid
			userids = BeanUtils.list(getUserid());
			// 查询自己roleid
			roleids = BeanUtils.list(getRoleid());
		}
		// 根据当前人查看处理过的所有消息，返回对应的流程id，全表搜索，性能要优化
		WorkCenterProcessMsgBo processMsgBo = new WorkCenterProcessMsgBo();
		processMsgBo.setHandlers(userids);
		processMsgBo.setCarbonCopyers(roleids);
		processMsgBo.setHandlerRoles(userids);
		processMsgBo.setCarbonCopyerRoles(roleids);
		processMsgBo.setCompleter(getUserid());
		processMsgBo.setEnterpriseid(getEnterpriseid());
		List<String> processIds = iProcessMsgService.listProcessId(processMsgBo);

		WorkCenterProcessBo processBo = BeanUtils.copy(processForm, WorkCenterProcessBo.class);
		processBo.setEnterpriseid(getEnterpriseid());

		processBo.setStatus(status);
		processBo.setAssignHandler(getUserid());
		processBo.setApplicant(userid);
		processBo.setNextHandlers(userids);
		processBo.setNextCarbonCopyers(userids);
		processBo.setNextHandlerRoles(roleids);
		processBo.setNextCarbonCopyerRoles(roleids);
		processBo.setIds(processIds);
		return iProcessService.page(processBo);
	}

	@Override
	public Pagination<WorkCenterProcessVo> pageAgent(WorkCenterProcessForm processForm)
			throws FrameworkRuntimeException {
		List<String> statuss = processForm.getStatuss();
		// 查询自己userid
		List<String> userids = BeanUtils.list(getUserid());
		// 查询自己roleid
		List<String> roleids = BeanUtils.list(getRoleid());
		WorkCenterProcessBo processBo = new WorkCenterProcessBo();
		processBo.setPage(processForm.getPage());
		processBo.setPageCount(processForm.getPageCount());
		processBo.setEnterpriseid(getEnterpriseid());
		processBo.setStatuss(statuss);
		if (statuss.contains(WaterConstants.WORK_CENTER_PROCESS_HANG)
				||statuss.contains(WaterConstants.WORK_CENTER_PROCESS_OVER)) {
			processBo.setAssignHandler(getUserid());
		}
		processBo.setApplicant(getUserid());
		processBo.setNextHandlers(userids);
		processBo.setNextCarbonCopyers(userids);
		processBo.setNextHandlerRoles(roleids);
		processBo.setNextCarbonCopyerRoles(roleids);
		processBo.setStartDate(processForm.getStartDate());
		processBo.setEndDate(processForm.getEndDate());
		return iProcessService.page(processBo);

	}

	@Override
	public Pagination<WorkCenterProcessVo> pageBusiness(WorkCenterProcessForm processForm)
			throws FrameworkRuntimeException {
		WorkCenterProcessBo processBo = new WorkCenterProcessBo();
		processBo.setPage(processForm.getPage());
		processBo.setPageCount(processForm.getPageCount());
		processBo.setBusinessType(processForm.getBusinessType());
		processBo.setEnterpriseid(getEnterpriseid());
		return iProcessService.page(processBo);
	}

	@Override
	public WorkCenterProcessVo getBusiness(WorkCenterProcessForm processForm) throws FrameworkRuntimeException {
		WorkCenterProcessBo processBo = new WorkCenterProcessBo();
		processBo.setBusinessId(processForm.getBusinessId());
		processBo.setEnterpriseid(getEnterpriseid());
		return iProcessService.get(processBo);
	}

	// @Override
	// public List<WorkCenterProcessVo> list(WorkCenterProcessForm processForm)
	// throws FrameworkRuntimeException {
	// // 普通用户角度
	// WorkCenterProcessBo processBo = BeanUtils.copy(processForm,
	// WorkCenterProcessBo.class);
	// processBo.setEnterpriseid(getEnterpriseid());
	// String status = processForm.getStatus();
	// // 查询自己userid
	// List<String> userids = BeanUtils.list(getUserid());
	// // 查询自己roleid
	// List<String> roleids = BeanUtils.list(getRoleid());
	// switch (status) {
	// case WaterConstants.WORK_CENTER_PROCESS_APPLY:
	// // 已申请
	// processBo.setApplicant(getUserid());
	// return iProcessService.list(processBo);
	// case WaterConstants.WORK_CENTER_PROCESS_HANDLE:
	// // 处理中
	// processBo.setNextHandlers(userids);
	// processBo.setNextCarbonCopyers(userids);
	// processBo.setNextHandlerRoles(roleids);
	// processBo.setNextCarbonCopyerRoles(roleids);
	// return iProcessService.list(processBo);
	// case WaterConstants.WORK_CENTER_PROCESS_OVER:
	// // 已结束
	// processBo.setApplicant(getUserid());
	// processBo.setNextHandlers(userids);
	// processBo.setNextCarbonCopyers(userids);
	// processBo.setNextHandlerRoles(roleids);
	// processBo.setNextCarbonCopyerRoles(roleids);
	// return iProcessService.list(processBo);
	// case WaterConstants.WORK_CENTER_PROCESS_HANG:
	// // 已挂起
	// processBo.setAssignHandler(getUserid());
	// return iProcessService.list(processBo);
	// default:
	// return new ArrayList<>();
	// }
	// }

	// @Override
	// public WorkCenterProcessVo get(WorkCenterProcessForm processForm) throws
	// FrameworkRuntimeException {
	// WorkCenterProcessBo processBo = new WorkCenterProcessBo();
	// processBo.setId(processForm.getId());
	// processBo.setEnterpriseid(getEnterpriseid());
	// WorkCenterProcessVo get = iProcessService.get(processBo);
	// return get;
	// }

	// @Override
	// @Transactional(propagation = Propagation.REQUIRED, rollbackFor =
	// FrameworkRuntimeException.class)
	// public WorkCenterProcessVo add(WorkCenterProcessForm processForm) throws
	// FrameworkRuntimeException {
	// // 新增数据源
	// WorkCenterProcessBo processBo = BeanUtils.copy(processForm,
	// WorkCenterProcessBo.class);
	// processBo.setCode(null);
	// processBo.setEnterpriseid(getEnterpriseid());
	// processBo.setUserBy(getName());
	// processBo.setCurr(getCurr());
	// WorkCenterProcessVo workCenterProcessVo =
	// iProcessService.add(processBo);
	// return workCenterProcessVo;
	// }

	// @Override
	// @Transactional(propagation = Propagation.REQUIRED, rollbackFor =
	// FrameworkRuntimeException.class)
	// public WorkCenterProcessVo edit(WorkCenterProcessForm processForm) throws
	// FrameworkRuntimeException {
	// // 编辑数据源
	// WorkCenterProcessBo processBo = BeanUtils.copy(processForm,
	// WorkCenterProcessBo.class);
	// processBo.setEnterpriseid(getEnterpriseid());
	// processBo.setUserBy(getName());
	// processBo.setCurr(getCurr());
	// iProcessService.edit(processBo);
	//
	// return null;
	// }

	// @Override
	// @Transactional(propagation = Propagation.REQUIRED, rollbackFor =
	// FrameworkRuntimeException.class)
	// public String del(WorkCenterProcessForm processForm) throws
	// FrameworkRuntimeException {
	// WorkCenterProcessBo processBo = new WorkCenterProcessBo();
	// processBo.setId(processForm.getId());
	// processBo.setEnterpriseid(getEnterpriseid());
	// iProcessService.del(processBo);
	// return null;
	// }

}
