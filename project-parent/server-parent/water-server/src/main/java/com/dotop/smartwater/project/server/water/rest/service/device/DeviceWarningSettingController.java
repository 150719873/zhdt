package com.dotop.smartwater.project.server.water.rest.service.device;

import com.dotop.smartwater.dependence.core.common.BaseController;
import com.dotop.smartwater.dependence.core.global.GlobalContext;
import com.dotop.smartwater.dependence.core.utils.VerificationUtils;
import com.dotop.smartwater.project.module.api.device.IDeviceWarningSettingFactory;
import com.dotop.smartwater.project.module.core.water.constants.ResultCode;
import com.dotop.smartwater.project.module.core.water.enums.OperateTypeEnum;
import com.dotop.smartwater.project.module.core.water.form.DeviceWarningSettingForm;
import com.dotop.smartwater.project.server.water.common.FoundationController;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**

 * @date 2019/4/1.
 */
@RestController

@RequestMapping("/deviceWarningSetting")
public class DeviceWarningSettingController extends FoundationController implements BaseController<DeviceWarningSettingForm> {

	@Autowired
	private IDeviceWarningSettingFactory iDeviceWarningSettingFactory;

	/**
	 * 告警设置分页
	 *
	 * @param agrs
	 * @return
	 */
	@Override
	@PostMapping(value = "/page", produces = GlobalContext.PRODUCES)
	public String page(@RequestBody DeviceWarningSettingForm agrs) {
		VerificationUtils.obj("DeviceWarningSettingForm", agrs);
		return resp(ResultCode.Success, ResultCode.SUCCESS, iDeviceWarningSettingFactory.page(agrs));
	}

	@Override
	@PostMapping(value = "/edit", produces = GlobalContext.PRODUCES)
	public String edit(@RequestBody DeviceWarningSettingForm agrs) {
		VerificationUtils.integer("预警次数", agrs.getWarningNum());
		VerificationUtils.integer("预警类型", agrs.getWarningType());
		VerificationUtils.integer("预警方式", agrs.getNotifyType());
		VerificationUtils.integer("模板类型", agrs.getModelType());
		if(StringUtils.isEmpty(agrs.getId())){
			auditLog(OperateTypeEnum.DEVICE_WARNING_SETTING,"新增","预警类型",agrs.getWarningType());
		}else{
			auditLog(OperateTypeEnum.DEVICE_WARNING_SETTING,"修改","预警通知设置ID",agrs.getId());
		}
		return resp(ResultCode.Success, ResultCode.SUCCESS, iDeviceWarningSettingFactory.edit(agrs));
	}
}
