package com.dotop.smartwater.project.module.service.revenue;

import com.dotop.smartwater.dependence.core.common.BaseService;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.project.module.core.water.bo.DiscountBo;
import com.dotop.smartwater.project.module.core.water.vo.DiscountVo;

public interface IDiscountService extends BaseService<DiscountBo, DiscountVo> {

	Pagination<DiscountVo> find(DiscountBo discountBo);

	void save(DiscountBo discountBo);

	@Override
	DiscountVo edit(DiscountBo discountBo);

	void delete(DiscountBo discountBo);

	@Override
	DiscountVo get(DiscountBo discountBo);

	DiscountVo getisDefaultDiscount(DiscountBo discountBo);

	DiscountVo getDiscountById(String discountid);

}
