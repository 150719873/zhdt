package com.dotop.smartwater.project.module.api.revenue;

import com.dotop.smartwater.dependence.core.common.BaseFactory;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.project.module.core.water.form.DiscountForm;
import com.dotop.smartwater.project.module.core.water.vo.DiscountVo;

/**
 * Discount接口

 */
public interface IDiscountFactory extends BaseFactory<DiscountForm, DiscountVo> {

	/**
	 * 分页列表
	 * @param discountForm 参数对象
	 * @return 分页信息
	 */
	Pagination<DiscountVo> find(DiscountForm discountForm) ;

	/**
	 * 保存 discount
	 * @param discountForm 参数对象
	 */
	void save(DiscountForm discountForm) ;

	@Override
	DiscountVo edit(DiscountForm f) ;

	/**
	 * 删除
	 * @param discountForm 参数对象
	 */
	void delete(DiscountForm discountForm) ;

	@Override
	DiscountVo get(DiscountForm discountForm) ;

}
