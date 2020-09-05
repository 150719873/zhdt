package com.dotop.smartwater.project.module.service.revenue;

import java.util.List;

import com.dotop.smartwater.dependence.core.common.BaseService;
import com.dotop.smartwater.project.module.core.water.bo.EverydayWaterRecordBo;
import com.dotop.smartwater.project.module.core.water.bo.customize.StatisticsWaterBo;
import com.dotop.smartwater.project.module.core.water.vo.EverydayWaterRecordVo;
import com.dotop.smartwater.project.module.core.water.vo.customize.CommunityDeviceCountVo;
import com.dotop.smartwater.project.module.core.water.vo.customize.CommunityOwnpayVo;
import com.dotop.smartwater.project.module.core.water.vo.customize.CommunityWaterVo;
import com.dotop.smartwater.project.module.core.water.vo.customize.StatisticsVo;
import com.dotop.smartwater.project.module.core.water.vo.customize.StatisticsWaterVo;

/**

 * @date 2019/2/26.
 */
public interface IEverydayWaterRecordService extends BaseService<EverydayWaterRecordBo, EverydayWaterRecordVo> {

	/**
	 * 统计当前水司下每个月小区用水量
	 *
	 * @param enterpriseid
	 * @param ids
	 * @return @
	 */
	List<CommunityWaterVo> getWaterByMonth(String enterpriseid, String ids);

	/**
	 * 月总用水量
	 * @param date
	 * @param enterpriseid
	 * @return
	 */
	Double monthWater(String date,String enterpriseid);
	
	
	/**
	 * 查询当前时间到前一个月的用水量情况（由区域日用水量修改）
	 *
	 * @param statisticsWaterBo
	 * @return @
	 */
	List<StatisticsWaterVo> dailyGetStatisticsWater(StatisticsWaterBo statisticsWaterBo);

	/**
	 * 用水量情况
	 *
	 * @param statisticsWaterBo
	 * @return @
	 */
	List<StatisticsWaterVo> monthGetStatisticsWater(StatisticsWaterBo statisticsWaterBo);

	/**
	 * 当前水司下小区设备数量
	 *
	 * @param enterpriseid
	 * @return @
	 */
	List<CommunityDeviceCountVo> getDeviceCount(String enterpriseid);
	
	
	/**
	 * 统计水司下每种型号设备数量
	 * @return
	 */
	List<StatisticsVo> getDeviceModels(String enterpriseid);
	
	/**
	 * 统计水司下每种设备用途数量
	 * @return
	 */
	List<StatisticsVo> getDevicePurposes(String enterpriseid);
	
	
	/**
	 * 统计水司下每种通讯设备数量
	 * @param enterpriseid
	 * @return
	 */
	List<StatisticsVo> getDeviceModes(String enterpriseid);
	
	/**
	 * 获取水司下预警趋势
	 * @param enterpriseid
	 * @return
	 */
	List<StatisticsVo> getDeviceWarns(String enterpriseid);
	

	/**
	 * 当前水司下每个小区欠费
	 *
	 * @param enterpriseid
	 * @return @
	 */
	List<CommunityOwnpayVo> getCommunityOwnpay(String enterpriseid);

	/**
	 * 插入记录
	 *
	 * @param everydayWaterRecordBo
	 * @return @
	 */
	int insert(EverydayWaterRecordBo everydayWaterRecordBo);

	/**
	 * 添加记录
	 *
	 * @return @
	 */
	int addEveryDayWaterRecord();

	/**
	 * 更新记录
	 *
	 * @return @
	 */
	int updateEveryDayWaterRecord();

}
