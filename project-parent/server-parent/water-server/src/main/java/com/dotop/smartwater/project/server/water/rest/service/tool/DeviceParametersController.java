package com.dotop.smartwater.project.server.water.rest.service.tool;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.dotop.smartwater.dependence.core.common.BaseController;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.global.GlobalContext;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.dependence.core.utils.VerificationUtils;
import com.dotop.smartwater.project.module.api.tool.IDeviceParametersFactory;
import com.dotop.smartwater.project.module.core.water.constants.ResultCode;
import com.dotop.smartwater.project.module.core.water.form.DeviceParametersForm;
import com.dotop.smartwater.project.module.core.water.vo.DeviceParametersVo;

/**
 * 设备参数绑定Controller
 * 

 * @date 2019年2月21日
 */
@RestController()

@RequestMapping("/deviceParameters")
public class DeviceParametersController implements BaseController<DeviceParametersForm> {

	private static final Logger LOGGER = LogManager.getLogger(DeviceParametersController.class);

	private static final String DEVICEPARID = "deviceParId";

	@Autowired
	private IDeviceParametersFactory iDeviceParametersFactory;

	/**
	 * 新增绑定参数类型
	 */
	@Override
	@PostMapping(value = "/add", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
	public String add(@RequestBody DeviceParametersForm deviceParametersForm) {
		LOGGER.info(LogMsg.to("msg:", "新增功能开始", deviceParametersForm));
		// 校验
		VerificationUtils.string("deviceName", deviceParametersForm.getDeviceName());
		VerificationUtils.string("model", deviceParametersForm.getMode());
		VerificationUtils.string("valveStatus", deviceParametersForm.getValveStatus());
		VerificationUtils.string("valveType", deviceParametersForm.getValveType());
		VerificationUtils.string("unit", deviceParametersForm.getUnit());
		VerificationUtils.string("sensorType", deviceParametersForm.getSensorType());
		DeviceParametersVo deviceParametersVo = iDeviceParametersFactory.add(deviceParametersForm);
		LOGGER.info(LogMsg.to("msg:", "新增功能结束", deviceParametersForm));
		return resp(ResultCode.Success, ResultCode.Success, deviceParametersVo);
	}

	/**
	 * 分页查询 分页查询是允许添加查询条件 例如 设备类型 批次号 等两个条件 这两个条件不判空的
	 */
	@Override
	@PostMapping(value = "/page", produces = GlobalContext.PRODUCES)
	public String page(@RequestBody DeviceParametersForm deviceParametersForm) {
		LOGGER.info(LogMsg.to("msg:", " 分页查询开始", "deviceForm", deviceParametersForm));
		Integer page = deviceParametersForm.getPage();
		Integer pageCount = deviceParametersForm.getPageCount();
		// 验证
		VerificationUtils.integer("page", page);
		VerificationUtils.integer("pageCount", pageCount);
		Pagination<DeviceParametersVo> pagination = iDeviceParametersFactory.page(deviceParametersForm);
		LOGGER.info(LogMsg.to("msg:", " 分页查询查询结束"));
		return resp(ResultCode.Success, ResultCode.Success, pagination);
	}

	/**
	 * 列表查询
	 * 
	 * 对接app接口
	 */
	@Override
	@PostMapping(value = "/list", produces = GlobalContext.PRODUCES)
	public String list(DeviceParametersForm deviceParametersForm) {
		LOGGER.info(LogMsg.to("msg:", "查询列表开始", "deviceForm", deviceParametersForm));
		List<DeviceParametersVo> list = iDeviceParametersFactory.list(deviceParametersForm);
		LOGGER.info(LogMsg.to("msg:", "查询列表结束"));
		return resp(ResultCode.Success, ResultCode.Success, list);
	}

	/**
	 * 编辑
	 */
	@Override
	@PostMapping(value = "/edit", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
	public String edit(@RequestBody DeviceParametersForm deviceParametersForm) {
		LOGGER.info(LogMsg.to("msg:", "编辑内容开始", deviceParametersForm));

		String deviceParId = deviceParametersForm.getDeviceParId();
		// 校验
		VerificationUtils.string(DEVICEPARID, deviceParId);
		
		if (!iDeviceParametersFactory.checkDeviceName(deviceParametersForm)) {
			throw new FrameworkRuntimeException(ResultCode.Fail, "设备参数名称已存在");
		}
		DeviceParametersVo deviceParametersVo = iDeviceParametersFactory.edit(deviceParametersForm);
		LOGGER.info(LogMsg.to("msg:", "编辑内容结束"));
		return resp(ResultCode.Success, ResultCode.Success, deviceParametersVo);
	}

	/**
	 * 删除记录
	 */
	@Override
	@PostMapping(value = "/del", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
	public String del(@RequestBody DeviceParametersForm deviceParametersForm) {
		LOGGER.info(LogMsg.to("msg:", "删除内容开始", deviceParametersForm));
		// id
		String deviceParId = deviceParametersForm.getDeviceParId();
		// 校验
		VerificationUtils.string(DEVICEPARID, deviceParId);
		String id = iDeviceParametersFactory.del(deviceParametersForm);
		LOGGER.info(LogMsg.to("msg:", "删除内容结束", "id", id));
		return resp(ResultCode.Success, "SUCCESS", id);
	}
	
	
	/**
	 * 详情 暂时无用
	 */
	@PostMapping(value = "/getParams", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
	public String getParams(@RequestBody DeviceParametersForm deviceParametersForm) {
		LOGGER.info(LogMsg.to("msg:", "查询详情开始", deviceParametersForm));
		// 校验
		VerificationUtils.string(DEVICEPARID, deviceParametersForm.getDeviceParId());
		DeviceParametersVo deviceParametersVo = iDeviceParametersFactory.getParams(deviceParametersForm);
		LOGGER.info(LogMsg.to("msg:", "查询详情结束", deviceParametersForm));
		return resp(ResultCode.Success, "SUCCESS", deviceParametersVo);
	}
	

	/**
	 * 详情 暂时无用
	 */
	@Override
	@GetMapping(value = "/get/{deviceParId}", produces = GlobalContext.PRODUCES)
	public String get(DeviceParametersForm deviceParametersForm) {
		LOGGER.info(LogMsg.to("msg:", "查询详情开始", deviceParametersForm));
		// id
		String deviceParId = deviceParametersForm.getDeviceParId();
		// 校验
		VerificationUtils.string(DEVICEPARID, deviceParId);
		DeviceParametersVo deviceParametersVo = iDeviceParametersFactory.get(deviceParametersForm);
		LOGGER.info(LogMsg.to("msg:", "查询详情结束", deviceParametersForm));
		return resp(ResultCode.Success, "SUCCESS", deviceParametersVo);
	}

}
