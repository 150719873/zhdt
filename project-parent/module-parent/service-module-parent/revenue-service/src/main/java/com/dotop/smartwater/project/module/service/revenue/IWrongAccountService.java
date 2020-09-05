package com.dotop.smartwater.project.module.service.revenue;

import java.util.List;

import com.dotop.smartwater.dependence.core.common.BaseService;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.project.module.core.water.bo.WrongAccountBo;
import com.dotop.smartwater.project.module.core.water.vo.WrongAccountVo;

public interface IWrongAccountService extends BaseService<WrongAccountBo, WrongAccountVo> {

	@Override
	Pagination<WrongAccountVo> page(WrongAccountBo wrongAccountBo);

	@Override
	WrongAccountVo get(WrongAccountBo wrongAccountBo);

	@Override
	WrongAccountVo add(WrongAccountBo wrongAccountBo);

	WrongAccountVo update(WrongAccountBo wrongAccountBo);

	void complete(WrongAccountBo wrongAccountBo);

	void cancel(WrongAccountBo wrongAccountBo);

	/**
	 * 根据业主ID获取错账的状态
	 * 
	 * ownerid
	 */
	List<WrongAccountVo> getStatus(WrongAccountBo wrongAccountBo);

	/**
	 * 错账订单历史是否存在
	 * 
	 * ownerid/wrongno
	 * 
	 */
	@Override
	boolean isExist(WrongAccountBo wrongAccountBo);

	/**
	 * 错账订单是否存在处理
	 * 
	 * orderid
	 */
	boolean ishandle(WrongAccountBo wrongAccountBo);

	void updateCoupon(WrongAccountBo wrongAccountBo);

}
