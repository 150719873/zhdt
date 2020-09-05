package com.dotop.smartwater.project.server.water.rest.service.revenue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.dotop.smartwater.dependence.core.common.BaseController;
import com.dotop.smartwater.dependence.core.global.GlobalContext;
import com.dotop.smartwater.project.module.api.revenue.IDiscountCouponFactory;
import com.dotop.smartwater.project.module.core.water.constants.ResultCode;
import com.dotop.smartwater.project.module.core.water.form.customize.RechargeForm;

/**
 * 

 * @Date 2019年2月25日
 * 
 *       原com.dotop.water.controller.CouponController
 */
@RestController

@RequestMapping(value = "/discount")
public class DiscountCouponController implements BaseController<RechargeForm> {

	@Autowired
	private IDiscountCouponFactory iDiscountCouponFactory;

	/**
	 * 根据优惠活动生成代金券
	 */
	@PostMapping(value = "/generateCoupon", produces = GlobalContext.PRODUCES)
	public String generateCoupon(@RequestBody RechargeForm rechargeForm) {
		// 参数校验
		iDiscountCouponFactory.generateCoupon(rechargeForm);
		return resp(ResultCode.Success, ResultCode.SUCCESS, null);

	}
}
