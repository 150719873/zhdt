package com.dotop.pipe.data.report.api.service;

import java.util.Date;
import java.util.List;
import java.util.Set;

import com.dotop.pipe.core.enums.DateTypeEnum;
import com.dotop.pipe.core.enums.FieldTypeEnum;
import com.dotop.pipe.core.vo.area.AreaModelVo;
import com.dotop.pipe.data.report.core.vo.AreaReportVo;
import com.dotop.pipe.data.report.core.vo.RegionReportVo;
import com.dotop.pipe.data.report.core.vo.ReportAreaGroupVo;
import com.dotop.pipe.data.report.core.vo.ReportVo;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.pagination.Pagination;

/**
 *
 * @date 2018/11/2.
 */
public interface IReportService {

	/**
	 * 获取设备统计数据
	 * 
	 * @param deviceIds
	 * @param dateType
	 * @param fieldType
	 * @param startDate
	 * @param endDate
	 * @return
	 * @throws FrameworkRuntimeException
	 */
	List<ReportVo> getDeviceReport(String operEid, List<String> deviceIds, DateTypeEnum dateType,
			FieldTypeEnum[] fieldType, Date startDate, Date endDate, Set<String> ctimes)
			throws FrameworkRuntimeException;

	/**
	 * 设备统计分页查询
	 * 
	 * @param deviceIds
	 * @param dateType
	 * @param fieldType
	 * @param startDate
	 * @param endDate
	 * @param page
	 * @param pageSize
	 * @return
	 * @throws FrameworkRuntimeException
	 */
	Pagination<ReportVo> getDeviceReportPage(String operEid, List<String> deviceIds, DateTypeEnum dateType,
			FieldTypeEnum[] fieldType, Date startDate, Date endDate, Set<String> ctimes, Integer page, Integer pageSize)
			throws FrameworkRuntimeException;

	/**
	 * 获取设备实时数据
	 * 
	 * @param deviceIds
	 * @param fieldType
	 * @return
	 * @throws FrameworkRuntimeException
	 */
	List<ReportVo> getDeviceRealTime(String operEid, List<String> deviceIds, FieldTypeEnum[] fieldType)
			throws FrameworkRuntimeException;

	/**
	 * 区域用水量
	 * 
	 * @param areaIds
	 * @param dateType
	 * @param startDate
	 * @param endDate
	 * @return
	 * @throws FrameworkRuntimeException
	 */
	List<ReportAreaGroupVo> getAreaReport(String enterpriseId, List<String> areaIds, DateTypeEnum dateTypeEnum,
			Date startDate, Date endDate, Set<String> ctimes) throws FrameworkRuntimeException;

	/**
	 * 虚拟流量
	 * 
	 * @param positiveIds
	 * @param reverseIds
	 * @param startDate
	 * @param endDate
	 * @param ctimes
	 * @return
	 * @throws FrameworkRuntimeException
	 */
	List<ReportVo> getVirtualFlow(String operEid, List<String> positiveIds, List<String> reverseIds, Date startDate,
			Date endDate, Set<String> ctimes) throws FrameworkRuntimeException;

	/**
	 * 片区用水量统计
	 * 
	 * @param operEid
	 * @param deviceIds
	 * @param dateTypeEnum
	 * @param startDate
	 * @param endDate
	 * @param ctimes
	 * @param page
	 * @param pageSize
	 * @return
	 */
	Pagination<RegionReportVo> getRegionReportPage(String operEid, List<String> deviceIds, DateTypeEnum dateTypeEnum,
			Date startDate, Date endDate, Set<String> ctimes, Integer page, Integer pageSize);

	/**
	 * 区域抄表信息统计
	 * 
	 * @param startDate
	 * @param endDate
	 * @param ctimes
	 * @param operEid
	 * @param arealist
	 * @return
	 */
	List<AreaReportVo> getAreaReportByReading(Date startDate, Date endDate, Set<String> ctimes, String operEid,
			List<AreaModelVo> arealist);

	/**
	 * 分区分级 报表统计
	 * 
	 * @param startDate
	 * @param endDate
	 * @param ctimes
	 * @param operEid
	 * @return
	 */
	List<RegionReportVo> getRegionRealTimeReport(Date startDate, Date endDate, Set<String> ctimes, String operEid);

	/**
	 * 区域实时报表统计
	 * 
	 * @param startDate
	 * @param endDate
	 * @param ctimes
	 * @param operEid
	 * @return
	 */
	List<AreaReportVo> getAreaRealTimeReport(Date startDate, Date endDate, Set<String> ctimes, String operEid);
}
