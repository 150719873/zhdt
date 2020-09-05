package com.dotop.smartwater.project.server.water.rest.service.device;

import com.dotop.smartwater.project.server.water.common.FoundationController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.dotop.smartwater.dependence.core.common.BaseController;
import com.dotop.smartwater.dependence.core.global.GlobalContext;
import com.dotop.smartwater.dependence.core.utils.VerificationUtils;
import com.dotop.smartwater.project.module.api.device.IMeterReadingFactory;
import com.dotop.smartwater.project.module.core.water.constants.ResultCode;
import com.dotop.smartwater.project.module.core.water.form.MeterReadingDetailForm;
import com.dotop.smartwater.project.module.core.water.form.MeterReadingTaskForm;

/**
 * 抄表任务
 *

 * @date 2019/2/22.
 */
@RestController()

@RequestMapping("/meterReading")
public class MeterReadingController extends FoundationController implements BaseController<MeterReadingTaskForm> {

	private static final String METER_READING_TASK_FORM = "MeterReadingTaskForm";

	@Autowired
	private IMeterReadingFactory iMeterReadingFactory;

	/**
	 * 获取抄表任务
	 *
	 * @param agrs
	 * @return
	 */
	@Override
	@PostMapping(value = "/get", produces = GlobalContext.PRODUCES)
	public String get(@RequestBody MeterReadingTaskForm agrs) {
		VerificationUtils.obj(METER_READING_TASK_FORM, agrs);
		return resp(ResultCode.Success, ResultCode.SUCCESS, iMeterReadingFactory.get(agrs));
	}

	/**
	 * 抄表任务分页
	 *
	 * @param agrs
	 * @return
	 */
	@Override
	@PostMapping(value = "/page", produces = GlobalContext.PRODUCES)
	public String page(@RequestBody MeterReadingTaskForm agrs) {
		VerificationUtils.obj(METER_READING_TASK_FORM, agrs);
		return resp(ResultCode.Success, ResultCode.SUCCESS, iMeterReadingFactory.page(agrs));
	}

	/**
	 * 编辑抄表任务
	 *
	 * @param agrs
	 * @return
	 */
	@Override
	@PostMapping(value = "/edit", produces = GlobalContext.PRODUCES)
	public String edit(@RequestBody MeterReadingTaskForm agrs) {
		VerificationUtils.obj(METER_READING_TASK_FORM, agrs);
		return resp(ResultCode.Success, ResultCode.SUCCESS, iMeterReadingFactory.edit(agrs));
	}

	/**
	 * 抄表任务详情分页
	 *
	 * @param agrs
	 * @return
	 */
	@PostMapping(value = "/detailPage", produces = GlobalContext.PRODUCES)
	public String detailPage(@RequestBody MeterReadingDetailForm agrs) {
		VerificationUtils.obj(METER_READING_TASK_FORM, agrs);
		return resp(ResultCode.Success, ResultCode.SUCCESS, iMeterReadingFactory.detailPage(agrs));
	}

	/**
	 * 设备详情
	 *
	 * @param agrs
	 * @return
	 */
	@PostMapping(value = "/deviceDetail", produces = GlobalContext.PRODUCES)
	public String deviceDetail(@RequestBody MeterReadingDetailForm form) {
		VerificationUtils.string("id", form.getId());
		return resp(ResultCode.Success, ResultCode.SUCCESS, iMeterReadingFactory.deviceDetail(form));
	}

	/**
	 * 获取抄表任务的区域列表
	 *
	 * @param agrs
	 * @return
	 */
	@PostMapping(value = "/getTaskArea", produces = GlobalContext.PRODUCES)
	public String getTaskArea(@RequestBody MeterReadingTaskForm agrs) {
		VerificationUtils.obj(METER_READING_TASK_FORM, agrs);
		return resp(ResultCode.Success, ResultCode.SUCCESS, iMeterReadingFactory.getTaskArea(agrs));
	}

	/**
	 * 获取抄表员列表
	 *
	 * @param agrs
	 * @return
	 */
	@PostMapping(value = "/getMeterReaders", produces = GlobalContext.PRODUCES)
	public String getMeterReaders(@RequestBody MeterReadingTaskForm agrs) {
		VerificationUtils.obj(METER_READING_TASK_FORM, agrs);
		return resp(ResultCode.Success, ResultCode.SUCCESS, iMeterReadingFactory.getMeterReaders(agrs));
	}
}
