package com.dotop.smartwater.project.module.service.revenue;

import java.util.List;

import com.dotop.smartwater.dependence.core.common.BaseService;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.project.module.core.water.bo.CouponBo;
import com.dotop.smartwater.project.module.core.water.vo.CouponVo;

public interface ICouponService extends BaseService<CouponBo, CouponVo> {

	Pagination<CouponVo> getCouponList(CouponBo couponBo);

	CouponVo addCoupon(CouponBo couponBo);

	CouponVo getByNo(CouponBo couponBo);

	CouponVo getCoupon(CouponBo couponBo);

	void delete(CouponBo couponBo);

	void disable(CouponBo couponBo);

	@Override
	List<CouponVo> list(CouponBo couponBo);

	/**
	 * 生成流水号
	 */
	String genNo();

	int editStatus(CouponBo couponBo);
}
