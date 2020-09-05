package com.dotop.smartwater.project.module.dao.device;

import com.dotop.smartwater.dependence.core.common.BaseDao;
import com.dotop.smartwater.project.module.core.water.dto.DeviceUplinkDto;
import com.dotop.smartwater.project.module.core.water.dto.customize.QueryParamDto;
import com.dotop.smartwater.project.module.core.water.vo.DeviceUplinkVo;
import com.dotop.smartwater.project.module.core.water.vo.DeviceVo;
import com.dotop.smartwater.project.module.core.water.vo.customize.OriginalVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**

 * @date 2019/2/25.
 */
public interface IDeviceUplinkDao extends BaseDao<DeviceUplinkDto, DeviceUplinkVo> {

	/**
	 * 添加
	 *
	 * @param deviceUplinkDto
	 * @
	 */
	@Override
	void add(DeviceUplinkDto deviceUplinkDto);

	/**
	 * 查询
	 *
	 * @param devid
	 * @param date
	 * @return
	 * @
	 */
	DeviceUplinkVo findLastUplinkWater(@Param("devid") String devid, @Param("date")String date);

	/**
	 * 分页查询
	 *
	 * @param queryParamDto
	 * @return
	 * @
	 */
	List<OriginalVo> findOriginal(QueryParamDto queryParamDto);

	/**
	 * 分页查询
	 *
	 * @param queryParamDto
	 * @return
	 * @
	 */
	List<OriginalVo> findOriginalCrossMonth(QueryParamDto queryParamDto);
	
	/**
	 * 导出上报信息分页查询
	 * @param queryParamDto
	 * @return
	 * @
	 */
	List<OriginalVo> exportFindOriginal(QueryParamDto queryParamDto);

	/**
	 * 导出上报信息跨月分页查询
	 * @param queryParamDto
	 * @return
	 * @
	 */
	List<OriginalVo> exportFindOriginalCrossMonth(QueryParamDto queryParamDto);
	
	
	/**
	 * 设备管理-数据监控（不分页当月查询）
	 * @param queryParamBo
	 * @return
	 */
	List<OriginalVo> findUplinkData(QueryParamDto queryParamDto);
	
	/**
	 * 设备管理-数据监控（不分页跨月查询）
	 * @param queryParamBo
	 * @return
	 */
	List<OriginalVo> findCrossMonthUplinkData(QueryParamDto queryParamDto);

	/**
	 * 分页查询
	 *
	 * @param deviceUplinkDto
	 * @return
	 * @
	 */
	List<DeviceUplinkVo> findDownLink(DeviceUplinkDto deviceUplinkDto);

	/**
	 * 查询设备上行数据 -- lsc
	 *
	 * @param deveui
	 * @param format
	 * @return
	 * @
	 */
	List<DeviceVo> getUplinkData(@Param("deveui") String deveui, @Param("month") String month)
	;

	OriginalVo getOriginalByIdAndDate(@Param("id") String id, @Param("month") String month);

	void insertBatch( @Param("deviceUplinkDtoList")List<DeviceUplinkDto> deviceUplinkDtoList, @Param("thisMonth")String thisMonth);
	
	/**
	 * 批量获取水表读数
	 *
	 * @param deveuis 水表EUI数组
	 * @param systime 上报表时间后缀
	 * @param rxtime 上报时间字符串 y-m-d
	 * @return
	 */
	List<DeviceUplinkVo> batchFindWaterByDeveuis(@Param("deveuis") String deveuis, @Param("systime") String systime, @Param("rxtime") String rxtime);
	
}
