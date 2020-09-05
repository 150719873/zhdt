package com.dotop.smartwater.project.module.dao.revenue;

import com.dotop.smartwater.dependence.core.common.BaseDao;
import com.dotop.smartwater.project.module.core.water.dto.BillDto;
import com.dotop.smartwater.project.module.core.water.form.customize.BalanceChangeParamForm;
import com.dotop.smartwater.project.module.core.water.vo.BillVo;
import com.dotop.smartwater.project.module.core.water.vo.customize.PayDetailRecord;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @program: project-parent
 * @description: 账单相关

 * @create: 2019-02-28 18:32
 **/
public interface IBillDao extends BaseDao<BillDto, BillVo> {
	List<BillVo> getList(BillDto billDto);

	@Override
	void add(BillDto billDto);

	@Override
	Boolean isExist(BillDto billDto);

	Integer update(BillDto billDto);

	BillVo getById(@Param("id") String id);

	List<PayDetailRecord> balanceFind(BalanceChangeParamForm balanceChangeParam);

	PayDetailRecord findDetailRecord(BalanceChangeParamForm balanceChangeParam);
}
