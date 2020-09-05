package com.dotop.smartwater.project.module.service.revenue;

import com.dotop.smartwater.dependence.core.common.BaseService;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.project.module.core.water.bo.PayDetailBo;
import com.dotop.smartwater.project.module.core.water.vo.PayDetailVo;

/**

 */
public interface IPayDetailService extends BaseService<PayDetailBo, PayDetailVo> {

	@Override
	Pagination<PayDetailVo> page(PayDetailBo payDetail);

	PayDetailVo findTradePay(String tradeno);

	void deletePayDetail(String tradeno);

	int addPayDetail(PayDetailBo payDetail);

	void addPayDetail(PayDetailBo payDetail, Double alreadypay);

}
