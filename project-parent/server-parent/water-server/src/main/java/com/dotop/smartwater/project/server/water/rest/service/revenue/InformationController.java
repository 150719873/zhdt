/** @author : KangJunRong
 *  @description : 
 *  @date : 2017年12月21日 上午10:08:48
 */
package com.dotop.smartwater.project.server.water.rest.service.revenue;

import java.util.List;

import com.dotop.smartwater.project.module.core.water.enums.OperateTypeEnum;
import com.dotop.smartwater.project.server.water.common.FoundationController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.dotop.smartwater.dependence.core.common.BaseController;
import com.dotop.smartwater.dependence.core.global.GlobalContext;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.dependence.core.utils.VerificationUtils;
import com.dotop.smartwater.project.module.api.revenue.IInformationFactory;
import com.dotop.smartwater.project.module.core.water.constants.ResultCode;
import com.dotop.smartwater.project.module.core.water.form.DeviceForm;
import com.dotop.smartwater.project.module.core.water.vo.DeviceVo;

@RestController
@RequestMapping("/Information")

public class InformationController extends FoundationController implements BaseController<DeviceForm> {
	private static final Logger LOGGER = LoggerFactory.getLogger(InformationController.class);

	@Autowired
	private IInformationFactory iInformationFactory;

	private static final String DEVEUI = "deveui";

	/**
	 * 对应原方法 editDevice
	 * 
	 * @param deviceVo
	 * @return
	 */
	@Override
	@PostMapping(value = "/editDevice", consumes = GlobalContext.PRODUCES)
	public String edit(@RequestBody DeviceForm deviceForm) {
		LOGGER.info(LogMsg.to("msg:", "修改设备信息开始", "deviceForm", deviceForm));
		String deveui = deviceForm.getDeveui();
		String devno = deviceForm.getDevno();
		VerificationUtils.string(DEVEUI, deveui);
		VerificationUtils.string("devno", devno);
		DeviceVo deviceVo = iInformationFactory.edit(deviceForm);
		LOGGER.info(LogMsg.to("msg:", "修改设备信息结束"));
		auditLog(OperateTypeEnum.METER_MANAGEMENT,"编辑","设备EUI",deveui,"设备编号",devno);
		return resp(ResultCode.Success, ResultCode.SUCCESS, deviceVo);
	}

	/**
	 * 对应原方法名称为 deleteDevice 删除设备信息
	 */
	@Override
	@PostMapping(value = "/deleteDevice", produces = GlobalContext.PRODUCES)
	public String del(@RequestBody DeviceForm deviceForm) {
		LOGGER.info(LogMsg.to("msg:", "删除设备信息开始", "deviceForm", deviceForm));
		String deveui = deviceForm.getDeveui();
		VerificationUtils.string(DEVEUI, deveui);
		iInformationFactory.del(deviceForm);
		LOGGER.info(LogMsg.to("msg:", "删除设备信息结束", "deviceForm", deviceForm));
		auditLog(OperateTypeEnum.METER_MANAGEMENT,"删除","设备EUI",deveui);
		return resp(ResultCode.Success, ResultCode.SUCCESS, null);
	}

	/**
	 * 查询上行数据
	 */
	@PostMapping(value = "/getUplinkData", produces = GlobalContext.PRODUCES)
	public String getUplinkData(@RequestBody DeviceForm deviceForm) {
		LOGGER.info(LogMsg.to("msg:", "查询上行数据开始", "deviceForm", deviceForm));
		String deveui = deviceForm.getDeveui();
		VerificationUtils.string(DEVEUI, deveui);
		List<DeviceVo> list = iInformationFactory.getUplinkData(deviceForm);
		LOGGER.info(LogMsg.to("msg:", "查询上行数据结束", "list", list));
		return resp(ResultCode.Success, ResultCode.SUCCESS, list);
	}

	/**
	 * 设备管理-- 水表管理-- 分页查询
	 */
	@PostMapping(value = "/queryDevice", produces = GlobalContext.PRODUCES)
	public String queryDevice(@RequestBody DeviceForm deviceForm) {
		LOGGER.info(LogMsg.to("msg:", "查询设备开始", "deviceForm", deviceForm));
		Integer page = deviceForm.getPage();
		Integer pageCount = deviceForm.getPageCount();
		VerificationUtils.integer("page", page);
		VerificationUtils.integer("pageCount", pageCount);

		Pagination<DeviceVo> pagination = iInformationFactory.queryDevice(deviceForm);
		LOGGER.info(LogMsg.to("msg:", "查询设备结束", "pagination", pagination));
		return resp(ResultCode.Success, ResultCode.SUCCESS, pagination);
	}
	
	
	/**
	 * 设备管理-- 监控数据
	 */
	@PostMapping(value = "/getMonitor", produces = GlobalContext.PRODUCES)
	public String getMonitor(@RequestBody DeviceForm deviceForm) {
		LOGGER.info(LogMsg.to("msg:", "查询设备开始", "deviceForm", deviceForm));
		Integer page = deviceForm.getPage();
		Integer pageCount = deviceForm.getPageCount();
		VerificationUtils.integer("page", page);
		VerificationUtils.integer("pageCount", pageCount);

		Pagination<DeviceVo> pagination = iInformationFactory.getMonitor(deviceForm);
		LOGGER.info(LogMsg.to("msg:", "查询设备结束", "pagination", pagination));
		return resp(ResultCode.Success, ResultCode.SUCCESS, pagination);
	}
	
	
	/**
	 * 设备管理-水表监控-获取时间间隔中的每一天
	 */
	@PostMapping(value = "/getDate", produces = GlobalContext.PRODUCES)
	public String getDate(@RequestBody DeviceForm deviceForm) {
		LOGGER.info(LogMsg.to("msg:", "获取时间间隔中的每一天开始", "deviceForm", deviceForm));

		List<String> dates = iInformationFactory.getDate(deviceForm);
		LOGGER.info(LogMsg.to("msg:", "获取时间间隔中的每一天结束", "dates", dates));
		return resp(ResultCode.Success, ResultCode.SUCCESS, dates);
	}
	
	
	

}