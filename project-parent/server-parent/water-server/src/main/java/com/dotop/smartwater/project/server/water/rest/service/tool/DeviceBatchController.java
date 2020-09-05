package com.dotop.smartwater.project.server.water.rest.service.tool;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.dotop.smartwater.dependence.core.common.BaseController;
import com.dotop.smartwater.dependence.core.global.GlobalContext;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.dependence.core.utils.VerificationUtils;
import com.dotop.smartwater.project.module.api.tool.IDeviceBatchFactory;
import com.dotop.smartwater.project.module.api.tool.IDeviceParametersFactory;
import com.dotop.smartwater.project.module.core.water.constants.ResultCode;
import com.dotop.smartwater.project.module.core.water.form.DeviceBatchForm;
import com.dotop.smartwater.project.module.core.water.form.DeviceBatchRelatioForm;
import com.dotop.smartwater.project.module.core.water.vo.DeviceBatchVo;
import com.dotop.smartwater.project.module.core.water.vo.DeviceVo;

/**
 * 设备批次管理

 *
 */
@RestController()

@RequestMapping("/deviceBatch")
public class DeviceBatchController implements BaseController<DeviceBatchForm> {

	private static final Logger LOGGER = LogManager.getLogger(DeviceBatchController.class);

	@Autowired
	private IDeviceBatchFactory factory;
	
	/**
	 * 新增批次信息
	 */
	@Override
	@PostMapping(value = "/add", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
	public String add(@RequestBody DeviceBatchForm form) {
		LOGGER.info(LogMsg.to("msg:", "新增批次开始", form));
		// 校验
		VerificationUtils.string("serialNumber", form.getSerialNumber());
		VerificationUtils.string("deviceParId", form.getDeviceParId());
		VerificationUtils.string("productId", form.getProductId());
		VerificationUtils.string("startTime", form.getStartTime());
		VerificationUtils.string("endTime", form.getEndTime());
		DeviceBatchVo vo = factory.add(form);
		LOGGER.info(LogMsg.to("msg:", "新增批次结束", form));
		return resp(ResultCode.Success, ResultCode.Success, vo);
	}
	
	/**
	 * 修改批次信息
	 */
	@Override
	@PostMapping(value = "/edit", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
	public String edit(@RequestBody DeviceBatchForm form) {
		LOGGER.info(LogMsg.to("msg:", "修改批次开始", form));
		// 校验
		VerificationUtils.string("id", form.getId());
		VerificationUtils.string("serialNumber", form.getSerialNumber());
		VerificationUtils.string("deviceParId", form.getDeviceParId());
		VerificationUtils.string("productId", form.getProductId());
		VerificationUtils.string("startTime", form.getStartTime());
		VerificationUtils.string("endTime", form.getEndTime());
		DeviceBatchVo vo = factory.edit(form);
		LOGGER.info(LogMsg.to("msg:", "修改批次结束", form));
		return resp(ResultCode.Success, ResultCode.Success, vo);
	}
	
	
	/**
	 * 删除批次信息
	 */
	@PostMapping(value = "/delete", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
	public String delete(@RequestBody DeviceBatchForm form) {
		LOGGER.info(LogMsg.to("msg:", "删除批次开始", form));
		// 校验
		VerificationUtils.string("id", form.getId());
		factory.delete(form);
		LOGGER.info(LogMsg.to("msg:", "删除批次结束", form));
		return resp(ResultCode.Success, ResultCode.Success, true);
	}
	
	/**
	 * 删除设备信息
	 */
	@PostMapping(value = "/deleteDevice", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
	public String deleteDevice(@RequestBody DeviceBatchRelatioForm form) {
		LOGGER.info(LogMsg.to("msg:", "删除批次下设备信息开始", form));
		// 校验
		VerificationUtils.string("deveui", form.getDeveui());
		VerificationUtils.string("devid", form.getDevid());
		VerificationUtils.string("batchId", form.getBatchId());
		factory.deleteDevice(form);
		LOGGER.info(LogMsg.to("msg:", "删除批次下设备信息结束", form));
		return resp(ResultCode.Success, ResultCode.Success, true);
	}
	
	
	/**
	 * 结束批次信息
	 */
	@PostMapping(value = "/end", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
	public String end(@RequestBody DeviceBatchForm form) {
		LOGGER.info(LogMsg.to("msg:", "结束批次开始", form));
		// 校验
		VerificationUtils.string("id", form.getId());
		factory.end(form);
		LOGGER.info(LogMsg.to("msg:", "结束批次完成", form));
		return resp(ResultCode.Success, ResultCode.Success, true);
	}
	
	/**
	 * 设备批次分页查询
	 */
	@Override
	@PostMapping(value = "/page", produces = GlobalContext.PRODUCES)
	public String page(@RequestBody DeviceBatchForm form) {
		LOGGER.info(LogMsg.to("msg:", " 分页查询开始", "deviceBatch", form));
		Integer page = form.getPage();
		Integer pageCount = form.getPageCount();
		// 验证
		VerificationUtils.integer("page", page);
		VerificationUtils.integer("pageCount", pageCount);
		Pagination<DeviceBatchVo> pagination = factory.page(form);
		LOGGER.info(LogMsg.to("msg:", " 分页查询结束"));
		return resp(ResultCode.Success, ResultCode.Success, pagination);
	}
	
	/**
	 * 设备批次-水表信息
	 */
	@PostMapping(value = "/detailPage", produces = GlobalContext.PRODUCES)
	public String detailPage(@RequestBody DeviceBatchRelatioForm form) {
		LOGGER.info(LogMsg.to("msg:", " 分页查询开始", "deviceBatch", form));
		// 验证
		VerificationUtils.string("batchId", form.getBatchId());
		VerificationUtils.integer("page", form.getPage());
		VerificationUtils.integer("pageCount", form.getPageCount());
		Pagination<DeviceVo> pagination = factory.detailPage(form);
		LOGGER.info(LogMsg.to("msg:", " 分页查询结束"));
		return resp(ResultCode.Success, ResultCode.Success, pagination);
	}
	
	/**
	 * 新增批次信息
	 */
	@Override
	@PostMapping(value = "/get", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
	public String get(@RequestBody DeviceBatchForm form) {
		LOGGER.info(LogMsg.to("msg:", "获取设备批次开始", form));
		// 校验
		VerificationUtils.string("ID", form.getId());
		DeviceBatchVo vo = factory.get(form);
		LOGGER.info(LogMsg.to("msg:", "获取设备批次结束", form));
		return resp(ResultCode.Success, ResultCode.Success, vo);
	}
}
