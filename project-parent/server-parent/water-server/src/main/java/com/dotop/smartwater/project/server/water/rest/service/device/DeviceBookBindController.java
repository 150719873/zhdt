package com.dotop.smartwater.project.server.water.rest.service.device;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.dotop.smartwater.dependence.core.common.BaseController;
import com.dotop.smartwater.dependence.core.global.GlobalContext;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.dependence.core.utils.UuidUtils;
import com.dotop.smartwater.dependence.core.utils.VerificationUtils;
import com.dotop.smartwater.project.module.api.device.IDeviceBookBindFactory;
import com.dotop.smartwater.project.module.core.water.constants.ResultCode;
import com.dotop.smartwater.project.module.core.water.form.DeviceBookBindForm;
import com.dotop.smartwater.project.module.core.water.vo.DeviceBookBindVo;

/**
 * 

 * @description 表册绑定抄表员
 * @date 2019-10-22 17:16
 *
 */
@RestController

@RequestMapping("/deviceBookBind")
public class DeviceBookBindController implements BaseController<DeviceBookBindForm> {

	private static final Logger LOGGER = LogManager.getLogger(DeviceBookBindController.class);
	
	@Autowired
	private IDeviceBookBindFactory IDeviceBookBindFactory;
	
	@PostMapping(value = "/configureDeviceBookBind", produces = GlobalContext.PRODUCES)
	public String configureDeviceBookBind(@RequestBody List<DeviceBookBindForm> deviceBookBindForms) {
		LOGGER.info(LogMsg.to("msg:", "绑定表册抄表员开始", "deviceBookBindForms", deviceBookBindForms));
		if(deviceBookBindForms == null || deviceBookBindForms.isEmpty()) {
			deviceBookBindForms = new ArrayList<DeviceBookBindForm>();
			DeviceBookBindForm deviceBookBindForm = new DeviceBookBindForm();
			deviceBookBindForm.setBookNum(DeviceBookBindVo.DEFAULT_WIPE_DATA);
			deviceBookBindForm.setBookUserId(UuidUtils.getUuid());
			deviceBookBindForms.add(deviceBookBindForm);
		}
		for(DeviceBookBindForm deviceBookBindForm: deviceBookBindForms) {
			String bookUserId = deviceBookBindForm.getBookUserId();
			String bookNum = deviceBookBindForm.getBookNum();
			VerificationUtils.toString("bookUserId", bookUserId);
			VerificationUtils.toString("bookNum", bookNum);
		}
		String result = IDeviceBookBindFactory.configureDeviceBookBind(deviceBookBindForms);
		LOGGER.info(LogMsg.to("msg:", "绑定表册抄表员结束"));
		if("success".equals(result)) {
			return resp(ResultCode.Success, ResultCode.SUCCESS, null);
		}else {
			return resp(ResultCode.Fail, result, null);
		}
	}
	
	@PostMapping(value = "/listDeviceBookBind", produces = GlobalContext.PRODUCES)
	public String listDeviceBookBind(@RequestBody DeviceBookBindForm deviceBookBindForm) {	
		LOGGER.info(LogMsg.to("msg:", "获取表册抄表员列表开始", "deviceBookBindForm", deviceBookBindForm));
		List<DeviceBookBindVo> list = IDeviceBookBindFactory.listDeviceBookBind(deviceBookBindForm);
		LOGGER.info(LogMsg.to("msg:", "获取表册抄表员结束", "deviceBookBindForm", deviceBookBindForm));
		return resp(ResultCode.Success, ResultCode.SUCCESS, list);
	}
	
	@PostMapping(value = "/deleteDeviceBookBind", produces = GlobalContext.PRODUCES)
	public String deleteDeviceBookBind(@RequestBody DeviceBookBindForm deviceBookBindForm) {
		LOGGER.info(LogMsg.to("msg:", "解绑表册抄表员开始", "deviceBookBindForm", deviceBookBindForm));
		if(StringUtils.isBlank(deviceBookBindForm.getId()) && StringUtils.isBlank(deviceBookBindForm.getBookNum())) {
			return resp(ResultCode.Fail, "解绑对象主键不能为空", null);
		}
		String result = IDeviceBookBindFactory.deleteDeviceBookBind(deviceBookBindForm);
		LOGGER.info(LogMsg.to("msg:", "解绑表册抄表员结束", "deviceBookBindForm", deviceBookBindForm));
		if(result == "success") {
			return resp(ResultCode.Success, ResultCode.SUCCESS, null);
		}else {
			return resp(ResultCode.Fail, result, null);
		}
	}
	
	@PostMapping(value = "/pageBindOwner", produces = GlobalContext.PRODUCES)
	public String pageBindOwner(@RequestBody DeviceBookBindForm deviceBookBindForm) {
		LOGGER.info(LogMsg.to("msg:", "获取表册下所绑业主开始", "deviceBookBindForm", deviceBookBindForm));
		String bookNum = deviceBookBindForm.getBookNum();
		VerificationUtils.toString("bookNum", bookNum);
		
		Pagination<DeviceBookBindVo> pagination = IDeviceBookBindFactory.pageBindOwner(deviceBookBindForm);
		LOGGER.info(LogMsg.to("msg:", "获取表册下所绑业主结束", "deviceBookBindForm", deviceBookBindForm));
		return resp(ResultCode.Success, ResultCode.SUCCESS, pagination);
	}
	
	@PostMapping(value = "/listBindOwner", produces = GlobalContext.PRODUCES)
	public String listBindOwner(@RequestBody DeviceBookBindForm deviceBookBindForm) {
		LOGGER.info(LogMsg.to("msg:", "获取业主开始", "deviceBookBindForm", deviceBookBindForm));
		String bookNum = deviceBookBindForm.getBookNum();
		VerificationUtils.toString("bookNum", bookNum);
		
		Pagination<DeviceBookBindVo> list = IDeviceBookBindFactory.listBindOwner(deviceBookBindForm);
		LOGGER.info(LogMsg.to("msg:", "获取业主结束", "deviceBookBindForm", deviceBookBindForm));
		return resp(ResultCode.Success, ResultCode.SUCCESS, list);
	}
	
	@PostMapping(value = "/bookBindOwner", produces = GlobalContext.PRODUCES)
	public String bookBindOwner(@RequestBody List<DeviceBookBindForm> deviceBookBindForms) {
		LOGGER.info(LogMsg.to("msg:", "表册绑定业主开始", "deviceBookBindForms", deviceBookBindForms));
		if(deviceBookBindForms == null || deviceBookBindForms.isEmpty()) {
			return resp(ResultCode.Fail, "业主列表不能为空", null);
		}
		for(DeviceBookBindForm deviceBookBindForm: deviceBookBindForms) {
			String ownerId = deviceBookBindForm.getOwnerId();
			String bookNum = deviceBookBindForm.getBookNum();
			VerificationUtils.toString("ownerId", ownerId);
			VerificationUtils.toString("bookNum", bookNum);
			if(deviceBookBindForm.getChecked() == null) {
				deviceBookBindForm.setChecked(false);
			}
		}
		IDeviceBookBindFactory.bindOwner(deviceBookBindForms);
		LOGGER.info(LogMsg.to("msg:", "表册绑定业主结束"));
		return resp(ResultCode.Success, ResultCode.SUCCESS, null);
	}
	
	@PostMapping(value = "/deleteBindOwner", produces = GlobalContext.PRODUCES)
	public String deleteBindOwner(@RequestBody DeviceBookBindForm deviceBookBindForm) {
		LOGGER.info(LogMsg.to("msg:", "表册解绑业主开始", "deviceBookBindForm", deviceBookBindForm));
		String ownerId = deviceBookBindForm.getOwnerId();
		VerificationUtils.toString("ownerId", ownerId);
		
		String result = IDeviceBookBindFactory.deleteBindOwner(deviceBookBindForm);
		LOGGER.info(LogMsg.to("msg:", "表册解绑业主结束", "deviceBookBindForm", deviceBookBindForm));
		if("success".equals(result)) {
			return resp(ResultCode.Success, ResultCode.SUCCESS, null);
		}else {
			return resp(ResultCode.Fail, result, null);
		}
	}
}
