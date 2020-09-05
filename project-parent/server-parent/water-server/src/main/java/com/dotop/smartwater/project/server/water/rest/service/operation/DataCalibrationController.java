package com.dotop.smartwater.project.server.water.rest.service.operation;

import com.dotop.smartwater.dependence.core.common.BaseController;
import com.dotop.smartwater.dependence.core.global.GlobalContext;
import com.dotop.smartwater.dependence.core.utils.VerificationUtils;
import com.dotop.smartwater.project.module.api.operation.IDataCalibrationFactory;
import com.dotop.smartwater.project.module.core.water.constants.ResultCode;
import com.dotop.smartwater.project.module.core.water.form.DataCalibrationForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 数据校准
 *

 * @date 2019/3/5.
 */
@RestController()

@RequestMapping("/dataCalibration")
public class DataCalibrationController implements BaseController<DataCalibrationForm> {

	private static final String  DATA_CALIBRATION_FORM = "DataCalibrationForm";

	@Autowired
	private IDataCalibrationFactory iDataCalibrationFactory;

	@Override
	@PostMapping(value = "/get", produces = GlobalContext.PRODUCES)
	public String get(@RequestBody DataCalibrationForm agrs) {
		VerificationUtils.obj(DATA_CALIBRATION_FORM, agrs);
		return resp(ResultCode.Success, ResultCode.SUCCESS, iDataCalibrationFactory.get(agrs));
	}

	@Override
	@PostMapping(value = "/page", produces = GlobalContext.PRODUCES)
	public String page(@RequestBody DataCalibrationForm agrs) {
		VerificationUtils.obj(DATA_CALIBRATION_FORM, agrs);
		return resp(ResultCode.Success, ResultCode.SUCCESS, iDataCalibrationFactory.page(agrs));
	}

	@Override
	@PostMapping(value = "/edit", produces = GlobalContext.PRODUCES)
	public String edit(@RequestBody DataCalibrationForm agrs) {
		VerificationUtils.obj(DATA_CALIBRATION_FORM, agrs);

		return resp(ResultCode.Success, ResultCode.SUCCESS, iDataCalibrationFactory.edit(agrs));
	}

}
