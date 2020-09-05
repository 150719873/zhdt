package com.dotop.smartwater.project.module.api.device.impl;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.dependence.core.utils.UuidUtils;
import com.dotop.smartwater.project.module.api.device.IDeviceWarningFactory;
import com.dotop.smartwater.project.module.core.auth.local.AuthCasClient;
import com.dotop.smartwater.project.module.core.auth.vo.UserVo;
import com.dotop.smartwater.project.module.core.water.bo.DeviceWarningBo;
import com.dotop.smartwater.project.module.core.water.bo.DeviceWarningDetailBo;
import com.dotop.smartwater.project.module.core.water.bo.OwnerBo;
import com.dotop.smartwater.project.module.core.water.constants.PathCode;
import com.dotop.smartwater.project.module.core.water.constants.ResultCode;
import com.dotop.smartwater.project.module.core.water.constants.WaterConstants;
import com.dotop.smartwater.project.module.core.water.form.DeviceWarningDetailForm;
import com.dotop.smartwater.project.module.core.water.form.DeviceWarningForm;
import com.dotop.smartwater.project.module.core.water.form.OwnerForm;
import com.dotop.smartwater.project.module.core.water.utils.FileUtil;
import com.dotop.smartwater.project.module.core.water.utils.file.ExportCreator;
import com.dotop.smartwater.project.module.core.water.vo.DeviceVo;
import com.dotop.smartwater.project.module.core.water.vo.DeviceWarningDetailVo;
import com.dotop.smartwater.project.module.core.water.vo.DeviceWarningVo;
import com.dotop.smartwater.project.module.service.device.IDeviceService;
import com.dotop.smartwater.project.module.service.device.IDeviceWarningService;
import com.dotop.water.tool.service.BaseInf;

/**

 * @date 2019/2/25.
 */
@Component
public class DeviceWarningFactoryImpl implements IDeviceWarningFactory {

	private static final Logger LOGGER = LogManager.getLogger(DeviceWarningFactoryImpl.class);

	@Value("${param.revenue.excelTempUrl}")
	private String excelTempUrl;

	@Autowired
	private IDeviceService iDeviceService;

	@Autowired
	private IDeviceWarningService iDeviceWarningService;

	@Override
	public Integer updateDeviceWarning(DeviceWarningForm deviceWarningForm) {
		return null;
	}

	@Override
	public Pagination<DeviceWarningVo> getDeviceWarningPage(DeviceWarningForm deviceWarningForm) {
		UserVo user = AuthCasClient.getUser();
		DeviceWarningBo deviceWarningBo = new DeviceWarningBo();
		BeanUtils.copyProperties(deviceWarningForm, deviceWarningBo);
		
		deviceWarningBo.setEnterpriseid(user.getEnterpriseid());
		return iDeviceWarningService.getDeviceWarningPage(deviceWarningBo);
	}

	@Override
	public List<DeviceWarningVo> getDeviceWarningList(DeviceWarningForm deviceWarningForm) {
		DeviceWarningBo deviceWarningBo = new DeviceWarningBo();
		BeanUtils.copyProperties(deviceWarningForm, deviceWarningBo);
		return iDeviceWarningService.getWarningList(deviceWarningBo);
	}

	@Override
	public Integer addDeviceWarning(DeviceWarningForm deviceWarningForm) {
		UserVo user = AuthCasClient.getUser();	
		DeviceWarningBo deviceWarningBo = new DeviceWarningBo();
		BeanUtils.copyProperties(deviceWarningForm, deviceWarningBo);

		DeviceVo deviceVo = iDeviceService.findByDevEUI(deviceWarningForm.getDeveui());
		if(deviceVo == null){
			throw new FrameworkRuntimeException(ResultCode.NOTFINDWATER, ResultCode.getMessage(ResultCode.NOTFINDWATER));
		}
		deviceWarningBo.setId(UuidUtils.getUuid());
		deviceWarningBo.setStatus(DeviceWarningVo.DEVICE_WARNING_STATUS_WAIT);
		deviceWarningBo.setDevid(deviceVo.getDevid());
		deviceWarningBo.setDevno(deviceVo.getDevno());
		deviceWarningBo.setDeveui(deviceVo.getDeveui());
		deviceWarningBo.setAddress(deviceVo.getUseraddr());
		deviceWarningBo.setModeId(deviceVo.getMode());
		deviceWarningBo.setModeName(deviceVo.getModeName());
		deviceWarningBo.setCtime(new Date());
		deviceWarningBo.setEnterpriseid(user.getEnterpriseid());
		return iDeviceWarningService.addDeviceWarning(deviceWarningBo);
	}

	@Override
	public Pagination<DeviceWarningVo> getLowBattery(OwnerForm ownerForm) {
		OwnerBo ownerBo = new OwnerBo();
		BeanUtils.copyProperties(ownerForm, ownerBo);
		return iDeviceWarningService.findWarningBypage(ownerBo);
	}
	
	@Override
	public long warninghandle(DeviceWarningForm deviceWarningForm)  {
		// 获取用户信息
		String enterpriseid = "";
		String name = "";
		try{
			UserVo user = AuthCasClient.getUser();
			enterpriseid = user.getEnterpriseid();
			name = user.getName();
		}catch(Exception e){
			LOGGER.error(e.getMessage());
		}
		if(StringUtils.isEmpty(enterpriseid) || StringUtils.isEmpty(name)){
			enterpriseid = deviceWarningForm.getEnterpriseid();
			List<UserVo> userList = BaseInf.getUserList(enterpriseid);
			for(UserVo user:userList){
				if(user.getType() == WaterConstants.USER_TYPE_ADMIN_ENTERPRISE){
					name = user.getName();
					break;
				}
			}
		}
		// 数据处理
		iDeviceWarningService.handleWarning(name, deviceWarningForm.getNodeIds());
		return iDeviceWarningService.getDeviceWarningCount(enterpriseid);
	}

	@Override
	public long getDeviceWarningCount(DeviceWarningForm deviceWarningForm) {
		// 获取用户信息
		UserVo user = AuthCasClient.getUser();
		// 数据处理
		return iDeviceWarningService.getDeviceWarningCount(user.getEnterpriseid());
	}

	@Override
	public String exportLowBattery(OwnerForm ownerForm) {
		try {
			UserVo user = AuthCasClient.getUser();
			ownerForm.setEnterpriseid(user.getEnterpriseid());

			OwnerBo ownerBo = new OwnerBo();
			BeanUtils.copyProperties(ownerForm, ownerBo);

			File baseDir = new File(excelTempUrl, PathCode.DeviceWarningExcel);
			if (!baseDir.exists()) {
				baseDir.mkdirs();
			}
			FileUtil.deleteFiles(baseDir, 10);

			SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
			String date = format.format(new Date());

			Random r = new Random();
			String fileName = "水表告警数据" + "_" + date + r.nextInt(10000) + ".xls";
			String tempFilename = File.separator + PathCode.DeviceWarningExcel + File.separator + fileName;
			tempFilename = excelTempUrl + tempFilename;

			Pagination<DeviceWarningVo> pagination = iDeviceWarningService.findWarningBypage(ownerBo);

			ExportCreator creator = new ExportCreator(tempFilename, pagination.getData());

			creator.lowBatteryCreate();
			return fileName;

		} catch (Exception e) {
			throw new FrameworkRuntimeException(ResultCode.Fail, e.getMessage());
		}
	}

	@Override
	public Pagination<DeviceWarningDetailVo> getDeviceWarningDetailPage(
			DeviceWarningDetailForm deviceWarningDetailForm) {
		// TODO Auto-generated method stub
		DeviceWarningDetailBo deviceWarningDetailBo = new DeviceWarningDetailBo();
		BeanUtils.copyProperties(deviceWarningDetailForm, deviceWarningDetailBo);
		return iDeviceWarningService.getDeviceWarningDetailPage(deviceWarningDetailBo);
	}
	
	@Override
	public DeviceWarningVo getDeviceWarning(DeviceWarningForm deviceWarningForm) {
		// TODO Auto-generated method stub
		DeviceWarningBo deviceWarningBo = new DeviceWarningBo();
		BeanUtils.copyProperties(deviceWarningForm, deviceWarningBo);
		return iDeviceWarningService.getDeviceWarning(deviceWarningBo);
	}

	@Override
	public Integer deleteDeviceWarning(DeviceWarningForm deviceWarningForm) {
		// TODO Auto-generated method stub
		DeviceWarningBo deviceWarningBo = new DeviceWarningBo();
		BeanUtils.copyProperties(deviceWarningForm, deviceWarningBo);
		return iDeviceWarningService.deleteDeviceWarning(deviceWarningBo);
	}

}
