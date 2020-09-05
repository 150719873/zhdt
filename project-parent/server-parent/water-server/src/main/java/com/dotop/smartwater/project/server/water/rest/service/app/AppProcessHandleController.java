package com.dotop.smartwater.project.server.water.rest.service.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.dotop.smartwater.dependence.core.common.BaseController;
import com.dotop.smartwater.dependence.core.global.GlobalContext;
import com.dotop.smartwater.dependence.core.utils.VerificationUtils;
import com.dotop.smartwater.project.module.api.workcenter.IProcessHandleFactory;
import com.dotop.smartwater.project.module.core.water.constants.ResultCode;
import com.dotop.smartwater.project.module.core.water.form.WorkCenterProcessHandleForm;
import com.dotop.smartwater.project.module.core.water.vo.WorkCenterProcessHandleVo;

@RestController("AppWorkCenterProcessHandleController")
@RequestMapping("/app/workcenter/process/handle")

public class AppProcessHandleController implements BaseController<WorkCenterProcessHandleForm> {

	@Autowired
	private IProcessHandleFactory iProcessHandleFactory;

	private static final String PROCESSID = "processId";

	/**
	 * 流程模板表单信息展示
	 */
	@Override
	@PostMapping(value = "/get", produces = GlobalContext.PRODUCES)
	public String get(@RequestBody WorkCenterProcessHandleForm processHandleForm) {
		VerificationUtils.string(PROCESSID, processHandleForm.getProcessId());
		WorkCenterProcessHandleVo get = iProcessHandleFactory.get(processHandleForm);
		return resp(ResultCode.Success, ResultCode.SUCCESS, get);
	}

	/**
	 * 流程模板表单信息展示sql数据源
	 */
	@PostMapping(value = "/list/process/db/auto", produces = GlobalContext.PRODUCES)
	public String listProcessDbAuto(@RequestBody WorkCenterProcessHandleForm processHandleForm) {
		String processId = processHandleForm.getProcessId();
		String processDbId = processHandleForm.getProcessDbId();
		Integer page = processHandleForm.getPage();
		Integer pageCount = processHandleForm.getPageCount();
		VerificationUtils.string(PROCESSID, processId);
		VerificationUtils.string("processDbId", processDbId);
		VerificationUtils.integer("page", page);
		VerificationUtils.integer("pageCount", pageCount);
		WorkCenterProcessHandleVo pagination = iProcessHandleFactory.listProcessDbAuto(processHandleForm);
		return resp(ResultCode.Success, ResultCode.SUCCESS, pagination);
	}

	/**
	 * 查看当前进程处理节点信息展示流程信息处理
	 */
	@PostMapping(value = "/get/curr/node", produces = GlobalContext.PRODUCES)
	public String getCurrNode(@RequestBody WorkCenterProcessHandleForm processHandleForm) {
		String processId = processHandleForm.getProcessId();
		// 验证
		VerificationUtils.string(PROCESSID, processId);
		WorkCenterProcessHandleVo get = iProcessHandleFactory.getCurrNode(processHandleForm);
		return resp(ResultCode.Success, ResultCode.SUCCESS, get);
	}
}
