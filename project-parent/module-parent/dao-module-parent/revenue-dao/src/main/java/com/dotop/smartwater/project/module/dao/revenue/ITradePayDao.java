package com.dotop.smartwater.project.module.dao.revenue;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;

import com.dotop.smartwater.dependence.core.common.BaseDao;
import com.dotop.smartwater.project.module.core.water.dto.TradePayDto;
import com.dotop.smartwater.project.module.core.water.vo.TradePayVo;

/**

 * @date 2019年2月25日
 */
public interface ITradePayDao extends BaseDao<TradePayDto, TradePayVo> {

	TradePayVo getTradePay(String tradeno);

	TradePayVo findPayNo(TradePayDto tradePay);

	void updateTradePayStatus(@Param("payno") String payno, @Param("paystatus") int paystatus,
			@Param("remark") String remark);

	void addTradePay(TradePayDto tradePay);

	/**
	 * 批量查询 交易信息 账单对账处调用
	 *
	 * @param tradenos
	 * @return @
	 */
	@MapKey("tradeno")
	Map<String, TradePayVo> getTradePayByTradenos(@Param("tradenos") List<String> tradenos);

}
