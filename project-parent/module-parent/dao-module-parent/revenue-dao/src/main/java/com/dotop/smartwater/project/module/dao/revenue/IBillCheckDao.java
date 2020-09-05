package com.dotop.smartwater.project.module.dao.revenue;

import com.dotop.smartwater.dependence.core.common.BaseDao;
import com.dotop.smartwater.project.module.core.water.dto.BillCheckDto;
import com.dotop.smartwater.project.module.core.water.vo.BillCheckVo;

import java.util.List;

/**
 * 账单对账
 *

 * @date 2019年2月23日
 */
public interface IBillCheckDao extends BaseDao<BillCheckDto, BillCheckVo> {

	@Override
	List<BillCheckVo> list(BillCheckDto billCheckDto);

	@Override
	BillCheckVo get(BillCheckDto billCheckDto);

	@Override
	void add(BillCheckDto billCheckDto);

	@Override
	Integer edit(BillCheckDto billCheckDto);

	@Override
	Integer del(BillCheckDto billCheckDto);

	@Override
	Boolean isExist(BillCheckDto billCheckDto);

	Integer editStatus(BillCheckDto billCheckDto);
}
