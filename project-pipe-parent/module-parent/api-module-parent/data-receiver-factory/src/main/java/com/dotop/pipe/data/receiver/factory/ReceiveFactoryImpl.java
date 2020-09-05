package com.dotop.pipe.data.receiver.factory;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

import com.dotop.pipe.data.receiver.core.model.DeviceRD;
import com.dotop.pipe.data.receiver.core.utils.DeviceUtils;
import com.dotop.pipe.data.receiver.core.vo.AlarmDesVo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.dotop.pipe.api.service.alarm.IAlarmService;
import com.dotop.pipe.api.service.device.IDeviceService;
import com.dotop.pipe.api.service.model.IModelMapService;
import com.dotop.pipe.core.bo.alarm.AlarmBo;
import com.dotop.pipe.core.bo.device.DeviceBo;
import com.dotop.pipe.core.vo.device.DeviceVo;
import com.dotop.pipe.core.vo.model.ModelMapVo;
import com.dotop.pipe.data.receiver.api.factory.IDevicePropertyFactory;
import com.dotop.pipe.data.receiver.api.factory.IReceiveFactory;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.global.GlobalContext;
import com.dotop.smartwater.dependence.core.log.LogMsg;

@Component
public class ReceiveFactoryImpl implements IReceiveFactory {

	private final static Logger logger = LogManager.getLogger(ReceiveFactoryImpl.class);

	@Autowired
	private IDeviceService iDeviceService;

	@Autowired
	private IModelMapService iModelMapService;

	@Autowired
	private IDevicePropertyFactory iDevicePropertyFactory;

	@Autowired
	private IAlarmService iAlarmService;

	@Override
	public void handle(String deviceCode, JSONObject rddata) throws FrameworkRuntimeException {
		// 接收时间
		Date curr = new Date();
		// 模型解析
		// JSONObject rddata = com.alibaba.fastjson.JSON.parseObject(rdata);

		// 验证deviceCode计量计是否存在
		// 先查询缓存 是否存在 存在
		boolean cacheFlag = iDevicePropertyFactory.getCacheByDeviceCode(deviceCode);
		boolean flag = true;

		DeviceBo deviceBo = new DeviceBo();
		deviceBo.setCode(deviceCode);
		// 不存在查询数据库
		if (!cacheFlag) {
			// new DeviceBo();
			flag = iDeviceService.isExist(deviceBo);
		}

		if (flag) {
			// 添加到缓存
			iDevicePropertyFactory.setCacheByDeviceCode(deviceCode);

			// 如果存在查询计量计信息
			DeviceVo device = iDeviceService.get(deviceBo);
			if (device == null) {
				return;
			}

			String deviceId = device.getDeviceId();
			String enterpriseId = device.getEnterpriseId();

			// 获取计量计对应的模型解析xml
			ModelMapVo modelMap = iModelMapService.getByDeviceId(enterpriseId, deviceId);
			if (modelMap == null) {
				logger.error(LogMsg.to("msg", "modelMap为空"));
				return;
			}
			String content = modelMap.getContent();

			DeviceRD deviceRD = DeviceUtils.parse(curr, content, rddata, device);
			if (deviceRD == null) {
				logger.error(LogMsg.to("msg", "deviceRD为空"));
				return;
			}
			// 更新计量计属性表和新增计量计属性日志表
			// 现在采取同步处理(根据性能要求，可改为异步)
			List<AlarmDesVo> alarmDesVoList = iDevicePropertyFactory.merge(device, deviceRD);

			// 流量计和压力计 有一个异常值 水质计可能有多个异常值
			for (AlarmDesVo alarmDesVo : alarmDesVoList) {
				// 发出报警日志
				if (alarmDesVo.isStatus()) {
					Date createDate = new Date();
					Random rand = new Random();
					SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");// 设置日期格式
					String code = df.format(createDate) + String.valueOf(rand.nextInt(900) + 100);

					AlarmBo alarmbo = new AlarmBo();
					alarmbo.setName(alarmDesVo.getAlarmName());
					alarmbo.setDes(alarmDesVo.getAlarmDes());
					alarmbo.setDeviceId(deviceId);
					alarmbo.setStatus(0);
					alarmbo.setEnterpriseId(enterpriseId);
					alarmbo.setUserBy(GlobalContext.SYSTEM_NAME);
					alarmbo.setCode(code);
					alarmbo.setCurr(curr);
					iAlarmService.add(alarmbo);
				}
			}
		}
	}
}
