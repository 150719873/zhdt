package com.dotop.smartwater.project.server.water.rest.service.device;

import com.dotop.smartwater.dependence.core.common.BaseController;
import com.dotop.smartwater.dependence.core.global.GlobalContext;
import com.dotop.smartwater.dependence.core.utils.VerificationUtils;
import com.dotop.smartwater.project.module.api.device.IDeviceSubscribeFactory;
import com.dotop.smartwater.project.module.core.water.constants.ResultCode;
import com.dotop.smartwater.project.module.core.water.form.DeviceSubscribeForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @program: project-parent
 * @description: 管漏水表订阅

 * @create: 2019-03-04 09:05
 **/
@RestController

@RequestMapping("/deviceSubscribe")
public class DeviceSubscribeController implements BaseController<DeviceSubscribeForm> {

	@Autowired
	private IDeviceSubscribeFactory iDeviceSubscribeFactory;

	@Override
	@PostMapping(value = "/bind", produces = GlobalContext.PRODUCES)
	public String add(@RequestBody DeviceSubscribeForm form) {
		String enterpriseId = form.getEnterpriseid();
		String devNo = form.getDevno();
		VerificationUtils.toString("enterpriseId", enterpriseId);
		VerificationUtils.toString("devNo", devNo);

		iDeviceSubscribeFactory.add(form);
		return resp(ResultCode.Success, ResultCode.SUCCESS, null);
	}

	@Override
	@PostMapping(value = "/del", produces = GlobalContext.PRODUCES)
	public String del(@RequestBody DeviceSubscribeForm form) {
		String enterpriseId = form.getEnterpriseid();
		String devNo = form.getDevno();
		VerificationUtils.toString("enterpriseId", enterpriseId);
		VerificationUtils.toString("devNo", devNo);
		iDeviceSubscribeFactory.del(form);
		return resp(ResultCode.Success, ResultCode.SUCCESS, null);
	}


}
