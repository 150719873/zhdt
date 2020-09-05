package com.dotop.smartwater.project.server.water.rest.service.device;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.dotop.smartwater.dependence.core.common.BaseController;
import com.dotop.smartwater.dependence.core.global.GlobalContext;
import com.dotop.smartwater.dependence.core.utils.VerificationUtils;
import com.dotop.smartwater.project.module.api.device.IDeviceWarningFactory;
import com.dotop.smartwater.project.module.core.water.constants.ResultCode;
import com.dotop.smartwater.project.module.core.water.form.DeviceWarningForm;

/**
 * 

 * 
 *         原com.dotop.water.controller.CasController
 */
@RestController

@RequestMapping("/watercas")
public class DeviceWarningController implements BaseController<DeviceWarningForm> {

	@Autowired
	private IDeviceWarningFactory iDeviceWarningFactory;

	@PostMapping(value = "/warninghandle", produces = GlobalContext.PRODUCES)
	public String warninghandle(@RequestBody DeviceWarningForm deviceWarningForm) {
		// 校验
		List<String> nodeIds = deviceWarningForm.getNodeIds();
		VerificationUtils.strList("nodeIds", nodeIds);
		// 操作
		long count = iDeviceWarningFactory.warninghandle(deviceWarningForm);
		return resp(ResultCode.Success, ResultCode.SUCCESS, count);
	}

	@PostMapping(value = "/getDeviceWarningCount", produces = GlobalContext.PRODUCES)
	public String getDeviceWarningCount(@RequestBody DeviceWarningForm deviceWarningForm) {
		// 操作
		long count = iDeviceWarningFactory.getDeviceWarningCount(deviceWarningForm);
		return resp(ResultCode.Success, ResultCode.SUCCESS, count);
	}
}
