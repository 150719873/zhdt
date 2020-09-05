package com.dotop.smartwater.project.module.api.revenue;

import java.util.List;
import java.util.Map;

import com.dotop.smartwater.dependence.core.common.BaseFactory;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.project.module.core.water.form.EverydayWaterRecordForm;
import com.dotop.smartwater.project.module.core.water.vo.EverydayWaterRecordVo;
import com.dotop.smartwater.project.module.core.water.vo.customize.CommunityDeviceCountVo;
import com.dotop.smartwater.project.module.core.water.vo.customize.CommunityWaterVo;
import com.dotop.smartwater.project.module.core.water.vo.customize.StatisticsVo;

/**
 * 每日用水量

 * @date 2019/2/26.
 */
public interface IEverydayWaterRecordFactory extends BaseFactory<EverydayWaterRecordForm, EverydayWaterRecordVo> {
	/**
	 * 统计当前水司下每个月小区用水量
	 * @return
	 */
	List<CommunityWaterVo> getWaterByMonth();

	/**
	 * 当前水司下小区设备数量
	 *
	 * @return
	 * @throws FrameworkRuntimeException
	 */
	List<CommunityDeviceCountVo> getDeviceCount();
	
	
	/**
	 * 统计水司下每种型号设备数量
	 * @return
	 */
	List<StatisticsVo> getDeviceModels();
	
	
	/**
	 * 统计水司下每种通讯设备数量
	 * @return
	 */
	List<StatisticsVo> getDeviceModes();
	
	
	/**
	 * 获取水司下预警趋势
	 * @return
	 */
	List<StatisticsVo> getDeviceWarns();
	
	
	/**
	 * 统计水司下每种设备用途数量
	 * @return
	 */
	List<StatisticsVo> getDevicePurposes();
	

	/**
	 * 当前水司下每个小区欠费
	 *
	 * @return
	 * @throws FrameworkRuntimeException
	 */
	Map<String, Object> getCommunityOwnpay();

	/**
	 * 插入记录
	 *
	 * @param everydayWaterRecordForm 用水量
	 * @return 1
	 */
	int insert(EverydayWaterRecordForm everydayWaterRecordForm);

	/**
	 * 添加记录
	 *
	 * @return 1
	 * @throws FrameworkRuntimeException
	 */
	int addEveryDayWaterRecord();

	/**
	 * 更新记录
	 *
	 * @return 1
	 * @throws FrameworkRuntimeException
	 */
	int updateEveryDayWaterRecord();
}
