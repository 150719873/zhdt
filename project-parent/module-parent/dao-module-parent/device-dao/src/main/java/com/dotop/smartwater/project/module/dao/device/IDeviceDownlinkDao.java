package com.dotop.smartwater.project.module.dao.device;

import com.dotop.smartwater.dependence.core.common.BaseDao;
import com.dotop.smartwater.project.module.core.water.dto.DeviceDownlinkDto;
import com.dotop.smartwater.project.module.core.water.dto.customize.QueryParamDto;
import com.dotop.smartwater.project.module.core.water.vo.DeviceDownlinkVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 数据下行记录

 * @date 2019/2/25.
 */
public interface IDeviceDownlinkDao extends BaseDao<DeviceDownlinkDto, DeviceDownlinkVo> {

	/**
	 * 添加
	 * @param deviceDownlinkDto
	 */
	@Override
	void add(DeviceDownlinkDto deviceDownlinkDto);

	/**
	 * 根据clientid查询
	 * @param deviceDownlinkDto
	 * @return
	 */
	List<DeviceDownlinkVo> findByClientId(DeviceDownlinkDto deviceDownlinkDto);

	/**
	 * 更新
	 * @param deviceDownlinkDto
	 */
	void update(DeviceDownlinkDto deviceDownlinkDto);

	@Override
	DeviceDownlinkVo get(DeviceDownlinkDto deviceDownlinkDto);

	@Override
	List<DeviceDownlinkVo> list(DeviceDownlinkDto deviceDownlinkDto);

	List<DeviceDownlinkVo> getHistory(QueryParamDto queryParamDto);

	DeviceDownlinkVo getLastDownLink(@Param("devid") String devid);
}
