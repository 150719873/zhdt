package com.dotop.pipe.data.report.api.service;

import java.util.Date;

import com.dotop.pipe.data.report.core.vo.SensorReportVo;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.pagination.Pagination;

public interface ISensorReportService {

	/**
	 * 时统计
	 * 
	 * @param sensorCode
	 * @param startDate
	 * @param endDate
	 * @param sensorType
	 * @param operEid
	 * @param userBy
	 * @param page
	 * @param pageSize
	 * @return
	 * @throws FrameworkRuntimeException
	 */
	Pagination<SensorReportVo> getTotalByHour(String sensorCode, Date startDate, Date endDate, String sensorType,
			String operEid, String userBy, Integer page, Integer pageSize) throws FrameworkRuntimeException;

	Pagination<SensorReportVo> getTotalByYear(String sensorCode, Date startDate, Date endDate, String sensorType,
			String operEid, String userBy, Integer page, Integer pageSize) throws FrameworkRuntimeException;

	Pagination<SensorReportVo> getTotalByMonth(String sensorCode, Date startDate, Date endDate, String sensorType,
			String operEid, String userBy, Integer page, Integer pageSize) throws FrameworkRuntimeException;

	Pagination<SensorReportVo> getTotalByDay(String sensorCode, Date startDate, Date endDate, String sensorType,
			String operEid, String userBy, Integer page, Integer pageSize) throws FrameworkRuntimeException;

	Pagination<SensorReportVo> getTotalByRealTime(String sensorCode, Date startDate, Date endDate, String sensorType,
			String operEid, String userBy, Integer page, Integer pageSize) throws FrameworkRuntimeException;

	Pagination<SensorReportVo> getSensorWaterAmount(String sensorCode, Date startDate, Date endDate, String operEid,
			String userBy, Integer page, Integer pageSize) throws FrameworkRuntimeException;

	Pagination<SensorReportVo> getVirtualFlowList(Date startDate, Date endDate, String operEid, Integer page,
			Integer pageSize, String string, String string2) throws FrameworkRuntimeException;

	Pagination<SensorReportVo> getWaterQuality(String sensorType, String sensorCode, Date startDate, Date endDate,
			String operEid, String userBy, Integer page, Integer pageSize) throws FrameworkRuntimeException;

}
