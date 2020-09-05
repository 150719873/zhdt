package com.dotop.smartwater.project.module.api.device;

import java.util.List;

import com.dotop.smartwater.dependence.core.common.BaseFactory;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.project.module.core.water.form.DeviceWarningDetailForm;
import com.dotop.smartwater.project.module.core.water.form.DeviceWarningForm;
import com.dotop.smartwater.project.module.core.water.form.OwnerForm;
import com.dotop.smartwater.project.module.core.water.vo.DeviceWarningDetailVo;
import com.dotop.smartwater.project.module.core.water.vo.DeviceWarningVo;

/**
 * 设备预警

 * @date 2019/2/25.
 */
public interface IDeviceWarningFactory extends BaseFactory<DeviceWarningForm, DeviceWarningVo> {

	/**
	 * 获取告警分页
	 * @return 告警列表分页
	 */
	Pagination<DeviceWarningVo> getDeviceWarningPage(DeviceWarningForm deviceWarningForm);

	/**
	 * 添加告警
	 * @param deviceWarningForm 预警对象
	 * @return 结果
	 */
	Integer addDeviceWarning(DeviceWarningForm deviceWarningForm);

	/**
	 * @param ownerForm 业主对象
	 * @return 预警对象分页
	 */
	Pagination<DeviceWarningVo> getLowBattery(OwnerForm ownerForm);

	/**
	 *
	 * @param ownerForm 业主对象
	 * @return 结果
	 */
	String exportLowBattery(OwnerForm ownerForm);

	/**
	 * @param deviceWarningForm 预警对象
	 * @return 结果
	 */
	Integer updateDeviceWarning(DeviceWarningForm deviceWarningForm);

	/**
	 *
	 * @param deviceWarningForm 预警对象
	 * @return 预警处理结果数
	 */
	long warninghandle(DeviceWarningForm deviceWarningForm) ;

	/**
	 *
	 * @param deviceWarningForm 预警对象
	 * @return 预警数
	 */
	long getDeviceWarningCount(DeviceWarningForm deviceWarningForm);

	/**
	 * 获取告警列表
	 * @param deviceWarningForm
	 * @return
	 */
	List<DeviceWarningVo> getDeviceWarningList(DeviceWarningForm deviceWarningForm);

	/**
	 * 获取详情列列表
	 * @param deviceWarningDetailForm
	 * @return
	 */
	Pagination<DeviceWarningDetailVo> getDeviceWarningDetailPage(DeviceWarningDetailForm deviceWarningDetailForm);
	
	DeviceWarningVo getDeviceWarning(DeviceWarningForm deviceWarningForm);
	/**
	 * 删除设备告警信息（已处理告警信息可以删除）
	 * @param deviceWarningForm
	 * @return
	 */
	Integer deleteDeviceWarning(DeviceWarningForm deviceWarningForm);
}
