package com.dotop.smartwater.project.server.water.rest.service.tool;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.dotop.smartwater.dependence.core.common.BaseController;
import com.dotop.smartwater.dependence.core.global.GlobalContext;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.dependence.core.utils.VerificationUtils;
import com.dotop.smartwater.project.module.api.revenue.INumRuleSetFactory;
import com.dotop.smartwater.project.module.core.water.constants.ResultCode;
import com.dotop.smartwater.project.module.core.water.constants.WaterConstants;
import com.dotop.smartwater.project.module.core.water.form.NumRuleSetForm;
import com.dotop.smartwater.project.module.core.water.form.customize.MakeNumRequest;
import com.dotop.smartwater.project.module.core.water.vo.NumRuleSetVo;

/**
 * @program: project-parent
 * @description: 流水号设计

 * @create: 2019-03-13 09:27
 **/
@RestController

@RequestMapping("/numRuleSet")
public class NumRuleSetController implements BaseController<NumRuleSetForm> {

	@Autowired
	private INumRuleSetFactory iNumRuleSetFactory;

	private static final String RULEID = "ruleId";

	private static final String STATUS = "status";

	@Override
	@PostMapping(value = "/list", produces = GlobalContext.PRODUCES)
	public String list(@RequestBody NumRuleSetForm numRuleSetForm) {
		return resp(ResultCode.Success, ResultCode.SUCCESS, iNumRuleSetFactory.list(numRuleSetForm));
	}

	@Override
	@PostMapping(value = "/page", produces = GlobalContext.PRODUCES)
	public String page(@RequestBody NumRuleSetForm numRuleSetForm) {
		Pagination<NumRuleSetVo> pagination = iNumRuleSetFactory.page(numRuleSetForm);
		return resp(ResultCode.Success, ResultCode.SUCCESS, pagination);
	}

	@Override
	@PostMapping(value = "/add", produces = GlobalContext.PRODUCES)
	public String add(@RequestBody NumRuleSetForm numRuleSetForm) {
		VerificationUtils.integer(RULEID, numRuleSetForm.getRuleid());
		VerificationUtils.string("max_value", numRuleSetForm.getMax_value());
		VerificationUtils.integer(STATUS, numRuleSetForm.getStatus());
		return resp(ResultCode.Success, ResultCode.SUCCESS, iNumRuleSetFactory.add(numRuleSetForm));

	}

	@Override
	@PostMapping(value = "/edit", produces = GlobalContext.PRODUCES)
	public String edit(@RequestBody NumRuleSetForm numRuleSetForm) {
		VerificationUtils.integer(RULEID, numRuleSetForm.getRuleid());
		VerificationUtils.string("max_value", numRuleSetForm.getMax_value());
		VerificationUtils.integer(STATUS, numRuleSetForm.getStatus());
		iNumRuleSetFactory.edit(numRuleSetForm);
		return resp(ResultCode.Success, ResultCode.SUCCESS, null);

	}

	@Override
	@PostMapping(value = "/get", produces = GlobalContext.PRODUCES)
	public String get(@RequestBody NumRuleSetForm numRuleSetForm) {
		VerificationUtils.integer(RULEID, numRuleSetForm.getRuleid());
		return resp(ResultCode.Success, ResultCode.SUCCESS, iNumRuleSetFactory.get(numRuleSetForm));

	}

	@PostMapping(value = "/base", produces = GlobalContext.PRODUCES)
	public String base() {
		return resp(ResultCode.Success, ResultCode.SUCCESS, iNumRuleSetFactory.base());

	}

	@PostMapping(value = "/changeStatus", produces = GlobalContext.PRODUCES)
	public String changeStatus(@RequestBody NumRuleSetForm numRuleSetForm) {
		VerificationUtils.integer(RULEID, numRuleSetForm.getRuleid());
		VerificationUtils.integer(STATUS, numRuleSetForm.getStatus());
		iNumRuleSetFactory.changeStatus(numRuleSetForm);
		return resp(ResultCode.Success, ResultCode.SUCCESS, null);

	}

	@PostMapping(value = "/makeNo", produces = GlobalContext.PRODUCES)
	public String makeNo(@RequestBody MakeNumRequest makeNumRequest) {
		if (makeNumRequest.getRuleid() == null) {
			return resp(ResultCode.RULE_TYPE_IS_NOT_NULL, ResultCode.getMessage(ResultCode.RULE_TYPE_IS_NOT_NULL),
					null);
		} else if (makeNumRequest.getCount() == null) {
			return resp(ResultCode.NUMBER_CANNOT_BE_EMPTY, ResultCode.getMessage(ResultCode.NUMBER_CANNOT_BE_EMPTY),
					null);
		} else if (makeNumRequest.getCount() <= 0
				|| makeNumRequest.getCount() > WaterConstants.NUM_RULE_SET_MAX_VALUE) {
			return resp(ResultCode.EXCEEDING_THE_LIMIT_VALUE,
					ResultCode.getMessage(ResultCode.EXCEEDING_THE_LIMIT_VALUE), null);
		}
		return resp(ResultCode.Success, ResultCode.SUCCESS, iNumRuleSetFactory.makeNo(makeNumRequest));

	}

}
