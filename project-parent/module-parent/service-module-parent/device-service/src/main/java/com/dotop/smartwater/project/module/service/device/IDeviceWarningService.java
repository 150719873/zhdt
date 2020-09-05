package com.dotop.smartwater.project.module.service.device;

import java.util.List;

import com.dotop.smartwater.dependence.core.common.BaseService;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.project.module.core.water.bo.DeviceWarningBo;
import com.dotop.smartwater.project.module.core.water.bo.DeviceWarningDetailBo;
import com.dotop.smartwater.project.module.core.water.bo.OwnerBo;
import com.dotop.smartwater.project.module.core.water.vo.DeviceWarningDetailVo;
import com.dotop.smartwater.project.module.core.water.vo.DeviceWarningVo;

/**

 * @date 2019/2/25.

 * @date 2019-11-19
 */
public interface IDeviceWarningService extends BaseService<DeviceWarningBo, DeviceWarningVo> {

	Integer addDeviceWarning(DeviceWarningBo deviceWarningBo);

	Pagination<DeviceWarningVo> getDeviceWarningPage(DeviceWarningBo deviceWarningBo);

	Integer handleWarning(String userby, List<String> ids);

	List<DeviceWarningVo> getWarningList(DeviceWarningBo deviceWarningBo);

	Pagination<DeviceWarningVo> findWarningBypage(OwnerBo ownerBo);

	long getDeviceWarningCount(String enterpriseid);

	DeviceWarningVo findByDevice(DeviceWarningBo deviceWarningBo);

	Integer updateDeviceWarning(DeviceWarningBo deviceWarningBo);
	
	Pagination<DeviceWarningDetailVo> getDeviceWarningDetailPage(DeviceWarningDetailBo deviceWarningDetailBo);
	
	DeviceWarningVo getDeviceWarning(DeviceWarningBo deviceWarningBo);
	/**
	 * 删除设备告警信息（已处理告警信息可以删除）
	 * @param deviceWarningBo
	 * @return
	 */
	Integer deleteDeviceWarning(DeviceWarningBo deviceWarningBo);
}
