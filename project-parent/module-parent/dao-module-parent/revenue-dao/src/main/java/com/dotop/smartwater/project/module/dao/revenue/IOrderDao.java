package com.dotop.smartwater.project.module.dao.revenue;

import com.dotop.smartwater.dependence.core.common.BaseDao;
import com.dotop.smartwater.project.module.core.water.bo.OrderExtBo;
import com.dotop.smartwater.project.module.core.water.bo.OrderPreviewBo;
import com.dotop.smartwater.project.module.core.water.dto.*;
import com.dotop.smartwater.project.module.core.water.form.customize.PreviewForm;
import com.dotop.smartwater.project.module.core.water.vo.OrderVo;
import com.dotop.smartwater.project.module.core.water.vo.customize.AutoPayVo;
import com.dotop.smartwater.project.module.core.water.vo.customize.IncomeVo;
import com.dotop.smartwater.project.module.core.water.vo.customize.LastUpLinkVo;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface IOrderDao extends BaseDao<OrderDto, OrderVo> {

	List<OrderVo> orderList(OrderPreviewDto orderPreviewDto);

	List<OrderVo> orderListByWeChat(OrderPreviewDto orderPreviewDto);

	OrderVo findByUserNo(@Param("enterpriseid") String enterpriseid, @Param("userno") String userno);

	LastUpLinkVo findLastUplink(@Param("devid") String devid, @Param("currentMonth") String currentMonth,
	                            @Param("lastMonth") String lastMonth, @Param("meterTime") String meterTime);

	List<OrderVo> bills(OrderPreviewDto orderPreviewDto);

	@MapKey("devid")
	Map<String, LastUpLinkVo> findLastUplinkList(OrderLastUplinkDto orderLastUplinkDto);

	@MapKey("userno")
	Map<String, OrderVo> findByUserNoMap(@Param("enterpriseid") String enterpriseid, @Param("usernos") String usernos);

	void addOrders(PreviewForm previewForm);

	OrderVo findById(@Param("id") String id);

	void delete(@Param("id") String id);

	OrderVo findByOwnerId(@Param("ownerId") String ownerId);

	int findSubOrderCountByTradeNo(@Param("tradeno") String tradeno);

	Double findOwnerPayByPayNo(@Param("payno") String payno);

	IncomeVo statisIncome(@Param("enterpriseid") String enterpriseid);

	OrderVo findOrderByTradeNo(@Param("tradeno") String tradeno);

	void updateOrder(OrderDto orderDto);

	List<OrderVo> findByIds(@Param("orderids") String orderids);

	int insertOrderSubInf(@Param("newTradeNo") String newTradeNo, @Param("subTradeNo") String subTradeNo);

	void addOrder(OrderDto orderDto);

	void updateOrderSub(@Param("orderids") String orderids);

	void updateSubOrder(@Param("tradeno") String tradeno);

	void addPayDetail(PayDetailDto payDetail);

	void couponIsUsed(CouponDto couponDto);

	void updateOwnerAlreadypay(@Param("ownerid") String ownerid, @Param("alreadypay") Double alreadypay)
			;

	void addOrderPreviewList(@Param("list") List<OrderPreviewBo> list);

	void addOrderExtList(@Param("list") List<OrderExtBo> extList);

	List<OrderVo> getListBetweenDate(@Param("operEid") String operEid, @Param("startDate") String startDate,
	                                 @Param("endDate") String endDate);

	@MapKey("tradeno")
	Map<String, Map> findSubOrderCountByTradeNoMap(@Param("tradenos") List<String> tradenos);

	void deleteTradePay(@Param("payno") String payno);

	void updateCouponStatus(@Param("tradeno") String tradeno);

	void revokeOrder(OrderDto orderDto);

	void updateTradePayStatus(@Param("payno") String payno, @Param("paystatus") String paystatus,
	                          @Param("remark") String remark);

	void addOrdersByAuditing(PreviewForm previewForm);

	List<AutoPayVo> getAutoPay(@Param("enterpriseid") String enterpriseid);
}
