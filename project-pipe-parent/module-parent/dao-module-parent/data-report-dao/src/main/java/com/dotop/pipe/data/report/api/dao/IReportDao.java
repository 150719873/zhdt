package com.dotop.pipe.data.report.api.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.dotop.pipe.data.report.core.dto.report.ReportDto;
import com.dotop.pipe.data.report.core.vo.AreaReportVo;
import com.dotop.pipe.data.report.core.vo.RegionReportVo;
import com.dotop.pipe.data.report.core.vo.ReportAreaGroupVo;
import com.dotop.pipe.data.report.core.vo.ReportVo;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;
import org.springframework.dao.DataAccessException;

/**
 *
 * @date 2018/11/2.
 */
public interface IReportDao {

	/**
	 * 获取设备统计数据
	 * 
	 * @param reportDto
	 * @return
	 * @throws DataAccessException
	 */
	List<ReportVo> getDeviceReport(ReportDto reportDto) throws DataAccessException;

	/**
	 * 获取设备实时数据
	 * 
	 * @param reportDto
	 * @return
	 * @throws DataAccessException
	 */
	List<ReportVo> getDeviceRealTime(ReportDto reportDto) throws DataAccessException;

	/**
	 * 区域用水量
	 * 
	 * @param areaId
	 * @param dateType
	 * @param startDate
	 * @param endDate
	 * @param string
	 * @return
	 * @throws DataAccessException
	 */
	List<ReportAreaGroupVo> getAreaReport(ReportDto reportDto) throws DataAccessException;

	/**
	 * 虚拟流量
	 * 
	 * @param isDel
	 * @param enterpriseId
	 * 
	 * @param positiveIds
	 * @param reverseIds
	 * @param startDate
	 * @param endDate
	 * @param string
	 * @return
	 */
	List<ReportVo> getVirtualFlow(@Param("enterpriseId") String enterpriseId, @Param("isDel") Integer isDel,
			@Param("positiveIds") List<String> positiveIds, @Param("reverseIds") List<String> reverseIds,
			@Param("startDate") Date startDate, @Param("endDate") Date endDate, @Param("ctimes") Set<String> ctimes)
			throws DataAccessException;

	/**
	 * 片区用水量报表
	 * 
	 * @param reportDto
	 * @return
	 */
	List<RegionReportVo> getRegionReportPage(ReportDto reportDto);

	/**
	 * 区域用水量实时统计
	 * 
	 * @param reportDto
	 * @return
	 */
	List<AreaReportVo> getAreaReportPage(ReportDto reportDto);

	/**
	 * 返回的是每个设备在一段时间内的最新值
	 * 
	 * @param reportDto
	 * @return
	 */
	@MapKey("deviceId")
	Map<String, ReportVo> queryDeviceDateLog(ReportDto reportDto);

	/**
	 * 返回的是每个设备在一段时间内的一组值
	 * 
	 * @param reportDto
	 * @return
	 */
	List<ReportVo> queryDeviceDateLogs(ReportDto reportDto);

}
