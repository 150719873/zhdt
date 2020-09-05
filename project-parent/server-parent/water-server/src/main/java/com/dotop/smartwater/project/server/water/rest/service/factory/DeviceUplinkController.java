package com.dotop.smartwater.project.server.water.rest.service.factory;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.dotop.smartwater.dependence.core.common.BaseController;
import com.dotop.smartwater.dependence.core.global.GlobalContext;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.utils.VerificationUtils;
import com.dotop.smartwater.project.module.api.revenue.IThirdDeviceDataPushFactory;
import com.dotop.smartwater.project.module.core.auth.local.AuthCasClient;
import com.dotop.smartwater.project.module.core.auth.vo.UserVo;
import com.dotop.smartwater.project.module.core.water.constants.ResultCode;
import com.dotop.smartwater.project.module.core.water.form.DeviceUplinkForm;
import com.dotop.smartwater.project.server.water.common.FoundationController;

/**
*
* @author YangKe
* @description 第三方接口（上行数据）
* @date 2019-10-16
*/
@RestController

@RequestMapping("/device/third")
public class DeviceUplinkController extends FoundationController implements BaseController<DeviceUplinkForm> {

	private static final Logger LOGGER = LoggerFactory.getLogger(DeviceUplinkController.class);
	
	@Autowired
	private IThirdDeviceDataPushFactory iThirdDeviceDataPushFactory;
	
	@PostMapping(value = "/uplinks", produces = GlobalContext.PRODUCES)
    public String uplinkByThird(@RequestBody List<DeviceUplinkForm> deviceUplinkForms) {
		LOGGER.info(LogMsg.to("msg:", "第三方推送读数开始", "deviceUplinkForms", deviceUplinkForms));
		if(deviceUplinkForms == null || deviceUplinkForms.isEmpty()) {
			return resp(ResultCode.Fail, "列表不能为空", null);
		}
		// 校验
		for(DeviceUplinkForm deviceUplinkForm: deviceUplinkForms) {
			String devid = deviceUplinkForm.getDevid();
			String water = deviceUplinkForm.getWater();
			String deveui = deviceUplinkForm.getDeveui();
			Date rxtime = deviceUplinkForm.getRxtime();
			
	        VerificationUtils.string("water", water);
	        VerificationUtils.date("rxtime", rxtime);
	        if(StringUtils.isBlank(devid) && StringUtils.isBlank(deveui)) {
	        	return resp(ResultCode.Fail, "设备ID和设备EUI不能都为空", null);
	        }
		}
		UserVo user = AuthCasClient.getUser();
        iThirdDeviceDataPushFactory.batchUplink(deviceUplinkForms, user);
        LOGGER.info(LogMsg.to("msg:", "第三方推送读数结束", "deviceUplinkForms", deviceUplinkForms));
        return resp(ResultCode.Success, ResultCode.SUCCESS, null);
    }
}
