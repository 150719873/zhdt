package com.dotop.smartwater.project.module.service.revenue;

import com.dotop.smartwater.dependence.core.common.BaseService;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.project.module.core.water.bo.BillCheckBo;
import com.dotop.smartwater.project.module.core.water.vo.BillCheckVo;

/**
 * 账单对账
 * 

 * @date 2019年2月23日
 */
public interface IBillCheckService extends BaseService<BillCheckBo, BillCheckVo> {

	@Override
	Pagination<BillCheckVo> page(BillCheckBo billCheckBo);

	@Override
	BillCheckVo get(BillCheckBo billCheckBo);

	@Override
	BillCheckVo add(BillCheckBo billCheckBo);

	@Override
	String del(BillCheckBo billCheckBo);

	@Override
	BillCheckVo edit(BillCheckBo billCheckBo);

	void editStatus(BillCheckBo billCheckBo);

}
