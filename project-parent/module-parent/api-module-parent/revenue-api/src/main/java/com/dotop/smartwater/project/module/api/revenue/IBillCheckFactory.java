package com.dotop.smartwater.project.module.api.revenue;

import com.dotop.smartwater.dependence.core.common.BaseFactory;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.project.module.core.water.form.BillCheckForm;
import com.dotop.smartwater.project.module.core.water.vo.BillCheckVo;

/**
 * 账单对账
 * 

 * @date 2019年2月23日
 */
public interface IBillCheckFactory extends BaseFactory<BillCheckForm, BillCheckVo> {

	@Override
	Pagination<BillCheckVo> page(BillCheckForm billCheckForm) ;

	@Override
	BillCheckVo get(BillCheckForm billCheckForm) ;

	@Override
	BillCheckVo add(BillCheckForm billCheckForm) ;

	@Override
	String del(BillCheckForm billCheckForm) ;

	/**
	 * 发送
	 * @param billCheckForm 参数对象
	 * @return BillCheckVo对象
	 */
	BillCheckVo sendCheck(BillCheckForm billCheckForm) ;

	/**
	 * 检查
	 * @param billCheckForm 参数对象
	 * @return BillCheckVo对象
	 */
	BillCheckVo closeCheck(BillCheckForm billCheckForm) ;

	/**
	 * 编辑
	 * @param billCheckForm 参数对象
	 * @return BillCheckVo对象
	 */
	BillCheckVo editStatus(BillCheckForm billCheckForm) ;

}
