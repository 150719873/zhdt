package com.dotop.pipe.web.api.factory.report;

import java.util.Date;
import java.util.List;

import com.dotop.pipe.core.enums.DateTypeEnum;
import com.dotop.pipe.data.report.core.vo.AreaReportVo;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.pagination.Pagination;

public interface IAreaReportFactory {

	/**
	 * 区域统计
	 * 
	 * @param sensorCode
	 * @param startDate
	 * @param endDate
	 * @param sensorType
	 * @param page
	 * @param pageSize
	 * @param dateType
	 * @param areaId
	 * @return
	 */
	public Pagination<AreaReportVo> getTotalByArea(String sensorCode, Date startDate, Date endDate, String sensorType,
			Integer page, Integer pageSize, DateTypeEnum dateType, String areaId) throws FrameworkRuntimeException;

	/**
	 * 区域报警次数统计
	 * 
	 * @return
	 */
	public List<AreaReportVo> getAreaAlarmReport();

}
