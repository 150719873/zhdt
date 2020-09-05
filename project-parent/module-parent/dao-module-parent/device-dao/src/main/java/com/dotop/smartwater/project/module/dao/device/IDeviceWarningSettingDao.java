package com.dotop.smartwater.project.module.dao.device;

import com.dotop.smartwater.dependence.core.common.BaseDao;
import com.dotop.smartwater.project.module.core.water.dto.DeviceWarningSettingDto;
import com.dotop.smartwater.project.module.core.water.vo.DeviceWarningSettingVo;

import java.util.List;

/**

 * @date 2019/4/1.
 */
public interface IDeviceWarningSettingDao extends BaseDao<DeviceWarningSettingDto, DeviceWarningSettingVo> {

	@Override
	List<DeviceWarningSettingVo> list(DeviceWarningSettingDto deviceWarningSettingDto);

	@Override
	Integer edit(DeviceWarningSettingDto deviceWarningSettingDto);
}
