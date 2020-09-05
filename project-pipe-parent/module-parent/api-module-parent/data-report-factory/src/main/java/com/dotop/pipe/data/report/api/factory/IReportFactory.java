package com.dotop.pipe.data.report.api.factory;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.dotop.pipe.data.report.core.form.AreaReportForm;
import com.dotop.pipe.data.report.core.form.ReportForm;
import com.dotop.pipe.data.report.core.form.VirtualForm;
import com.dotop.pipe.data.report.core.vo.AreaReportVo;
import com.dotop.pipe.data.report.core.vo.RegionReportVo;
import com.dotop.pipe.data.report.core.vo.ReportAreaGroupVo;
import com.dotop.pipe.data.report.core.vo.ReportGroupVo;
import com.dotop.pipe.data.report.core.vo.ReportVo;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.pagination.Pagination;

/**
 *
 * @date 2018/11/2.
 */
public interface IReportFactory {
	/**
	 * 获取设备统计数据
	 *
	 * @param reportForm
	 * @return
	 * @throws FrameworkRuntimeException
	 */
	Map<String, List<ReportGroupVo>> getDeviceReport(ReportForm reportForm) throws FrameworkRuntimeException;

	/**
	 * 获取设备统计分页
	 * 
	 * @param reportForm
	 * @param page
	 * @param pageSize
	 * @return
	 * @throws FrameworkRuntimeException
	 */
	Pagination<ReportVo> getDeviceReportPage(ReportForm reportForm, Integer page, Integer pageSize)
			throws FrameworkRuntimeException;

	/**
	 * 获取设备实时数据
	 * 
	 * @param reportForm
	 * @return
	 * @throws FrameworkRuntimeException
	 */
	List<ReportVo> getDeviceRealTime(ReportForm reportForm) throws FrameworkRuntimeException;

	/**
	 * 区域用水量统计
	 * 
	 * @param areaReportForm
	 * @return
	 * @throws FrameworkRuntimeException
	 */
	List<ReportAreaGroupVo> getAreaReport(AreaReportForm areaReportForm) throws FrameworkRuntimeException;

	/**
	 * 虚拟流量计
	 * 
	 * @param virtualForm
	 * @return
	 * @throws FrameworkRuntimeException
	 */
	List<ReportVo> getVirtualFlow(VirtualForm virtualForm) throws FrameworkRuntimeException;

	/**
	 * 片区用水量统计
	 * 
	 * @param reportForm
	 * @param page
	 * @param pageSize
	 * @return
	 */
	Pagination<RegionReportVo> getRegionReportPage(ReportForm reportForm, Integer page, Integer pageSize);

	/**
	 * 区域抄表信息统计
	 * 
	 * @param startDate
	 * @param endDate
	 * @param areaIds
	 * @return
	 */
	public List<AreaReportVo> getAreaReportByReading(Date startDate, Date endDate, List<String> areaIds);

	/**
	 * 分区分级查看流量计设备
	 * 
	 * @param startDate
	 * @param endDate
	 * @param regionList
	 * @return
	 */
	List<RegionReportVo> getRegionRealTimeReport(Date startDate, Date endDate, List<String> regionList);

	/**
	 * 区域实时统计
	 * @param startDate
	 * @param endDate
	 * @param regionList
	 * @return
	 */
	List<AreaReportVo> getAreaRealTimeReport(Date startDate, Date endDate, List<String> regionList);

}
