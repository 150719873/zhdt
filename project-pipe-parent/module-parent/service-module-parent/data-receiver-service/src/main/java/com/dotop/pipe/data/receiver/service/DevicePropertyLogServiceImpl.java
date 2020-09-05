package com.dotop.pipe.data.receiver.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.dotop.pipe.data.receiver.api.dao.IDevicePropertyLogDao;
import com.dotop.pipe.data.receiver.api.service.IDevicePropertyLogService;
import com.dotop.pipe.data.receiver.core.constants.FieldConstants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.dotop.pipe.core.vo.device.DevicePropertyLogVo;
import com.dotop.smartwater.dependence.core.common.RootModel;
import com.dotop.smartwater.dependence.core.exception.BaseExceptionConstants;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.utils.UuidUtils;

@Service
public class DevicePropertyLogServiceImpl implements IDevicePropertyLogService {

	private final static Logger logger = LogManager.getLogger(DevicePropertyLogServiceImpl.class);

	@Autowired
	private IDevicePropertyLogDao iDevicePropertyLogDao;

	@Override
	public void add(String deviceId, String deviceCode, String field, String tag, String name, String unit, String val,
			Date devSendDate, Date serReceDate, Date curr, String userBy, String enterpriseId)
			throws FrameworkRuntimeException {
		try {
			String logId = UuidUtils.getUuid();
			Integer isDel = RootModel.NOT_DEL;
			DateTime td = new DateTime(devSendDate);
			String devSendYear = td.toString("yyyy");
			String devSendMonth = td.toString("MM");
			String devSendDay = td.toString("dd");
			String devSendHour = td.toString("HH");
			// String ctime = td.toString("yyyyMMdd");// 默认发送时间为创建分区时间
			String ctime = td.toString("yyyyMM");// 默认发送时间为创建分区时间
			// String ctime = "201809";// 默认发送时间为创建分区时间
			iDevicePropertyLogDao.add(logId, deviceId, deviceCode, field, tag, name, unit, val, devSendYear,
					devSendMonth, devSendDay, devSendHour, devSendDate, serReceDate, curr, userBy, isDel, ctime,
					enterpriseId);
		} catch (FrameworkRuntimeException e) {
			logger.error(LogMsg.to(e));
			throw e;
		} catch (DataAccessException e) {
			logger.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		} catch (Throwable e) {
			logger.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.BASE_ERROR, e.getMessage(), e);
		}

	}

	@Override
	public List<DevicePropertyLogVo> list(Date startDate, Date endDate) throws FrameworkRuntimeException {
		try {
			List<DevicePropertyLogVo> list = iDevicePropertyLogDao.list(startDate, endDate);
			return list;
		} catch (FrameworkRuntimeException e) {
			logger.error(LogMsg.to(e));
			throw e;
		} catch (DataAccessException e) {
			logger.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		} catch (Throwable e) {
			logger.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.BASE_ERROR, e.getMessage(), e);
		}
	}

	@Override
	public void addProperty(Map<String, Object> propertyLogMap, String enterpriseId, String deviceId,
			JSONObject originalData) {
		try {
			// 封装特殊字段参数
			propertyLogMap.put("enterprise_id", enterpriseId);
			propertyLogMap.put("device_id", deviceId);
			propertyLogMap.put("original_data", originalData.toString());
			propertyLogMap.put("log_id", UuidUtils.getUuid());
			propertyLogMap.put("is_del", RootModel.NOT_DEL);
			DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
			DateTime td = DateTime.parse(propertyLogMap.get(FieldConstants.KBL_DEVICE_SEND_DATE).toString(), formatter);
			propertyLogMap.put("dev_send_year", td.toString("yyyy"));
			propertyLogMap.put("dev_send_month", td.toString("MM"));
			propertyLogMap.put("dev_send_day", td.toString("dd"));
			propertyLogMap.put("dev_send_hour", td.toString("HH"));
			propertyLogMap.put("ctime", td.toString("yyyyMM"));
			Date date = new Date();
			propertyLogMap.put("create_date", date);
			propertyLogMap.put("ser_rece_date", date);
			//System.out.println(propertyLogMap);
			iDevicePropertyLogDao.addProperty(propertyLogMap);
		} catch (DataAccessException e) {
			logger.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}
}
