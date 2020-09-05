package com.dotop.smartwater.project.server.water.rest.service.revenue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.dotop.smartwater.dependence.core.common.BaseController;
import com.dotop.smartwater.dependence.core.global.GlobalContext;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.dependence.core.utils.VerificationUtils;
import com.dotop.smartwater.project.module.api.revenue.IDiscountConditionFactory;
import com.dotop.smartwater.project.module.core.water.constants.ResultCode;
import com.dotop.smartwater.project.module.core.water.form.ConditionForm;
import com.dotop.smartwater.project.module.core.water.vo.ConditionVo;

/**
 * 

 * @Date 2019年2月25日
 * 
 *       原com.dotop.water.controller.CouponController
 */
@RestController

@RequestMapping(value = "/discount")
public class DiscountConditionController implements BaseController<ConditionForm> {

	@Autowired
	private IDiscountConditionFactory iDiscountConditionFactory;

	/**
	 * 保存优惠活动信息
	 */
	@PostMapping(value = "/savecondition", produces = GlobalContext.PRODUCES)
	public String savecondition(@RequestBody ConditionForm conditionForm) {
		// 参数校验

		iDiscountConditionFactory.savecondition(conditionForm);
		return resp(ResultCode.Success, ResultCode.SUCCESS, null);
	}

	/**
	 * 修改活动规则
	 */
	@PostMapping(value = "/editcondition", produces = GlobalContext.PRODUCES)
	public String editcondition(@RequestBody ConditionForm conditionForm) {
		// 参数校验

		iDiscountConditionFactory.editcondition(conditionForm);
		return resp(ResultCode.Success, ResultCode.SUCCESS, null);
	}

	/**
	 * 根据ID查询活动规则
	 */
	@PostMapping(value = "/getcondition", produces = GlobalContext.PRODUCES)
	public String getcondition(@RequestBody ConditionForm conditionForm) {
		// 参数校验
		String id = conditionForm.getId();
		VerificationUtils.string("id", id);

		ConditionVo condition = iDiscountConditionFactory.getCondition(conditionForm);
		return resp(ResultCode.Success, ResultCode.SUCCESS, condition);

	}

	/**
	 * 删除活动规则
	 */
	@PostMapping(value = "/deletecondition", produces = GlobalContext.PRODUCES)
	public String deletecondition(@RequestBody ConditionForm conditionForm) {
		// 参数校验
		String id = conditionForm.getId();
		VerificationUtils.string("id", id);

		iDiscountConditionFactory.deleteCond(conditionForm);
		return resp(ResultCode.Success, ResultCode.SUCCESS, null);

	}

	/**
	 * 查询优惠活动
	 */
	@PostMapping(value = "/findcondition", produces = GlobalContext.PRODUCES)
	public String findcondition(@RequestBody ConditionForm conditionForm) {
		// 参数校验

		Pagination<ConditionVo> pagination = iDiscountConditionFactory.findcondition(conditionForm);
		return resp(ResultCode.Success, ResultCode.SUCCESS, pagination);

	}
}
