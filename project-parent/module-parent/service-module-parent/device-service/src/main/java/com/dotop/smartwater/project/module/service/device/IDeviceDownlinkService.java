package com.dotop.smartwater.project.module.service.device;

import java.util.List;

import com.dotop.smartwater.dependence.core.common.BaseService;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.project.module.core.water.bo.DeviceDownlinkBo;
import com.dotop.smartwater.project.module.core.water.bo.customize.QueryParamBo;
import com.dotop.smartwater.project.module.core.water.vo.DeviceDownlinkVo;

/**

 * @date 2019/2/25.
 */
public interface IDeviceDownlinkService extends BaseService<DeviceDownlinkBo, DeviceDownlinkVo> {
	/**
	 * 添加
	 *
	 * @param deviceDownlinkBo
	 * @return @
	 */
	@Override
	DeviceDownlinkVo add(DeviceDownlinkBo deviceDownlinkBo);

	/**
	 * 根据clientid查询
	 *
	 * @param deviceDownlinkBo
	 * @return
	 */
	List<DeviceDownlinkVo> findByClientId(DeviceDownlinkBo deviceDownlinkBo);

	/**
	 * 更新
	 *
	 * @param deviceDownlinkBo
	 */
	void update(DeviceDownlinkBo deviceDownlinkBo);

	@Override
	DeviceDownlinkVo get(DeviceDownlinkBo deviceDownlinkBo);

	@Override
	Pagination<DeviceDownlinkVo> page(DeviceDownlinkBo deviceDownlinkBo);

	Pagination<DeviceDownlinkVo> getHistory(QueryParamBo queryParamBo);

	DeviceDownlinkVo getLastDownLink(String devid);
}
