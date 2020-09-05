package com.dotop.smartwater.project.module.dao.revenue;

import com.dotop.smartwater.dependence.core.common.BaseDao;
import com.dotop.smartwater.project.module.core.water.dto.EverydayWaterRecordDto;
import com.dotop.smartwater.project.module.core.water.dto.customize.StatisticsWaterDto;
import com.dotop.smartwater.project.module.core.water.vo.EverydayWaterRecordVo;
import com.dotop.smartwater.project.module.core.water.vo.customize.CommunityDeviceCountVo;
import com.dotop.smartwater.project.module.core.water.vo.customize.CommunityOwnpayVo;
import com.dotop.smartwater.project.module.core.water.vo.customize.CommunityWaterVo;
import com.dotop.smartwater.project.module.core.water.vo.customize.StatisticsVo;
import com.dotop.smartwater.project.module.core.water.vo.customize.StatisticsWaterVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**

 * @date 2019/2/26.
 */
public interface IEverydayWaterRecordDao extends BaseDao<EverydayWaterRecordDto, EverydayWaterRecordVo> {

	/**
	 * 统计当前水司下每个月小区用水量
	 *
	 * @param enterpriseid
	 * @param ids
	 * @return
	 * @
	 */
	List<CommunityWaterVo> getWaterByMonth(@Param("enterpriseid") String enterpriseid, @Param("ids") String ids);

	/**
	 * 统计水司当月总用水量
	 * @param date
	 * @param enterpriseid
	 * @return
	 */
	Double monthWater(@Param("date") String date , @Param("enterpriseid") String enterpriseid);
	
	/**
	 * 当前水司下小区设备数量
	 *
	 * @param enterpriseid
	 * @return
	 * @
	 */
	List<CommunityDeviceCountVo> getDeviceCount(@Param("enterpriseid") String enterpriseid);
	
	
	/**
	 * 统计水司下每种通讯设备数量
	 * @return
	 */
	List<StatisticsVo> getDeviceModes(@Param("enterpriseid") String enterpriseid);
	
	/**
	 * 获取水司下预警趋势
	 * @return
	 */
	List<StatisticsVo> getDeviceWarns(@Param("enterpriseid") String enterpriseid);
	
	/**
	 * 统计水司下每种型号数量
	 * @return
	 */
	List<StatisticsVo> getDeviceModels(@Param("enterpriseid") String enterpriseid);
	
	
	/**
	 * 统计水司下每种用途数量
	 * @return
	 */
	List<StatisticsVo> getDevicePurposes(@Param("enterpriseid") String enterpriseid);
	
	
	/**
	 * 查询当前时间到前一个月的用水量情况（由区域日用水量修改）
	 *
	 * @param statisticsWaterDto
	 * @return
	 */
	List<StatisticsWaterVo> dailyGetStatisticsWater(StatisticsWaterDto statisticsWaterDto);

	/**
	 * 用水量情况
	 *
	 * @param statisticsWaterDto
	 * @return
	 */
	List<StatisticsWaterVo> monthGetStatisticsWater(StatisticsWaterDto statisticsWaterDto);

	/**
	 * 当前水司下每个小区欠费
	 *
	 * @param enterpriseid
	 * @return
	 * @
	 */
	List<CommunityOwnpayVo> getCommunityOwnpay(@Param("enterpriseid") String enterpriseid);

	/**
	 * 插入记录
	 *
	 * @param everydayWaterRecordDto
	 * @return
	 * @
	 */
	int insert(EverydayWaterRecordDto everydayWaterRecordDto);

	/**
	 * 添加记录
	 *
	 * @return
	 * @
	 */
	int addEveryDayWaterRecord();

	/**
	 * 更新记录
	 *
	 * @return
	 * @
	 */
	int updateEveryDayWaterRecord();
}
