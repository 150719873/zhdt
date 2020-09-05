package com.dotop.pipe.data.report.api.dao;

import java.util.Date;
import java.util.List;

import com.dotop.pipe.data.report.core.vo.SensorReportVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.dao.DataAccessException;

public interface ISensorReportDao {

	List<SensorReportVo> listByHour(@Param("operEid") String operEid, @Param("offset") Integer offset,
                                    @Param("limit") Integer limit, @Param("isDel") Integer isDel, @Param("sensorCode") String sensorCode,
                                    @Param("startDate") Date startDate, @Param("endDate") Date endDate, @Param("sensorType") String sensorType)
			throws DataAccessException;

	Integer listCountByHour(@Param("operEid") String operEid, @Param("isDel") Integer isDel,
			@Param("sensorCode") String sensorCode, @Param("startDate") Date startDate, @Param("endDate") Date endDate,
			@Param("sensorType") String sensorType) throws DataAccessException;

	List<SensorReportVo> listByRealTime(@Param("operEid") String operEid, @Param("offset") Integer offset,
			@Param("limit") Integer limit, @Param("isDel") Integer isDel, @Param("sensorCode") String sensorCode,
			@Param("startDate") Date startDate, @Param("endDate") Date endDate, @Param("sensorType") String sensorType)
			throws DataAccessException;

	Integer listCountByRealTime(@Param("operEid") String operEid, @Param("isDel") Integer isDel,
			@Param("sensorCode") String sensorCode, @Param("startDate") Date startDate, @Param("endDate") Date endDate,
			@Param("sensorType") String sensorType) throws DataAccessException;

	List<SensorReportVo> listByDay(@Param("operEid") String operEid, @Param("offset") Integer offset,
			@Param("limit") Integer limit, @Param("isDel") Integer isDel, @Param("sensorCode") String sensorCode,
			@Param("startDate") Date startDate, @Param("endDate") Date endDate, @Param("sensorType") String sensorType)
			throws DataAccessException;

	Integer listCountByDay(@Param("operEid") String operEid, @Param("isDel") Integer isDel,
			@Param("sensorCode") String sensorCode, @Param("startDate") Date startDate, @Param("endDate") Date endDate,
			@Param("sensorType") String sensorType) throws DataAccessException;

	List<SensorReportVo> listByMonth(@Param("operEid") String operEid, @Param("offset") Integer offset,
			@Param("limit") Integer limit, @Param("isDel") Integer isDel, @Param("sensorCode") String sensorCode,
			@Param("startDate") Date startDate, @Param("endDate") Date endDate, @Param("sensorType") String sensorType)
			throws DataAccessException;

	Integer listCountByMonth(@Param("operEid") String operEid, @Param("isDel") Integer isDel,
			@Param("sensorCode") String sensorCode, @Param("startDate") Date startDate, @Param("endDate") Date endDate,
			@Param("sensorType") String sensorType) throws DataAccessException;

	List<SensorReportVo> listByYear(@Param("operEid") String operEid, @Param("offset") Integer offset,
			@Param("limit") Integer limit, @Param("isDel") Integer isDel, @Param("sensorCode") String sensorCode,
			@Param("startDate") Date startDate, @Param("endDate") Date endDate, @Param("sensorType") String sensorType)
			throws DataAccessException;

	Integer listCountByYear(@Param("operEid") String operEid, @Param("isDel") Integer isDel,
			@Param("sensorCode") String sensorCode, @Param("startDate") Date startDate, @Param("endDate") Date endDate,
			@Param("sensorType") String sensorType) throws DataAccessException;

	/**
	 * 
	 * 传感器用水量 统计
	 * 
	 * @param operEid
	 * @param offset
	 * @param limit
	 * @param isDel
	 * @param sensorCode
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	List<SensorReportVo> getSensorWaterAmount(@Param("operEid") String operEid, @Param("offset") Integer offset,
			@Param("limit") Integer limit, @Param("isDel") Integer isDel, @Param("sensorCode") String sensorCode,
			@Param("startDate") Date startDate, @Param("endDate") Date endDate);

	/**
	 * 传感器用水量条数统计
	 * 
	 * @param operEid
	 * @param isDel
	 * @param sensorCode
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	Integer getSensorWaterCount(@Param("operEid") String operEid, @Param("isDel") Integer isDel,
			@Param("sensorCode") String sensorCode, @Param("startDate") Date startDate, @Param("endDate") Date endDate);

	/**
	 * 虚拟流量计查询
	 * 
	 * @param operEid
	 * @param offset
	 * @param limit
	 * @param isDel
	 * @param startDate
	 * @param endDate
	 * @param positiveValue
	 * @param reverseValue
	 * @return
	 */
	SensorReportVo getVirtualFlowList(@Param("operEid") String operEid, @Param("offset") Integer offset,
			@Param("limit") Integer limit, @Param("isDel") Integer isDel, @Param("startDate") Date startDate,
			@Param("endDate") Date endDate, @Param("value") String positiveValue);

	/**
	 * 水质计
	 * @param operEid
	 * @param offset
	 * @param limit
	 * @param isDel
	 * @param sensorCode
	 * @param sensorType
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	
	List<SensorReportVo> getWaterQuality(@Param("operEid") String operEid, @Param("offset") Integer offset,
			@Param("limit") Integer limit, @Param("isDel") Integer isDel, @Param("sensorCode") String sensorCode,
			@Param("sensorType") String sensorType, @Param("startDate") Date startDate, @Param("endDate") Date endDate);

	Integer getWaterQualityCount(@Param("operEid") String operEid, @Param("isDel") Integer isDel,
			@Param("sensorCode") String sensorCode, @Param("sensorType") String sensorType,
			@Param("startDate") Date startDate, @Param("endDate") Date endDate);

}
