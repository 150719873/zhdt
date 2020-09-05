package com.dotop.pipe.data.report.api.service;

import java.util.Date;
import java.util.List;

import com.dotop.pipe.data.report.core.vo.AreaReportVo;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.pagination.Pagination;

public interface IAreaReportService {

	/**
	 * 实时统计
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
	Pagination<AreaReportVo> getTotalByRealTime(String sensorCode, Date startDate, Date endDate, String sensorType,
			String operEid, String userBy, Integer page, Integer pageSize, String areaCode)
			throws FrameworkRuntimeException;

	List<AreaReportVo> getAreaAlarmReport(String operEid);
}
