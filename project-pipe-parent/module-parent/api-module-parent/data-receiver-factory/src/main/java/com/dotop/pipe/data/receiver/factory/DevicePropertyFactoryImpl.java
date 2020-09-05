package com.dotop.pipe.data.receiver.factory;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.dotop.pipe.data.receiver.core.model.DeviceRD;
import com.dotop.pipe.data.receiver.core.model.InfoRD;
import com.dotop.pipe.data.receiver.core.model.PropertyRD;
import com.dotop.pipe.data.receiver.core.vo.AlarmDesVo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dotop.pipe.core.vo.device.DevicePropertyVo;
import com.dotop.pipe.core.vo.device.DeviceVo;
import com.dotop.pipe.data.receiver.api.factory.IDevicePropertyFactory;
import com.dotop.pipe.data.receiver.api.service.IDevicePropertyLogService;
import com.dotop.pipe.data.receiver.api.service.IDevicePropertyService;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.global.GlobalContext;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.utils.UuidUtils;

@Component
public class DevicePropertyFactoryImpl implements IDevicePropertyFactory {

	private final static Logger logger = LogManager.getLogger(DevicePropertyFactoryImpl.class);

	@Autowired
	private IDevicePropertyService iDevicePropertyService;

	@Autowired
	private IDevicePropertyLogService iDevicePropertyLogService;

	@Override
	// 不需要长事务
	// @Transactional(propagation = Propagation.REQUIRED, rollbackFor =
	// FrameworkRuntimeException.class)
	public List<AlarmDesVo> merge(DeviceVo device, DeviceRD deviceRD) throws FrameworkRuntimeException {
		Date curr = new Date();
		String deviceId = device.getDeviceId();
		InfoRD info = deviceRD.getInfo();

		// 报警描述
		List<AlarmDesVo> alarmDesVoList = new ArrayList<AlarmDesVo>();

		List<PropertyRD> properties = deviceRD.getProperties();
		for (PropertyRD property : properties) {

			AlarmDesVo alarmDesVo = new AlarmDesVo();
			StringBuffer alarmName = new StringBuffer();
			StringBuffer alarmDes = new StringBuffer();

			// 异常扑抓，不影响其他属性变化
			String field = property.getField();
			// 查询缓存
			DevicePropertyVo deviceProperty = iDevicePropertyService.getCache(deviceId, field);

			String deviceCode = device.getCode();
			String tag = property.getTag();
			String name = property.getName();
			// 数据类型 不需要更新数据库
			String type = property.getType();
			String val = property.getVal();
			// 单位
			String unit = property.getUnit();

			Date devSendDate = info.getDevSendDate();
			Date serReceDate = info.getSerReceDate();

			// 没有缓存 数据中有 或者没有数据
			if (deviceProperty == null) {
				// field 分布式锁
				// 查询数据库 主要用于查看计量计属性表中是否存在这个属性 并更新为最新的
				deviceProperty = iDevicePropertyService.getDb(deviceId, field);
				if (deviceProperty == null) {
					// 数据库不存在
					String devProId = UuidUtils.getUuid();
					iDevicePropertyService.add(deviceId, deviceCode, field, tag, name, unit, val, devSendDate,
							serReceDate, curr, GlobalContext.SYSTEM_NAME, device.getEnterpriseId(), devProId);
					// deviceProperty 为空时 new一个对象
					deviceProperty = new DevicePropertyVo();
					deviceProperty.setDevProId(devProId);
					logger.info(LogMsg.to("计量计属性数据库不存在,添加计量计属性", deviceProperty));
				} else {
					// 数据库存在
					logger.info(LogMsg.to("计量计属性数据库存在,更新计量计属性", deviceProperty));
					String devProId = deviceProperty.getDevProId();
					iDevicePropertyService.upd(devProId, tag, name, unit, val, devSendDate, serReceDate, curr,
							GlobalContext.SYSTEM_NAME, device.getEnterpriseId());
				}
			} else { // 有缓存 代表 数据库中一定有数据
				// 数据库存在
				String devProId = deviceProperty.getDevProId();
				iDevicePropertyService.upd(devProId, tag, name, unit, val, devSendDate, serReceDate, curr,
						GlobalContext.SYSTEM_NAME, device.getEnterpriseId());
			}
			// 添加到property_log 表中
			logger.info(LogMsg.to("计量计属性log记录开始", deviceCode));
			iDevicePropertyLogService.add(deviceId, deviceCode, field, tag, name, unit, val, devSendDate, serReceDate,
					curr, GlobalContext.SYSTEM_NAME, device.getEnterpriseId());
			logger.info(LogMsg.to("计量计属性log记录结束", deviceCode));
			// 添加缓存
			deviceProperty.setTag(tag);
			deviceProperty.setName(name);
			deviceProperty.setType(type);
			deviceProperty.setVal(val);
			deviceProperty.setField(field);
			deviceProperty.setDevSendDate(devSendDate);
			deviceProperty.setSerReceDate(serReceDate);
			iDevicePropertyService.addCache(deviceProperty);

			String tagStr = property.getTag();
			// 报警异常 流量计 压力计 水质计
			if ("flw_value".equals(tagStr) // 流量计
					|| "pressure_value".equals(tagStr) // 压力计
					|| "quality_ph".equals(tagStr) // ph值
					|| "quality_oxygen".equals(tagStr) // 含氧值
					|| "quality_turbid".equals(tagStr) // 浑浊度
					|| "quality_chlorine".equals(tagStr) // 含氯值
			) {
				BigDecimal valbig = new BigDecimal(val);
				BigDecimal upVal = new BigDecimal(property.getUpVal());
				BigDecimal downVal = new BigDecimal(property.getDownVal());
				// 小于0 表示valbig < downVal(下限) 大于0 表示 valbig > upval(上限值)
				if (valbig.compareTo(downVal) < 0 || valbig.compareTo(upVal) > 0) {
					alarmName.append(property.getName()).append("异常;");
					alarmDes.append(property.getName()).append("异常,异常值:").append(val).append(";");
					alarmDesVo.setStatus(true);
					alarmDesVo.setAlarmName(alarmName.toString());
					alarmDesVo.setAlarmDes(alarmDes.toString());
					logger.info(LogMsg.to("计量计数据异常，添加报警。异常值", val, "设备编号:", deviceCode));
				}
				alarmDesVoList.add(alarmDesVo);
			}

		}

		return alarmDesVoList;
		// return alarmDesVo;
	}

	@Override
	public boolean getCacheByDeviceCode(String deviceCode) throws FrameworkRuntimeException {
		return iDevicePropertyService.getCacheByDeviceCode(deviceCode);
	}

	@Override
	public void setCacheByDeviceCode(String deviceCode) throws FrameworkRuntimeException {
		iDevicePropertyService.addCacheByDeviceCode(deviceCode);
	}

}
