package com.dotop.smartwater.project.module.dao.revenue;

import com.dotop.smartwater.dependence.core.common.BaseDao;
import com.dotop.smartwater.project.module.core.water.dto.CouponDto;
import com.dotop.smartwater.project.module.core.water.vo.CouponVo;

import java.util.List;

public interface ICouponDao extends BaseDao<CouponDto, CouponVo> {

	@Override
	List<CouponVo> list(CouponDto couponDto);

	void addCoupon(CouponDto couponDto);

	CouponVo getCoupon(CouponDto couponDto);

	void delete(CouponDto couponDto);

	void disable(CouponDto couponDto);

	@Override
	Integer edit(CouponDto couponDto);
}
