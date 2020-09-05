package com.dotop.pipe.data.receiver.api.dao;

import java.util.Date;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.dao.DataAccessException;

import com.dotop.pipe.core.vo.device.DevicePropertyVo;

public interface IDevicePropertyDao {

	DevicePropertyVo get(@Param("deviceId") String deviceId, @Param("field") String field,
			@Param("isDel") Integer isDel) throws DataAccessException;

	void add(@Param("devProId") String devProId, @Param("deviceId") String deviceId,
			@Param("deviceCode") String deviceCode, @Param("field") String field, @Param("tag") String tag,
			@Param("name") String name, @Param("unit") String unit, @Param("val") String val,
			@Param("devSendDate") Date devSendDate, @Param("serReceDate") Date serReceDate, @Param("curr") Date curr,
			@Param("userBy") String userBy, @Param("isDel") Integer isDel, @Param("enterpriseId") String enterpriseId)
			throws DataAccessException;

	void upd(@Param("devProId") String devProId, @Param("tag") String tag, @Param("name") String name,
			@Param("unit") String unit, @Param("val") String val, @Param("devSendDate") Date devSendDate,
			@Param("serReceDate") Date serReceDate, @Param("curr") Date curr, @Param("userBy") String userBy,
			@Param("isDel") Integer isDel, @Param("enterpriseId") String enterpriseId);

	void uptProperty(@Param("dataMap") Map<String, Object> dataMap, @Param("enterpriseId") String enterpriseId,
			@Param("deviceId") String deviceId);

	void addProperty(@Param("dataMap") Map<String, Object> dataMap);

	boolean isExist(@Param("enterpriseId") String enterpriseId, @Param("deviceId") String deviceId);

}
