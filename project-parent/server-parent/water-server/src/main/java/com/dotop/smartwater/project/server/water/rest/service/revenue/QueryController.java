package com.dotop.smartwater.project.server.water.rest.service.revenue;

import java.io.File;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.dotop.smartwater.dependence.core.common.BaseController;
import com.dotop.smartwater.dependence.core.global.GlobalContext;
import com.dotop.smartwater.dependence.core.utils.VerificationUtils;
import com.dotop.smartwater.project.module.api.device.IDeviceWarningFactory;
import com.dotop.smartwater.project.module.api.revenue.IOwnerCancelRecordFactory;
import com.dotop.smartwater.project.module.api.revenue.IQueryFactory;
import com.dotop.smartwater.project.module.core.water.constants.PathCode;
import com.dotop.smartwater.project.module.core.water.constants.ResultCode;
import com.dotop.smartwater.project.module.core.water.form.DeviceDownlinkForm;
import com.dotop.smartwater.project.module.core.water.form.DeviceUplinkForm;
import com.dotop.smartwater.project.module.core.water.form.DeviceWarningDetailForm;
import com.dotop.smartwater.project.module.core.water.form.DeviceWarningForm;
import com.dotop.smartwater.project.module.core.water.form.OwnerForm;
import com.dotop.smartwater.project.module.core.water.form.customize.QueryForm;
import com.dotop.smartwater.project.module.core.water.form.customize.StatisticsWaterForm;
import com.dotop.smartwater.project.module.core.water.vo.customize.OriginalVo;

/**
 * The type Query controller.
 *

 * @date 2019 /2/27.
 */
@RestController

@RequestMapping("/Query")
public class QueryController implements BaseController<DeviceDownlinkForm> {

	@Autowired
	private IDeviceWarningFactory iDeviceWarningFactory;

	@Autowired
	private IOwnerCancelRecordFactory iOwnerCancelRecordFactory;

	@Autowired
	private IQueryFactory iQueryFactory;

	/**
	 * 查询设备总数、用户总数、离线设备等信息
	 *
	 * @return data total
	 */
	@PostMapping(value = "/getDataTotal", produces = GlobalContext.PRODUCES)
	public String getDataTotal() {
		return resp(ResultCode.Success, ResultCode.SUCCESS, iQueryFactory.getDataTotal());
	}

	/**
	 * 原始数据查询
	 *
	 * @param queryForm the query form
	 * @return string
	 */
	@PostMapping(value = "/original", produces = GlobalContext.PRODUCES)
	public String original(@RequestBody QueryForm queryForm) {
		return resp(ResultCode.Success, ResultCode.SUCCESS, iQueryFactory.original(queryForm));
	}

	@PostMapping(value = "/getDownlinkHistory", produces = GlobalContext.PRODUCES)
	public String getDownlinkHistory(@RequestBody QueryForm queryForm) {
		return resp(ResultCode.Success, ResultCode.SUCCESS, iQueryFactory.getHistory(queryForm));
	}


	@PostMapping(value = "/getOriginalByIdAndDate", produces = GlobalContext.PRODUCES)
	public String getOriginalByIdAndDate(@RequestBody DeviceUplinkForm deviceUplinkForm) {
		VerificationUtils.string("id", deviceUplinkForm.getId());
		VerificationUtils.string("date", deviceUplinkForm.getDate());

		OriginalVo originalVo = iQueryFactory.getOriginalByIdAndDate(deviceUplinkForm);
		return resp(ResultCode.Success, ResultCode.SUCCESS, originalVo);
	}

	/**
	 * 过户记录查询
	 *
	 * @param queryForm the query form
	 * @return string
	 */
	@PostMapping(value = "/transfer", produces = GlobalContext.PRODUCES)
	public String transfer(@RequestBody QueryForm queryForm) {
		return null;
	}

	/**
	 * 销户记录查询
	 *

	 * @param queryForm the query form
	 * @return string
	 */
	@PostMapping(value = "/cancel", produces = GlobalContext.PRODUCES)
	public String cancel(@RequestBody QueryForm queryForm) {
		return resp(ResultCode.Success, ResultCode.SUCCESS, iOwnerCancelRecordFactory.findByPage(queryForm));
	}

	/**
	 * 水表异常查询
	 *
	 * @param ownerForm the owner form
	 * @return low battery
	 */
	@PostMapping(value = "/getLowBattery", produces = GlobalContext.PRODUCES)
	public String getLowBattery(@RequestBody OwnerForm ownerForm) {
		return resp(ResultCode.Success, ResultCode.SUCCESS, iDeviceWarningFactory.getLowBattery(ownerForm));
	}

	/**
	 * 添加告警
	 * @param warningForm
	 * @return
	 */
	@PostMapping(value = "/addWarning", produces = GlobalContext.PRODUCES)
	public String addDeviceWarning(@RequestBody DeviceWarningForm deviceWarningForm) {
		VerificationUtils.string("deveui", deviceWarningForm.getDeveui());
//		VerificationUtils.strList("告警类型", warningForm.getWarningType());
		if(StringUtils.isBlank(deviceWarningForm.getOpenException()) && StringUtils.isBlank(deviceWarningForm.getCloseException())
				&& StringUtils.isBlank(deviceWarningForm.getAbnormalCurrent()) && StringUtils.isBlank(deviceWarningForm.getAbnormalPower())
				&& StringUtils.isBlank(deviceWarningForm.getMagneticAttack()) && StringUtils.isBlank(deviceWarningForm.getAnhydrousAbnormal())
				&& StringUtils.isBlank(deviceWarningForm.getDisconnectionAbnormal()) && StringUtils.isBlank(deviceWarningForm.getPressureException())) {
			return resp(ResultCode.Fail, "告警类型不能为空", null);
		}
		return resp(ResultCode.Success, ResultCode.SUCCESS, iDeviceWarningFactory.addDeviceWarning(deviceWarningForm));
	}

	/**
	 * 获取告警分页
	 * @param deviceWarningForm
	 * @return
	 */
	@PostMapping(value = "/getWarningPage", produces = GlobalContext.PRODUCES)
	public String getDeviceWarningPage(@RequestBody DeviceWarningForm deviceWarningForm) {
		VerificationUtils.integer("page", deviceWarningForm.getPage());
		VerificationUtils.integer("pageCount", deviceWarningForm.getPageCount());
		return resp(ResultCode.Success, ResultCode.SUCCESS, iDeviceWarningFactory.getDeviceWarningPage(deviceWarningForm));
	}
	
	/**
	 * 标记为正常
	 * @param deviceWarningForm
	 * @return
	 */
	@PostMapping(value = "/markDeviceWarningNormal", produces = GlobalContext.PRODUCES)
	public String markDeviceWarningNormal(@RequestBody DeviceWarningForm deviceWarningForm) {
		if(deviceWarningForm.getNodeIds() == null || deviceWarningForm.getNodeIds().isEmpty()) {
			return resp(ResultCode.Fail, "主键ID不能为空", null);
		}
		return resp(ResultCode.Success, ResultCode.SUCCESS, iDeviceWarningFactory.warninghandle(deviceWarningForm));
	}
	
	/**
	 * 获取设备告警详情
	 * @param deviceWarningDetailForm
	 * @return
	 */
	@PostMapping(value = "/getDeviceWarningDetail", produces = GlobalContext.PRODUCES)
	public String getDeviceWarningDetail(@RequestBody DeviceWarningDetailForm deviceWarningDetailForm) {
		VerificationUtils.string("warningId", deviceWarningDetailForm.getWarningId());
		VerificationUtils.integer("page", deviceWarningDetailForm.getPage());
		VerificationUtils.integer("pageCount", deviceWarningDetailForm.getPageCount());
		return resp(ResultCode.Success, ResultCode.SUCCESS, iDeviceWarningFactory.getDeviceWarningDetailPage(deviceWarningDetailForm));
	}
	
	@PostMapping(value = "/getDeviceWarning", produces = GlobalContext.PRODUCES)
	public String getDeviceWarning(@RequestBody DeviceWarningForm deviceWarningForm) {
		VerificationUtils.string("id", deviceWarningForm.getId());
		return resp(ResultCode.Success, ResultCode.SUCCESS, iDeviceWarningFactory.getDeviceWarning(deviceWarningForm));
	}
	
	/**
	 * 删除设备告警（只能删除已处理告警）
	 * @param deviceWarningForm
	 * @return
	 */
	@PostMapping(value = "/deleteDeviceWarning", produces = GlobalContext.PRODUCES)
	public String deleteDeviceWarning(@RequestBody DeviceWarningForm deviceWarningForm) {
		VerificationUtils.string("id", deviceWarningForm.getId());
		Integer count = iDeviceWarningFactory.deleteDeviceWarning(deviceWarningForm);
		if(count > 0) {
			return resp(ResultCode.Success, ResultCode.SUCCESS, null);
		}else {
			return resp(ResultCode.Fail, "只能删除已处理告警新", null);
		}
	}

	/**
	 * 导出低电量水表记录
	 *
	 * @param ownerForm the owner form
	 * @return string
	 */
	@PostMapping(value = "/exportLowBattery", produces = GlobalContext.PRODUCES)
	public String exportLowBattery(@RequestBody OwnerForm ownerForm) {
		return resp(ResultCode.Success, iDeviceWarningFactory.exportLowBattery(ownerForm),
				PathCode.OriginalExcel + File.separator);
	}

	/**
	 * 查询当前时间到前一个月的用水量情况（由区域日用水量修改）
	 *
	 * @param request             the request
	 * @param statisticsWaterForm the statistics water form
	 * @return string
	 */
	@PostMapping(value = "/daily_getStatisticsWater", produces = GlobalContext.PRODUCES)
	public String dailyGetStatisticsWater(HttpServletRequest request,
	                                      @RequestBody StatisticsWaterForm statisticsWaterForm) {
		return resp(ResultCode.Success, ResultCode.SUCCESS, iQueryFactory.dailyGetStatisticsWater(statisticsWaterForm));
	}

	/**
	 * 用水量情况
	 *
	 * @param request             the request
	 * @param statisticsWaterForm the statistics water form
	 * @return string
	 */
	@PostMapping(value = "/month_getStatisticsWater", produces = GlobalContext.PRODUCES)
	public String monthGetStatisticsWater(HttpServletRequest request,
	                                      @RequestBody StatisticsWaterForm statisticsWaterForm) {
		return resp(ResultCode.Success, ResultCode.SUCCESS, iQueryFactory.monthGetStatisticsWater(statisticsWaterForm));
	}

	/**
	 * 查询业主所有信息
	 *

	 * @param ownerForm the owner form
	 * @return string
	 */
	@PostMapping(value = "/ownerinfo", produces = GlobalContext.PRODUCES)
	public String ownerinfo(@RequestBody OwnerForm ownerForm) {
		return resp(ResultCode.Success, ResultCode.SUCCESS, iQueryFactory.ownerinfo(ownerForm));
	}

}
