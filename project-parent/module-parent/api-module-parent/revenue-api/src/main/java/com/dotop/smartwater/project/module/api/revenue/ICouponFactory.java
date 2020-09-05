package com.dotop.smartwater.project.module.api.revenue;

import com.dotop.smartwater.dependence.core.common.BaseFactory;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.project.module.core.water.form.CouponForm;
import com.dotop.smartwater.project.module.core.water.vo.CouponVo;

/**
 * Coupon 接口
 */
public interface ICouponFactory extends BaseFactory<CouponForm, CouponVo> {

	/**
	 * @param couponForm 参数对象
	 * @return 结果
	 */
	@Override
	CouponVo add(CouponForm couponForm) ;

	/**
	 * 分页
	 * @param couponForm 参数对象
	 * @return 分页
	 */
	Pagination<CouponVo> getCouponList(CouponForm couponForm) ;

	/**
	 * 批量添加
	 * @param couponForm 参数对象
	 */
	void batchAdd(CouponForm couponForm) ;

	/**
	 * 获取
	 * @param couponForm 参数对象
	 * @return Coupon对象
	 */
	CouponVo getCoupon(CouponForm couponForm) ;

	/**
	 * 删除
	 * @param couponForm 参数对象
	 */
	void delete(CouponForm couponForm) ;

	/**
	 * 禁用
	 * @param couponForm 参数对象
	 */
	void disable(CouponForm couponForm) ;

	/**
	 * 优惠券检查任务
	 */
	void checkTask() ;

}
