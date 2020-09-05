package com.dotop.smartwater.project.server.water.rest.service.device;

import com.dotop.smartwater.dependence.core.common.BaseController;
import com.dotop.smartwater.dependence.core.global.GlobalContext;
import com.dotop.smartwater.dependence.core.utils.VerificationUtils;
import com.dotop.smartwater.project.module.api.device.IDeviceDownlinkFactory;
import com.dotop.smartwater.project.module.core.water.constants.ResultCode;
import com.dotop.smartwater.project.module.core.water.form.DeviceDownlinkForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**

 * @date 2019/4/4.
 */
@RestController

@RequestMapping("/deviceDownlink")
public class DeviceDownlinkController implements BaseController<DeviceDownlinkForm> {

	@Autowired
	private IDeviceDownlinkFactory iDeviceDownlinkFactory;

	@Override
	@PostMapping(value = "/page", produces = GlobalContext.PRODUCES)
	public String page(@RequestBody DeviceDownlinkForm agrs) {
		VerificationUtils.obj("DeviceDownlinkForm", agrs);
		return resp(ResultCode.Success, ResultCode.SUCCESS, iDeviceDownlinkFactory.page(agrs));
	}

	@Override
	@PostMapping(value = "/get", produces = GlobalContext.PRODUCES)
	public String get(@RequestBody DeviceDownlinkForm agrs) {
		VerificationUtils.obj("DeviceDownlinkForm", agrs);
		return resp(ResultCode.Success, ResultCode.SUCCESS, iDeviceDownlinkFactory.get(agrs));
	}

	@PostMapping(value = "/getLastDownLink", produces = GlobalContext.PRODUCES)
	public String getLastDownLink(@RequestBody DeviceDownlinkForm agrs) {
		VerificationUtils.string("devid", agrs.getDevid());
		return resp(ResultCode.Success, ResultCode.SUCCESS, iDeviceDownlinkFactory.getLastDownLink(agrs.getDevid()));
	}
}
