package com.dotop.smartwater.project.module.dao.revenue;

import com.dotop.smartwater.dependence.core.common.BaseDao;
import com.dotop.smartwater.project.module.core.water.dto.PayDetailDto;
import com.dotop.smartwater.project.module.core.water.vo.PayDetailVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface IPayDetailDao extends BaseDao<PayDetailDto, PayDetailVo> {
	@Override
	List<PayDetailVo> list(PayDetailDto payDetailDto);

	void deletePayDetail(@Param("tradeno")String tradeno);

	PayDetailVo findTradePay(@Param("tradeno")String tradeno);

	int addPayDetail(PayDetailDto payDetailDto);

	int updateOwnerAlreadypay(@Param("ownerid") String ownerid,
							  @Param("alreadypay") Double alreadypay);
}
