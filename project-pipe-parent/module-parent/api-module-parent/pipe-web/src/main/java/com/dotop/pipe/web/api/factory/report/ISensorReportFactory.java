package com.dotop.pipe.web.api.factory.report;

import java.util.Date;

import com.dotop.pipe.data.report.core.vo.SensorReportVo;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.pagination.Pagination;

public interface ISensorReportFactory {

	/**
	 * 实时统计
	 * 
	 * @param sensorCode
	 * @param startDate
	 * @param endDate
	 * @param sensorType
	 * @param page
	 * @param pageSize
	 * @return
	 */
	public Pagination<SensorReportVo> getTotalByRealTime(String sensorCode, Date startDate, Date endDate,
			String sensorType, Integer page, Integer pageSize) throws FrameworkRuntimeException;

	/**
	 * 时统计
	 * 
	 * @param sensorCode
	 * @param startDate
	 * @param endDate
	 * @param sensorType
	 * @param page
	 * @param pageSize
	 * @return
	 */
	public Pagination<SensorReportVo> getTotalByHour(String sensorCode, Date startDate, Date endDate, String sensorType,
			Integer page, Integer pageSize) throws FrameworkRuntimeException;

	/**
	 * 天统计
	 * 
	 * @param sensorCode
	 * @param startDate
	 * @param endDate
	 * @param sensorType
	 * @param page
	 * @param pageSize
	 * @return
	 */
	public Pagination<SensorReportVo> getTotalByDay(String sensorCode, Date startDate, Date endDate, String sensorType,
			Integer page, Integer pageSize) throws FrameworkRuntimeException;

	/**
	 * 月统计
	 * 
	 * @param sensorCode
	 * @param startDate
	 * @param endDate
	 * @param sensorType
	 * @param page
	 * @param pageSize
	 * @return
	 */
	public Pagination<SensorReportVo> getTotalByMonth(String sensorCode, Date startDate, Date endDate,
			String sensorType, Integer page, Integer pageSize) throws FrameworkRuntimeException;

	/**
	 * 年统计
	 * 
	 * @param sensorCode
	 * @param startDate
	 * @param endDate
	 * @param sensorType
	 * @param page
	 * @param pageSize
	 * @return
	 */
	public Pagination<SensorReportVo> getTotalByYear(String sensorCode, Date startDate, Date endDate, String sensorType,
			Integer page, Integer pageSize) throws FrameworkRuntimeException;

	/**
	 * 水量统计报表
	 * 
	 * @param sensorCode
	 * @param startDate
	 * @param endDate
	 * @param page
	 * @param pageSize
	 * @return
	 */
	public Pagination<SensorReportVo> getSensorWaterAmount(String sensorCode, Date startDate, Date endDate,
			Integer page, Integer pageSize) throws FrameworkRuntimeException;

	/**
	 * 查询虚拟流量计
	 * 
	 * @param startDate
	 * @param endDate
	 * @param page
	 * @param pageSize
	 * @param positiveValues
	 * @param reverseValues
	 * @return
	 */
	public Pagination<SensorReportVo> getVirtualFlowList(Date startDate, Date endDate, Integer page, Integer pageSize,
			String[] positiveValues, String[] reverseValues) throws FrameworkRuntimeException;

	/**
	 * 水质计
	 * 
	 * @param sensorCode
	 * @param startDate
	 * @param endDate
	 * @param page
	 * @param pageSize
	 * @return
	 */
	public Pagination<SensorReportVo> getWaterQuality(String sensorCode, Date startDate, Date endDate, Integer page,
			Integer pageSize) throws FrameworkRuntimeException;

}
