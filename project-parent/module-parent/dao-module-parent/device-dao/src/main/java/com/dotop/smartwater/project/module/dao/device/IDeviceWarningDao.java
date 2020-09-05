package com.dotop.smartwater.project.module.dao.device;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.dotop.smartwater.dependence.core.common.BaseDao;
import com.dotop.smartwater.project.module.core.water.dto.DeviceWarningDetailDto;
import com.dotop.smartwater.project.module.core.water.dto.DeviceWarningDto;
import com.dotop.smartwater.project.module.core.water.dto.OwnerDto;
import com.dotop.smartwater.project.module.core.water.vo.DeviceWarningDetailVo;
import com.dotop.smartwater.project.module.core.water.vo.DeviceWarningVo;

/**
 * 设备告警
 *

 * @date 2019/2/25.

 * @date 2019-11-19 15:59
 */
public interface IDeviceWarningDao extends BaseDao<DeviceWarningDto, DeviceWarningVo> {

	/**
	 * 获取告警列表
	 *
	 * @param deviceWarningDto
	 * @return
	 */
	List<DeviceWarningVo> getDeviceWarningList(DeviceWarningDto deviceWarningDto);
	
	/**
	 * 添加告警
	 *
	 * @param deviceWarningDto
	 * @return
	 */
	Integer addDeviceWarning(DeviceWarningDto deviceWarningDto);

	/**
	 * 查找告警(可根据devid、devno、deveui查找告警)
	 *
	 * @param deviceWarningDto
	 * @return
	 */
	DeviceWarningVo findByDevice(DeviceWarningDto deviceWarningDto);
	
	/**
	 * 处理告警
	 * @param deviceWarningDto
	 * @return
	 */
	Integer handleWarning(@Param("handler") String handler, @Param("list") List<String> ids);

	/**
	 * 更新告警
	 *
	 * @param deviceWarningDto
	 * @return
	 */
	Integer updateDeviceWarning(DeviceWarningDto deviceWarningDto);
	
	/**
	 * 获取预警条数
	 * @param deviceWarningDto
	 * @return
	 */
	Long getDeviceWarningCount(DeviceWarningDto deviceWarningDto);

	/**
	 * 获取告警列表
	 *
	 * @param ownerDto
	 * @return
	 */
	List<DeviceWarningVo> getDeviceWarningList(OwnerDto ownerDto);

	/**
	 * 删除设备告警信息（只能删除已处理告警信息）
	 * @param deviceWarningDto
	 * @return
	 */
	Integer deleteDeviceWarning(DeviceWarningDto deviceWarningDto);
	
	/**
	 * 添加告警详情
	 * @param deviceWarningDetailDto
	 * @return
	 */
	Integer addDeviceWarningDetail(DeviceWarningDetailDto deviceWarningDetailDto);
	
	/**
	 * 获取设备告警详情列表
	 * @param deviceWarningDetailDto
	 * @return
	 */
	List<DeviceWarningDetailVo> getDeviceWarningDetailList(DeviceWarningDetailDto deviceWarningDetailDto);
	
	DeviceWarningVo getDeviceWarning(DeviceWarningDto deviceWarningDto);
	
	/**
	 * 删除设备告警详情
	 * @param deviceWarningDetailDto
	 * @return
	 */
	Integer deleteDeviceWarningDetail(DeviceWarningDetailDto deviceWarningDetailDto);
}
