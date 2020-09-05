package com.dotop.smartwater.project.server.water.rest.service.revenue;

import com.dotop.smartwater.dependence.core.common.BaseController;
import com.dotop.smartwater.dependence.core.global.GlobalContext;
import com.dotop.smartwater.project.module.api.revenue.IEverydayWaterRecordFactory;
import com.dotop.smartwater.project.module.core.water.constants.ResultCode;
import com.dotop.smartwater.project.module.core.water.form.EverydayWaterRecordForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**

 * @date 2019/2/27.
 */
@RestController

@RequestMapping("/statistics")
public class StatisticsController implements BaseController<EverydayWaterRecordForm> {

	@Autowired
	private IEverydayWaterRecordFactory iEverydayWaterRecordFactory;

	/**
	 * 统计当前水司下每个月用水量
	 *
	 * @param request
	 * @return
	 */
	@PostMapping(value = "/getWaterByMonth", produces = GlobalContext.PRODUCES)
	public String getWaterByMonth(HttpServletRequest request) {
		return resp(ResultCode.Success, ResultCode.SUCCESS, iEverydayWaterRecordFactory.getWaterByMonth());
	}


	/**
	 * 显示当前水司下每个小区有多少个设备
	 *
	 * @param request
	 * @return
	 */
	@PostMapping(value = "/getDeviceCount", produces = GlobalContext.PRODUCES)
	public String getDeviceCount(HttpServletRequest request) {
		return resp(ResultCode.Success, ResultCode.SUCCESS, iEverydayWaterRecordFactory.getDeviceCount());
	}
	
	
	@PostMapping(value = "/getDeviceModels", produces = GlobalContext.PRODUCES)
	public String getDeviceModels(HttpServletRequest request) {
		return resp(ResultCode.Success, ResultCode.SUCCESS, iEverydayWaterRecordFactory.getDeviceModels());
	}
	
	/**
	 * 统计水司下每种通讯设备数量
	 * @param request
	 * @return
	 */
	@PostMapping(value = "/getDeviceModes", produces = GlobalContext.PRODUCES)
	public String getDeviceModes(HttpServletRequest request) {
		return resp(ResultCode.Success, ResultCode.SUCCESS, iEverydayWaterRecordFactory.getDeviceModes());
	}

	/**
	 * 获取水司下预警趋势
	 * @param request
	 * @return
	 */
	@PostMapping(value = "/getDeviceWarns", produces = GlobalContext.PRODUCES)
	public String getDeviceWarns(HttpServletRequest request) {
		return resp(ResultCode.Success, ResultCode.SUCCESS, iEverydayWaterRecordFactory.getDeviceWarns());
	}
	
	
	
	/**
	 * 统计水司下每种设备用途数量
	 * @param request
	 * @return
	 */
	@PostMapping(value = "/getDevicePurposes", produces = GlobalContext.PRODUCES)
	public String getDevicePurposes(HttpServletRequest request) {
		return resp(ResultCode.Success, ResultCode.SUCCESS, iEverydayWaterRecordFactory.getDevicePurposes());
	}
	
	
	/**
	 * 统计当前水司下每个小区欠费情况（当天和当月）
	 *
	 * @param request
	 * @return
	 */
	@PostMapping(value = "/getOwnpayByPage", produces = GlobalContext.PRODUCES)
	public String getOwnpayByPage(HttpServletRequest request) {
		return resp(ResultCode.Success, ResultCode.SUCCESS, iEverydayWaterRecordFactory.getCommunityOwnpay());
	}

}
