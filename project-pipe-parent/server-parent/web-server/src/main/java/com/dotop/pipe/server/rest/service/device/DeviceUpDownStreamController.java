package com.dotop.pipe.server.rest.service.device;

import javax.annotation.Resource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dotop.pipe.core.form.DeviceUpDownStreamForm;
import com.dotop.pipe.core.vo.device.DeviceUpDownStreamVo;
import com.dotop.pipe.web.api.factory.device.IDeviceUpDownStreamFactory;
import com.dotop.smartwater.dependence.core.common.BaseController;
import com.dotop.smartwater.dependence.core.global.GlobalContext;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.dependence.core.utils.VerificationUtils;

@RestController()
@RequestMapping("/upDownStream")
public class DeviceUpDownStreamController implements BaseController<DeviceUpDownStreamForm> {

	private static final Logger logger = LogManager.getLogger(DeviceUpDownStreamController.class);

	@Resource
	private IDeviceUpDownStreamFactory iDeviceUpDownStreamFactory;

	@PostMapping(value = "/add", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
	public String add(@RequestBody DeviceUpDownStreamForm deviceUpDownStreamForm) {
		logger.info(LogMsg.to("msg:", "设备上下游新增开始", "deviceUpDownStreamForm", deviceUpDownStreamForm));
		VerificationUtils.strList("parentId", deviceUpDownStreamForm.getParentDeviceIds());
		VerificationUtils.string("deviceId", deviceUpDownStreamForm.getDeviceId());
		iDeviceUpDownStreamFactory.add(deviceUpDownStreamForm);
		logger.info(LogMsg.to("msg:", "设备上下游新增结束", "更新数据"));
		return resp();
	}

	@PostMapping(value = "/editAlarmProperty", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
	public String editAlarmProperty(@RequestBody DeviceUpDownStreamForm deviceUpDownStreamForm) {
		logger.info(LogMsg.to("msg:", "设备上下游预测值范围设置开始", "deviceUpDownStreamForm", deviceUpDownStreamForm));
		// VerificationUtils.string("parentId", deviceUpDownStreamForm.getAlarmProperties());
		VerificationUtils.string("deviceId", deviceUpDownStreamForm.getId());
		iDeviceUpDownStreamFactory.editAlarmProperty(deviceUpDownStreamForm);
		logger.info(LogMsg.to("msg:", "设备上下游预测值范围设置结束", "更新数据"));
		return resp();
	}

	/**
	 * 设备上下游详情
	 */
	@GetMapping(value = "/{deviceId}", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
	public String get(DeviceUpDownStreamForm deviceUpDownStreamForm) {
		logger.info(LogMsg.to("msg:", "设备上下游详情查询开始"));
		logger.info(LogMsg.to("deviceId", deviceUpDownStreamForm.getDeviceId()));
		// 验证
		VerificationUtils.string("deviceId", deviceUpDownStreamForm.getDeviceId());
		DeviceUpDownStreamVo vo = iDeviceUpDownStreamFactory.get(deviceUpDownStreamForm);
		logger.info(LogMsg.to("msg:", "设备上下游详情查询结束"));
		return resp(vo);
	}

	/**
	 * 设备上下游删除关联关系
	 */
	@Override
	@PutMapping(value = "/del", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
	public String edit(@RequestBody DeviceUpDownStreamForm deviceUpDownStreamForm) {
		logger.info(LogMsg.to("msg:", "设备上下游预警删除开始", "deviceUpDownStreamForm", deviceUpDownStreamForm));
		// 验证
		String id = deviceUpDownStreamForm.getId();
		// 处理状态不能为空
		VerificationUtils.string("id", id);
		iDeviceUpDownStreamFactory.del(deviceUpDownStreamForm);
		logger.info(LogMsg.to("msg:", "设备上下游预警删除结束", "更新数据"));
		return resp();
	}

	/**
	 * 设备上下游关联关系统计
	 */
	@Override
	@PostMapping(value = "/page", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
	public String page(@RequestBody DeviceUpDownStreamForm deviceUpDownStreamForm) {
		logger.info(LogMsg.to("msg:", "设备上下游报表分页查询开始", "deviceUpDownStreamForm", deviceUpDownStreamForm));
		Integer page = deviceUpDownStreamForm.getPage();
		Integer pageSize = deviceUpDownStreamForm.getPageSize();
		VerificationUtils.integer("page", page);
		VerificationUtils.integer("pageSize", pageSize);
		Pagination<DeviceUpDownStreamVo> pagination = iDeviceUpDownStreamFactory.page(deviceUpDownStreamForm);
		logger.info(LogMsg.to("msg:", "设备上下游报表分页查询结束", "更新数据"));
		return resp(pagination);
	}
}
