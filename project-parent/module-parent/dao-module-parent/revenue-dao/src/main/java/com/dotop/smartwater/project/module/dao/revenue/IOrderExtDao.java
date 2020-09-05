package com.dotop.smartwater.project.module.dao.revenue;

import com.dotop.smartwater.dependence.core.common.BaseDao;
import com.dotop.smartwater.project.module.core.water.dto.OrderExtDto;
import com.dotop.smartwater.project.module.core.water.vo.OrderExtVo;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**

 * @date 2019年2月25日
 */
public interface IOrderExtDao extends BaseDao<OrderExtDto, OrderExtVo> {

	OrderExtVo findOrderExtByTradeno(String tradeno);

	void deleteOrderExtByOwnerId(String ownerid);

	void deleteOrderExtByTradeno(String tradeno);

	void addOrderExt(OrderExtDto orderExtDto);

	void updateOrderExt(OrderExtDto orderExt);

	/**
	 * 批量查询工单拓展信息
	 *
	 * @param tradenos
	 * @return
	 * @
	 */
	@MapKey("tradeno")
	Map<String, OrderExtVo> findOrderExtByTradenos(@Param("tradenos") List<String> tradenos);
}
