package com.dotop.smartwater.project.server.water.rest.service.revenue;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import com.dotop.smartwater.dependence.core.common.BaseController;
import com.dotop.smartwater.dependence.core.global.GlobalContext;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.dependence.core.utils.VerificationUtils;
import com.dotop.smartwater.project.module.api.revenue.IWrongAccountFactory;
import com.dotop.smartwater.project.module.core.water.constants.ResultCode;
import com.dotop.smartwater.project.module.core.water.form.WrongAccountForm;
import com.dotop.smartwater.project.module.core.water.vo.WrongAccountVo;

/**
 * 

 * @Date 2019年2月25日
 * 
 *       原com.dotop.water.controller.WrongAccountController
 */
@RestController

@RequestMapping("/WrongAccount")
public class WrongAccountController implements BaseController<WrongAccountForm> {

	@Resource
	private IWrongAccountFactory iWrongAccountFactory;

	// 错账分页查询
	@Override
	@PostMapping(value = "/page", produces = GlobalContext.PRODUCES)
	public String page(@RequestBody WrongAccountForm wrongAccountForm) {

		Pagination<WrongAccountVo> pagination = iWrongAccountFactory.page(wrongAccountForm);
		return resp(ResultCode.Success, ResultCode.SUCCESS, pagination);

	}

	// 订单查询
	@Override
	@PostMapping(value = "/get", produces = GlobalContext.PRODUCES)
	public String get(@RequestBody WrongAccountForm wrongAccountForm) {
		// 参数校验
		String tradeno = wrongAccountForm.getTradeno();
		String id = wrongAccountForm.getId();

		if (StringUtils.isBlank(tradeno)) {
			if (StringUtils.isBlank(id)) {
				return resp(ResultCode.Fail, "账单流水号不能为空", null);
			} else {
				// 说明waid不为空
			}
		}
		WrongAccountVo wrongAccountVo = iWrongAccountFactory.get(wrongAccountForm);
		return resp(ResultCode.Success, ResultCode.SUCCESS, wrongAccountVo);
	}

	// 新增
	@Override
	@PostMapping(value = "/add", produces = GlobalContext.PRODUCES)
	public String add(@RequestBody WrongAccountForm wrongAccountForm) {
		// 参数校验
		String orderid = wrongAccountForm.getOrderid();
		Integer type = wrongAccountForm.getType();
		String description = wrongAccountForm.getDescription();
		VerificationUtils.string("orderid", orderid);
		VerificationUtils.integer("type", type);
		VerificationUtils.string("description", description, false, 500);

		iWrongAccountFactory.add(wrongAccountForm);
		return resp(ResultCode.Success, ResultCode.SUCCESS, null);
	}

	// 修改
	@PostMapping(value = "/update", produces = GlobalContext.PRODUCES)
	public String update(@RequestBody WrongAccountForm wrongAccountForm) {
		String id = wrongAccountForm.getId();
		String handedesc = wrongAccountForm.getHandedesc();
		String description = wrongAccountForm.getDescription();
		VerificationUtils.string("id", id);
		VerificationUtils.string("handedesc", handedesc, false, 500);
		VerificationUtils.string("description", description, false, 500);

		iWrongAccountFactory.update(wrongAccountForm);
		return resp(ResultCode.Success, ResultCode.SUCCESS, null);
	}

	// 完成
	@PostMapping(value = "/complete", produces = GlobalContext.PRODUCES)
	public String complete(@RequestBody WrongAccountForm wrongAccountForm) throws IOException {
		String id = wrongAccountForm.getId();
		VerificationUtils.string("id", id);

		iWrongAccountFactory.complete(wrongAccountForm);
		return resp(ResultCode.Success, ResultCode.SUCCESS, null);
	}

	// 撤销
	@PostMapping(value = "/cancel", produces = GlobalContext.PRODUCES)
	public String cancel(@RequestBody WrongAccountForm wrongAccountForm) {
		String id = wrongAccountForm.getId();
		VerificationUtils.string("id", id);

		iWrongAccountFactory.cancel(wrongAccountForm);
		return resp(ResultCode.Success, ResultCode.SUCCESS, null);
	}

	// 新增抵扣
	@PostMapping(value = "/addcoupon", produces = GlobalContext.PRODUCES)
	public String addCoupon(@RequestBody WrongAccountForm wrongAccountForm) {
		String id = wrongAccountForm.getId();
		String couponname = wrongAccountForm.getCouponname();
		Date couponstarttime = wrongAccountForm.getCouponstarttime();
		Date couponendtime = wrongAccountForm.getCouponendtime();
		BigDecimal facevalue = wrongAccountForm.getFacevalue();
		Integer couponunit = wrongAccountForm.getCouponunit();
		VerificationUtils.string("id", id);
		VerificationUtils.string("couponname", couponname);
		VerificationUtils.date("couponstarttime", couponstarttime);
		VerificationUtils.date("couponendtime", couponendtime);
		VerificationUtils.bigDecimal("facevalue", facevalue);
		VerificationUtils.integer("couponunit", couponunit);

		iWrongAccountFactory.addCoupon(wrongAccountForm);
		return resp(ResultCode.Success, ResultCode.SUCCESS, null);
	}

}
