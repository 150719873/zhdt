package com.dotop.smartwater.project.module.api.revenue;

import com.dotop.smartwater.dependence.core.common.BaseFactory;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.project.module.core.water.form.BillBadForm;
import com.dotop.smartwater.project.module.core.water.vo.BillBadVo;

/**
 * 账单坏账
 * 

 * @date 2019年2月23日
 */
public interface IBillBadFactory extends BaseFactory<BillBadForm, BillBadVo> {

	/**
	 * @param billBadForm 参数对象
	 * @return 分页
	 */
	@Override
	Pagination<BillBadVo> page(BillBadForm billBadForm) ;

	/**
	 * 坏账
	 * @param billBadForm 参数对象
	 * @return 分页
	 */
	Pagination<BillBadVo> getBillBadPage(BillBadForm billBadForm) ;

	/**
	 * 标记坏账
	 * @param billBadForm 参数对象
	 * @return 结果
	 */
	String markBadBill(BillBadForm billBadForm);
	
//	public BillBadVo add(BillBadForm billBadForm) ;
//
//	public String del(BillBadForm billBadForm) ;



}
