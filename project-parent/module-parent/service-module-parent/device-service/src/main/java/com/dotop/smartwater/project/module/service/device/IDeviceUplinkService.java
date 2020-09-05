package com.dotop.smartwater.project.module.service.device;

import java.util.List;

import com.dotop.smartwater.dependence.core.common.BaseService;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.project.module.core.water.bo.DeviceBo;
import com.dotop.smartwater.project.module.core.water.bo.DeviceUplinkBo;
import com.dotop.smartwater.project.module.core.water.bo.customize.QueryParamBo;
import com.dotop.smartwater.project.module.core.water.vo.DeviceUplinkVo;
import com.dotop.smartwater.project.module.core.water.vo.DeviceVo;
import com.dotop.smartwater.project.module.core.water.vo.customize.OriginalVo;

/**

 * @date 2019/2/25.
 */
public interface IDeviceUplinkService extends BaseService<DeviceUplinkBo, DeviceUplinkVo> {

	/**
	 * 添加
	 *
	 * @param deviceUplinkBo
	 * @return @
	 */
	@Override
	DeviceUplinkVo add(DeviceUplinkBo deviceUplinkBo);

	/**
	 * 查询
	 *
	 * @param devid
	 * @param date
	 * @return @
	 */
	DeviceUplinkVo findLastUplinkWater(String devid, String date);

	/**
	 * 分页查询
	 *
	 * @param queryParamBo
	 * @return @
	 */
	Pagination<OriginalVo> findOriginal(QueryParamBo queryParamBo);

	/**
	 * 分页查询
	 *
	 * @param queryParamBo
	 * @return @
	 */
	Pagination<OriginalVo> findOriginalCrossMonth(QueryParamBo queryParamBo);
	
	/**
	 * 导出上报信息分页查询
	 *
	 * @param queryParamBo
	 * @return @
	 */
	Pagination<OriginalVo> exportFindOriginal(QueryParamBo queryParamBo);

	/**
	 * 导出上报信息跨月分页查询
	 *
	 * @param queryParamBo
	 * @return @
	 */
	Pagination<OriginalVo> exportFindOriginalCrossMonth(QueryParamBo queryParamBo);
	
	
	/**
	 * 设备管理-数据监控（不分页当月查询）
	 * @param queryParamBo
	 * @return
	 */
	List<OriginalVo> findUplinkData(QueryParamBo queryParamBo);
	
	
	/**
	 * 设备管理-数据监控（不分页跨月查询）
	 * @param queryParamBo
	 * @return
	 */
	List<OriginalVo> findCrossMonthUplinkData(QueryParamBo queryParamBo);
	

	/**
	 * 分页查询
	 *
	 * @param deviceBo
	 * @param start
	 * @param end
	 * @return @
	 */
	Pagination<DeviceUplinkVo> findDownLink(DeviceBo deviceBo, String start, String end);

	/**
	 * 查询设备上行数据 -- lsc
	 *
	 * @param deveui
	 * @return
	 */
	List<DeviceVo> getUplinkData(String deveui, String month);

	OriginalVo getOriginalByIdAndDate(String id, String month);
	
	/**
	 * 批量获取水表读数
	 *
	 * @param deveuis 水表EUI数组
	 * @param systime 上报表时间后缀
	 * @param rxtime 上报时间字符串 y-m-d
	 * @return
	 */
	List<DeviceUplinkVo> batchFindWaterByDeveuis(String deveuis, String systime, String rxtime);
	
}
