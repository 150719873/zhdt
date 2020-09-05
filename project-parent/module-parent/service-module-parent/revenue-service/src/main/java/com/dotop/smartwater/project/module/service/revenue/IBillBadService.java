package com.dotop.smartwater.project.module.service.revenue;

import java.util.List;

import com.dotop.smartwater.dependence.core.common.BaseService;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.project.module.core.water.bo.BillBadBo;
import com.dotop.smartwater.project.module.core.water.vo.BillBadVo;
import com.dotop.smartwater.project.module.core.water.vo.OrderVo;

/**
 * 账单坏账
 * 

 * @date 2019年2月23日
 */
public interface IBillBadService extends BaseService<BillBadBo, BillBadVo> {

	@Override
	Pagination<BillBadVo> page(BillBadBo billBadBo);

	/*
	 * 批量插入异常的账单到坏账表 默认不是坏账
	 */
	void addList(BillBadBo billBadBo, List<OrderVo> list);

	Pagination<BillBadVo> getBillBadPage(BillBadBo billBadBo);

	void markBadBill(BillBadBo billBadBo);

	/**
	 * 修改坏账账单的流程id
	 */
	@Override
	BillBadVo edit(BillBadBo billBadBo);

	@Override
	boolean isExist(BillBadBo billBadBo);

	/**
	 * 将标记为坏账的账单 在流程审批失败后 取消标记为坏账
	 * 
	 * @param billBadBo
	 * @
	 */
	void editProcessOver(BillBadBo billBadBo);
}
