package com.dotop.smartwater.project.server.water.rest.service.app;

import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.dotop.smartwater.dependence.core.common.BaseController;
import com.dotop.smartwater.dependence.core.global.GlobalContext;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.dependence.core.utils.VerificationUtils;
import com.dotop.smartwater.project.module.api.workcenter.IProcessMsgFactory;
import com.dotop.smartwater.project.module.core.water.constants.ResultCode;
import com.dotop.smartwater.project.module.core.water.form.DictionaryChildForm;
import com.dotop.smartwater.project.module.core.water.form.WorkCenterProcessMsgForm;
import com.dotop.smartwater.project.module.core.water.vo.WorkCenterProcessMsgVo;

@RestController("AppWorkCenterProcessMsgController")
@RequestMapping("/app/workcenter/process/msg")

public class AppProcessMsgController implements BaseController<WorkCenterProcessMsgForm> {

	private static final Logger LOGGER = LogManager.getLogger(AppProcessMsgController.class);

	@Autowired
	private IProcessMsgFactory iProcessMsgFactory;

	private static final String PROCESSID = "processId";

	private static final long SIZE = 1 * 1024l * 1024l;

	@Override
	@PostMapping(value = "/page", produces = GlobalContext.PRODUCES)
	public String page(@RequestBody WorkCenterProcessMsgForm processMsgForm) {
		String processId = processMsgForm.getProcessId();
		Integer page = processMsgForm.getPage();
		Integer pageCount = processMsgForm.getPageCount();
		// 验证
		VerificationUtils.string(PROCESSID, processId);
		VerificationUtils.integer("page", page);
		VerificationUtils.integer("pageCount", pageCount);
		Pagination<WorkCenterProcessMsgVo> pagination = iProcessMsgFactory.page(processMsgForm);
		return resp(ResultCode.Success, ResultCode.SUCCESS, pagination);
	}

	@Override
	@PostMapping(value = "/add", produces = GlobalContext.PRODUCES)
	public String add(@RequestBody WorkCenterProcessMsgForm processMsgForm) {
		String processId = processMsgForm.getProcessId();
		String status = processMsgForm.getStatus();
		String processNodeId = processMsgForm.getProcessNodeId();
		DictionaryChildForm handleDictChild = processMsgForm.getHandleDictChild();
		// 验证
		VerificationUtils.string(PROCESSID, processId);
		VerificationUtils.string("status", status);
		VerificationUtils.string("processNodeId", processNodeId);
		VerificationUtils.obj("handleDictChild", handleDictChild);
		String childId = handleDictChild.getChildId();
		VerificationUtils.string("childId", childId);
		WorkCenterProcessMsgVo add = iProcessMsgFactory.add(processMsgForm);
		return resp(ResultCode.Success, ResultCode.SUCCESS, add);
	}

	/**
	 * 文件上传
	 */
	@PostMapping(value = "/upload", consumes = "multipart/form-data")
	public String upload(@RequestParam(name = "file", required = false) MultipartFile file,
			@RequestParam(required = false) String processId, @RequestParam(required = false) String type) {
		LOGGER.info(LogMsg.to("file", file, PROCESSID, processId, "type", type));
		if (file.getSize() > SIZE) { // 1M
			return resp(ResultCode.Fail, "上传的文件过大", null);
		}
		VerificationUtils.string(PROCESSID, processId);
		VerificationUtils.string("type", type);
		String url = iProcessMsgFactory.upload(file, processId, type);
		return resp(ResultCode.Success, ResultCode.SUCCESS, url);
	}

	@PostMapping(value = "/upload/del", produces = GlobalContext.PRODUCES)
	public String uploadDel(@RequestBody Map<String, String> map) {
		String url = map.get("url");
		String processId = map.get(PROCESSID);
		LOGGER.info(LogMsg.to("url", url, PROCESSID, processId));
		VerificationUtils.string("url", url, false, 1024);
		VerificationUtils.string(PROCESSID, processId);
		iProcessMsgFactory.uploadDel(url, processId);
		return resp(ResultCode.Success, ResultCode.SUCCESS, null);
	}

}
