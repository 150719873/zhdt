package com.dotop.pipe.web.factory.third;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dotop.pipe.api.service.device.IDeviceService;
import com.dotop.pipe.auth.api.service.enterprise.IEnterpriseService;
import com.dotop.pipe.auth.core.vo.enterprise.EnterpriseVo;
import com.dotop.pipe.core.bo.device.DeviceBo;
import com.dotop.pipe.core.form.DeviceForm;
import com.dotop.pipe.core.vo.device.DeviceVo;
import com.dotop.pipe.web.api.factory.third.IThirdDeviceFactory;
import com.dotop.smartwater.dependence.core.pagination.Pagination;

@Component
public class ThirdDeviceFactoryImpl implements IThirdDeviceFactory {

	@Autowired
	private IEnterpriseService iEnterpriseService;

	@Autowired
	private IDeviceService iDeviceService;

	@Override
	public Pagination<DeviceVo> page(DeviceForm deviceForm) {
		String eid = deviceForm.getEnterpriseId();
		EnterpriseVo enterpriseVo = iEnterpriseService.get(eid);
		String enterpriseId = enterpriseVo.getEnterpriseId();
		Integer page = deviceForm.getPage();
		Integer pageSize = deviceForm.getPageSize();
		String name = deviceForm.getName();
		String code = deviceForm.getCode();

		DeviceBo deviceBo = new DeviceBo();
		deviceBo.setName(name);
		deviceBo.setCode(code);
		deviceBo.setPage(page);
		deviceBo.setPageSize(pageSize);
		deviceBo.setEnterpriseId(enterpriseId);
		return iDeviceService.page(deviceBo);
	}

}
