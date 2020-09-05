package com.dotop.smartwater.project.server.water.rest.service.device;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.dotop.smartwater.dependence.core.common.BaseController;
import com.dotop.smartwater.dependence.core.global.GlobalContext;
import com.dotop.smartwater.dependence.core.utils.VerificationUtils;
import com.dotop.smartwater.project.module.api.device.IDeviceBookManagementFactory;
import com.dotop.smartwater.project.module.core.water.constants.ResultCode;
import com.dotop.smartwater.project.module.core.water.form.DeviceBookManagementForm;
import com.dotop.smartwater.project.module.core.water.vo.DeviceBookManagementVo;

/**
 * @program: project-parent
 * @description: 表册管理

 * @create: 2019-03-04 09:05
 **/
@RestController

@RequestMapping("/deviceBookManagement")
public class DeviceBookController implements BaseController<DeviceBookManagementForm> {

	@Autowired
	private IDeviceBookManagementFactory iDeviceBookManagementFactory;

	@Override
	@PostMapping(value = "/add", produces = GlobalContext.PRODUCES)
	public String add(@RequestBody DeviceBookManagementForm deviceBookManagementForm) {
		String bookNum = deviceBookManagementForm.getBookNum();
		String bookType = deviceBookManagementForm.getBookType();
		VerificationUtils.toString("bookNum", bookNum);
		VerificationUtils.toString("bookType", bookType);
		
		iDeviceBookManagementFactory.add(deviceBookManagementForm);
		return resp(ResultCode.Success, ResultCode.SUCCESS, null);
	}

	@Override
	@PostMapping(value = "/edit", produces = GlobalContext.PRODUCES)
	public String edit(@RequestBody DeviceBookManagementForm deviceBookManagementForm) {
		if(StringUtils.isBlank(deviceBookManagementForm.getBookId())) {
			return resp(ResultCode.Fail, "获取表册主键失败", null);
		}
		iDeviceBookManagementFactory.edit(deviceBookManagementForm);
		return resp(ResultCode.Success, ResultCode.SUCCESS, null);
	}

	@Override
	@PostMapping(value = "/del", produces = GlobalContext.PRODUCES)
	public String del(@RequestBody DeviceBookManagementForm deviceBookManagementForm) {
		String result = iDeviceBookManagementFactory.del(deviceBookManagementForm);
		if("Fail".equals(result)) {
			return resp(ResultCode.Fail, "该表册正在使用中，不能删除", null);
		}
		return resp(ResultCode.Success, ResultCode.SUCCESS, null);
	}

	@Override
	@PostMapping(value = "/get", produces = GlobalContext.PRODUCES)
	public String get(@RequestBody DeviceBookManagementForm deviceBookManagementForm) {
		return resp(ResultCode.Success, ResultCode.SUCCESS, iDeviceBookManagementFactory.get(deviceBookManagementForm));
	}

	@Override
	@PostMapping(value = "/page", produces = GlobalContext.PRODUCES)
	public String page(@RequestBody DeviceBookManagementForm deviceBookManagementForm) {
		List<DeviceBookManagementVo> list = iDeviceBookManagementFactory.list(deviceBookManagementForm);
		return resp(ResultCode.Success, ResultCode.SUCCESS, list);
	}

}
