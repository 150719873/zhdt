package com.dotop.smartwater.project.module.service.revenue;

import com.dotop.smartwater.dependence.core.common.BaseService;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.project.module.core.water.bo.BillBo;
import com.dotop.smartwater.project.module.core.water.form.customize.BalanceChangeParamForm;
import com.dotop.smartwater.project.module.core.water.vo.BillVo;
import com.dotop.smartwater.project.module.core.water.vo.customize.PayDetailRecord;

/**

 */
public interface IBillService extends BaseService<BillBo, BillVo> {

	Pagination<BillVo> getList(BillBo billBo);

	@Override
	BillVo add(BillBo billBo);

	@Override
	boolean isExist(BillBo billBo);

	@Override
	BillVo edit(BillBo billBo);

	BillVo getById(String id);

	Pagination<PayDetailRecord> balanceFind(BalanceChangeParamForm balanceChangeParam);

	PayDetailRecord findDetailRecord(BalanceChangeParamForm balanceChangeParam);
}
