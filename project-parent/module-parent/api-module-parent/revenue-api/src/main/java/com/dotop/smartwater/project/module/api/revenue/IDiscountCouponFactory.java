package com.dotop.smartwater.project.module.api.revenue;

import com.dotop.smartwater.dependence.core.common.BaseFactory;
import com.dotop.smartwater.dependence.core.common.BaseVo;
import com.dotop.smartwater.project.module.core.water.form.customize.RechargeForm;

/**
 * DiscountCoupon 接口

 */
public interface IDiscountCouponFactory extends BaseFactory<RechargeForm, BaseVo> {

	/**
	 * 生成DiscountCoupon
	 * @param rechargeForm DiscountCoupon
	 */
	void generateCoupon(RechargeForm rechargeForm);

}
