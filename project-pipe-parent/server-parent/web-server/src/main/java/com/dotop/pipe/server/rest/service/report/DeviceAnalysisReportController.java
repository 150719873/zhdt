package com.dotop.pipe.server.rest.service.report;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.dotop.pipe.core.utils.VoFilter;
import com.dotop.pipe.core.vo.report.DeviceAnalysisVo;
import com.dotop.pipe.web.api.factory.report.IDeviceAnalysisFactory;
import com.dotop.smartwater.dependence.core.common.BaseController;
import com.dotop.smartwater.dependence.core.common.BaseForm;
import com.dotop.smartwater.dependence.core.global.GlobalContext;
import com.dotop.smartwater.dependence.core.log.LogMsg;

@RestController()
@RequestMapping("/report/device/analysis")
public class DeviceAnalysisReportController implements BaseController<BaseForm> {

	private final static Logger logger = LogManager.getLogger(DeviceAnalysisReportController.class);

	@Autowired
	private IDeviceAnalysisFactory iDeviceAnalysisFactory;

	// 设备状况
	@GetMapping(value = "/statuss", produces = GlobalContext.PRODUCES)
	public String statuss(BaseForm baseForm) {
		logger.info(LogMsg.to("msg:", " 分页查询开始", "baseForm", baseForm));
		DeviceAnalysisVo obj = iDeviceAnalysisFactory.statuss(baseForm);
		logger.info(LogMsg.to("msg:", " 分页查询查询结束"));
		return resp(obj);
	}

	// 设备属性
	@GetMapping(value = "/propertys", produces = GlobalContext.PRODUCES)
	public String propertys(BaseForm baseForm) {
		logger.info(LogMsg.to("msg:", " 分页查询开始", "baseForm", baseForm));
		DeviceAnalysisVo obj = iDeviceAnalysisFactory.propertys(baseForm);
		logger.info(LogMsg.to("msg:", " 分页查询查询结束"));
		// return resp(obj);
		// return JSON.toJSONString(obj, VoFilter.rf());
		return JSON.toJSONString(obj, VoFilter.rf(),
				new SerializerFeature[] { SerializerFeature.DisableCircularReferenceDetect });
	}
}
