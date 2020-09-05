package com.dotop.pipe.server.rest.service.alarm;

import java.util.List;

import javax.annotation.Resource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dotop.pipe.core.form.AlarmWMSettingForm;
import com.dotop.pipe.core.vo.alarm.AlarmWMSettingVo;
import com.dotop.pipe.core.vo.device.DeviceVo;
import com.dotop.pipe.web.api.factory.alarm.IAlarmWMSettingFactory;
import com.dotop.smartwater.dependence.core.common.BaseController;
import com.dotop.smartwater.dependence.core.global.GlobalContext;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.dependence.core.utils.VerificationUtils;

@RestController()
@RequestMapping("/alarmSetting/WM")
public class AlarmWMSettingController implements BaseController<AlarmWMSettingForm> {

	private static final Logger logger = LogManager.getLogger(AlarmWMSettingController.class);

	@Resource
	private IAlarmWMSettingFactory iAlarmWMSettingFactory;

	@PostMapping(value = "/add", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
	public String add(@RequestBody AlarmWMSettingForm alarmWMSettingForm) {
		logger.info(LogMsg.to("msg:", "水质预警设置开始", "alarmWMSettingForm", alarmWMSettingForm));
		// VerificationUtils.string("id", alarmWMSettingForm.getId());
		VerificationUtils.string("deviceId", alarmWMSettingForm.getDeviceId());
		iAlarmWMSettingFactory.add(alarmWMSettingForm);
		logger.info(LogMsg.to("msg:", "水质预警设置结束", "更新数据"));
		return resp();
	}

	@PostMapping(value = "/page", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
	public String page(@RequestBody AlarmWMSettingForm alarmWMSettingForm) {
		logger.info(LogMsg.to("msg:", "水质预警设置分页查询开始", "alarmWMSettingForm", alarmWMSettingForm));
		Integer page = alarmWMSettingForm.getPage();
		Integer pageSize = alarmWMSettingForm.getPageSize();
		// 验证
		VerificationUtils.integer("page", page);
		VerificationUtils.integer("pageSize", pageSize);
		Pagination<DeviceVo> pagination = iAlarmWMSettingFactory.getPage(alarmWMSettingForm);
		logger.info(LogMsg.to("msg:", "水质预警设置分页查询结束"));
		return resp(pagination);
	}

	/**
	 * 水质预警详情
	 */
	@GetMapping(value = "/{deviceId}", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
	public String get(AlarmWMSettingForm alarmWMSettingForm) {
		logger.info(LogMsg.to("msg:", "报警详情查询开始"));
		logger.info(LogMsg.to("deviceId", alarmWMSettingForm.getDeviceId()));
		// 验证
		VerificationUtils.string("deviceId", alarmWMSettingForm.getDeviceId());
		List<AlarmWMSettingVo> list = iAlarmWMSettingFactory.getByDeviceId(alarmWMSettingForm);
		logger.info(LogMsg.to("msg:", "报警详情查询结束"));
		return resp(list);
	}

	/**
	 * 水质预警个性化删除
	 */
	@Override
	@PutMapping(value = "/upd", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
	public String edit(@RequestBody AlarmWMSettingForm alarmWMSettingForm) {
		logger.info(LogMsg.to("msg:", "水质预警删除", "alarmWMSettingForm", alarmWMSettingForm));
		// 验证
		String id = alarmWMSettingForm.getDeviceId();
		// 处理状态不能为空
		VerificationUtils.string("id", id);
		iAlarmWMSettingFactory.edit(alarmWMSettingForm);
		logger.info(LogMsg.to("msg:", "报警处理结束", "更新数据"));
		return resp();
	}

	/**
	 * 水质预警定时功能
	 */
	@GetMapping(value = "/timing", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
	public String getAlarm(AlarmWMSettingForm alarmWMSettingForm) {
		logger.info(LogMsg.to("msg:", "报警详情查询开始"));
		List<DeviceVo> list = iAlarmWMSettingFactory.getAlarm(alarmWMSettingForm);
		logger.info(LogMsg.to("msg:", "报警详情查询结束"));
		return resp(list);
	}

}
