package com.dotop.smartwater.project.auth.rest.service;

import com.dotop.smartwater.project.auth.api.IDeviceChangeFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.dotop.smartwater.dependence.core.common.BaseController;
import com.dotop.smartwater.dependence.core.global.GlobalContext;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.dependence.core.utils.VerificationUtils;
import com.dotop.smartwater.project.module.core.water.constants.ResultCode;
import com.dotop.smartwater.project.module.core.water.form.DeviceChangeForm;
import com.dotop.smartwater.project.module.core.water.vo.DeviceChangeVo;

@RestController

@RequestMapping("/monitor")
public class DeviceChangeController implements BaseController<DeviceChangeForm> {
	
	private static final Logger LOGGER = LogManager.getLogger(DeviceChangeController.class);

	@Autowired
	private IDeviceChangeFactory iFatory;
	
	@PostMapping(value = "/page", produces = GlobalContext.PRODUCES)
	public String page(@RequestBody DeviceChangeForm form) {
		LOGGER.info(LogMsg.to("msg:", " 分页查询开始", "form", form));
		Integer page = form.getPage();
		Integer pageCount = form.getPageCount();
		// 验证
		VerificationUtils.integer("page", page);
		VerificationUtils.integer("pageCount", pageCount);

		Pagination<DeviceChangeVo> pagination = iFatory.page(form);
		LOGGER.info(LogMsg.to("msg:", " 分页查询查询结束"));
		return resp(ResultCode.Success, ResultCode.SUCCESS, pagination);
	}
}
