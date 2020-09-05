package com.dotop.smartwater.project.server.water.rest.service.revenue;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.dotop.smartwater.dependence.core.common.BaseController;
import com.dotop.smartwater.dependence.core.global.GlobalContext;
import com.dotop.smartwater.project.module.api.device.IDeviceModelFactory;
import com.dotop.smartwater.project.module.core.water.constants.ResultCode;
import com.dotop.smartwater.project.module.core.water.form.DeviceModelForm;

/**

 * @date 2019/2/27.
 */
@RestController

@RequestMapping("/model")
public class ModelController implements BaseController<DeviceModelForm> {

	@Autowired
	private IDeviceModelFactory iDeviceModelFactory;

	/**
	 * 型号-列表
	 *
	 * @param request
	 * @param deviceModelForm
	 * @return
	 */
	@PostMapping(value = "/find_mode", produces = GlobalContext.PRODUCES)
	public String findMode(HttpServletRequest request, @RequestBody DeviceModelForm deviceModelForm) {
		if (deviceModelForm.getPage() == 0 && deviceModelForm.getPageCount() == 0) {
			return resp(ResultCode.Success, ResultCode.SUCCESS, iDeviceModelFactory.noPagingfind(deviceModelForm));
		} else {
			return resp(ResultCode.Success, ResultCode.SUCCESS, iDeviceModelFactory.find(deviceModelForm));
		}
	}

	/**
	 * 新增
	 *
	 * @param request
	 * @param deviceModelForm
	 * @return
	 */
	@PostMapping(value = "/save", produces = GlobalContext.PRODUCES)
	public String save(HttpServletRequest request, @RequestBody DeviceModelForm deviceModelForm) {
		iDeviceModelFactory.save(deviceModelForm);
		return resp(ResultCode.Success, ResultCode.SUCCESS, null);
	}

	/**
	 * 修改设备类型信息
	 *
	 * @param request
	 * @param deviceModelForm
	 * @return
	 */
	@PostMapping(value = "/update", produces = GlobalContext.PRODUCES)
	public String update(HttpServletRequest request, @RequestBody DeviceModelForm deviceModelForm) {
		iDeviceModelFactory.update(deviceModelForm);
		return resp(ResultCode.Success, ResultCode.SUCCESS, null);
	}

	/**
	 * 删除
	 *
	 * @param request
	 * @param deviceModelForm
	 * @return
	 */
	@PostMapping(value = "/delete", produces = GlobalContext.PRODUCES)
	public String delete(HttpServletRequest request, @RequestBody DeviceModelForm deviceModelForm) {
		iDeviceModelFactory.delete(deviceModelForm);
		return resp(ResultCode.Success, ResultCode.SUCCESS, null);
	}

	/**
	 * 查找
	 *
	 * @param request
	 * @param deviceModelForm
	 * @return
	 */
	@PostMapping(value = "/get", produces = GlobalContext.PRODUCES)
	public String get(HttpServletRequest request, @RequestBody DeviceModelForm deviceModelForm) {
		return resp(ResultCode.Success, ResultCode.SUCCESS, iDeviceModelFactory.get(deviceModelForm));
	}
}
