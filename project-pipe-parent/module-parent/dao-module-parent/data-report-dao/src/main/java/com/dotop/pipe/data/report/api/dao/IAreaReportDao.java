package com.dotop.pipe.data.report.api.dao;

import java.util.Date;
import java.util.List;

import com.dotop.pipe.data.report.core.vo.AreaReportVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.dao.DataAccessException;

public interface IAreaReportDao {

	List<AreaReportVo> listByRealTime(@Param("operEid") String operEid, @Param("offset") Integer offset,
                                      @Param("limit") Integer limit, @Param("isDel") Integer isDel, @Param("sensorCode") String sensorCode,
                                      @Param("startDate") Date startDate, @Param("endDate") Date endDate, @Param("sensorType") String sensorType,
                                      @Param("areaCode") String areaCode) throws DataAccessException;

	Integer listCountByRealTime(@Param("operEid") String operEid, @Param("isDel") Integer isDel,
			@Param("sensorCode") String sensorCode, @Param("startDate") Date startDate, @Param("endDate") Date endDate,
			@Param("sensorType") String sensorType, @Param("areaCode") String areaCode) throws DataAccessException;

	List<AreaReportVo> getAreaAlarmReport(@Param("operEid") String operEid, @Param("isDel") Integer isDel);
}
