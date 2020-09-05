package com.dotop.pipe.data.receiver.api.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.dotop.pipe.core.vo.device.DevicePropertyLogVo;

public interface IDevicePropertyLogDao {

	void add(@Param("logId") String logId, @Param("deviceId") String deviceId, @Param("deviceCode") String deviceCode,
			@Param("field") String field, @Param("tag") String tag, @Param("name") String name,
			@Param("unit") String unit, @Param("val") String val, @Param("devSendYear") String devSendYear,
			@Param("devSendMonth") String devSendMonth, @Param("devSendDay") String devSendDay,
			@Param("devSendHour") String devSendHour, @Param("devSendDate") Date devSendDate,
			@Param("serReceDate") Date serReceDate, @Param("curr") Date curr, @Param("userBy") String userBy,
			@Param("isDel") Integer isDel, @Param("ctime") String ctime, @Param("enterpriseId") String enterpriseId);

	List<DevicePropertyLogVo> list(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

	void addProperty(@Param("propertyLogMap") Map<String, Object> propertyLogMap);
}
