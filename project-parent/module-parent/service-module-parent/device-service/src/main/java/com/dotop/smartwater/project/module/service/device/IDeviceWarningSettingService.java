package com.dotop.smartwater.project.module.service.device;

import java.util.List;

import com.dotop.smartwater.dependence.core.common.BaseService;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.project.module.core.water.bo.DeviceWarningSettingBo;
import com.dotop.smartwater.project.module.core.water.vo.DeviceWarningSettingVo;

/**

 * @date 2019/4/1.
 */
public interface IDeviceWarningSettingService extends BaseService<DeviceWarningSettingBo, DeviceWarningSettingVo> {

	@Override
	Pagination<DeviceWarningSettingVo> page(DeviceWarningSettingBo deviceWarningSettingBo);

	@Override
	DeviceWarningSettingVo edit(DeviceWarningSettingBo deviceWarningSettingBo);

	@Override
	List<DeviceWarningSettingVo> list(DeviceWarningSettingBo deviceWarningSettingBo);
}
