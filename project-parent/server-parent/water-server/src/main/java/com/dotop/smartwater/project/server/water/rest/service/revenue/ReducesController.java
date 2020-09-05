package com.dotop.smartwater.project.server.water.rest.service.revenue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.dotop.smartwater.dependence.core.common.BaseController;
import com.dotop.smartwater.dependence.core.global.GlobalContext;
import com.dotop.smartwater.dependence.core.utils.VerificationUtils;
import com.dotop.smartwater.project.module.api.revenue.IReduceFactory;
import com.dotop.smartwater.project.module.core.water.constants.ResultCode;
import com.dotop.smartwater.project.module.core.water.form.ReduceForm;

/**
 * 用水减免
 * 

 * @date 2019年4月11日
 *
 */
@RestController

@RequestMapping("/Reduces")
public class ReducesController implements BaseController<ReduceForm> {

	@Autowired
	private IReduceFactory iReduceFactory;

	@PostMapping(value = "/getReduces", produces = GlobalContext.PRODUCES)
	public String getReduces() {
		ReduceForm reduceForm = new ReduceForm();
		return resp(ResultCode.Success, ResultCode.SUCCESS, iReduceFactory.getReduces(reduceForm));
	}

	@PostMapping(value = "/addReduces", produces = GlobalContext.PRODUCES)
	public String addReduces(@RequestBody ReduceForm reduceForm) {

		VerificationUtils.string("name", reduceForm.getName());
		VerificationUtils.doubles("rvalue", reduceForm.getRvalue());
		VerificationUtils.integer("unit", reduceForm.getUnit());

		iReduceFactory.addReduce(reduceForm);
		return resp(ResultCode.Success, ResultCode.SUCCESS, null);
	}

	@PostMapping(value = "/delReduces", produces = GlobalContext.PRODUCES)
	public String delReduces(@RequestBody ReduceForm reduceForm) {
		iReduceFactory.delReduce(reduceForm);
		return resp(ResultCode.Success, ResultCode.SUCCESS, null);
	}

	@PostMapping(value = "/editReduces", produces = GlobalContext.PRODUCES)
	public String editReduces(@RequestBody ReduceForm reduceForm) {
		VerificationUtils.string("reduceId", reduceForm.getReduceid());
		VerificationUtils.string("name", reduceForm.getName());
		VerificationUtils.doubles("rvalue", reduceForm.getRvalue());
		VerificationUtils.integer("unit", reduceForm.getUnit());
		iReduceFactory.editReduce(reduceForm);
		return resp(ResultCode.Success, ResultCode.SUCCESS, null);
	}

}
