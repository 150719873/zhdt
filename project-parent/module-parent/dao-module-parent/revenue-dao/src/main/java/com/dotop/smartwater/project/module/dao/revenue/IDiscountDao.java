package com.dotop.smartwater.project.module.dao.revenue;

import com.dotop.smartwater.dependence.core.common.BaseDao;
import com.dotop.smartwater.project.module.core.water.bo.DiscountBo;
import com.dotop.smartwater.project.module.core.water.dto.DiscountDto;
import com.dotop.smartwater.project.module.core.water.vo.DiscountVo;

import java.util.List;

public interface IDiscountDao extends BaseDao<DiscountDto, DiscountVo> {

	List<DiscountVo> find(DiscountBo discountBo);

	void updateDiscountDefault(String enterpriseid);

	void save(DiscountDto discountDto);

	@Override
	Integer edit(DiscountDto discountDto);

	void deleteConds(DiscountDto discountDto);

	void delete(DiscountDto discountDto);

	DiscountVo getDiscount(DiscountDto discountDto);

	DiscountVo getisDefaultDiscount(DiscountDto discountDto);

}
