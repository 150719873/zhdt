package com.dotop.smartwater.project.module.service.revenue;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.dotop.smartwater.dependence.core.common.BaseService;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.project.module.core.auth.vo.UserVo;
import com.dotop.smartwater.project.module.core.water.bo.OrderBo;
import com.dotop.smartwater.project.module.core.water.bo.OrderExtBo;
import com.dotop.smartwater.project.module.core.water.bo.OrderPreviewBo;
import com.dotop.smartwater.project.module.core.water.form.OrderExtForm;
import com.dotop.smartwater.project.module.core.water.form.TradePayForm;
import com.dotop.smartwater.project.module.core.water.form.customize.PreviewForm;
import com.dotop.smartwater.project.module.core.water.vo.CouponVo;
import com.dotop.smartwater.project.module.core.water.vo.OrderExtVo;
import com.dotop.smartwater.project.module.core.water.vo.OrderPreviewVo;
import com.dotop.smartwater.project.module.core.water.vo.OrderVo;
import com.dotop.smartwater.project.module.core.water.vo.PayTypeVo;
import com.dotop.smartwater.project.module.core.water.vo.PurposeVo;
import com.dotop.smartwater.project.module.core.water.vo.ReduceVo;
import com.dotop.smartwater.project.module.core.water.vo.TradePayVo;
import com.dotop.smartwater.project.module.core.water.vo.customize.AutoPayVo;
import com.dotop.smartwater.project.module.core.water.vo.customize.IncomeVo;
import com.dotop.smartwater.project.module.core.water.vo.customize.LadderPriceDetailVo;
import com.dotop.smartwater.project.module.core.water.vo.customize.LastUpLinkVo;

/**

 */
public interface IOrderService extends BaseService<OrderBo, OrderVo> {

	List<OrderVo> orderList(OrderBo orderBo);

	List<OrderVo> orderListByWeChat(OrderBo orderBo);

	List<OrderVo> list(OrderPreviewBo orderPreviewBo);

	OrderVo findByUserNo(String enterpriseId, String userNo);

	/**
	 * 生成支付二维码
	 * 
	 * @param enterpriseId
	 * @param userNo
	 * @return
	 * @throws FrameworkRuntimeException
	 */
	Map<String, String> generatePayQrCode(String enterpriseId, String userNo, String ip);

	LastUpLinkVo findLastUplink(String devId, String currentMonth, String lastMonth, String meterTime);

	Pagination<OrderVo> bills(OrderPreviewBo orderPreviewBo, String ids);

	Pagination<OrderPreviewVo> orderPreviewList(PreviewForm previewForm);

	Map<String, LastUpLinkVo> findLastUplinkList(PreviewForm previewForm, String currentMonth, String lastMonth,
			String meterTime);

	Map<String, OrderVo> findByUserNoMap(String enterpriseid, String usernos);

	void deletePreview(String ownerId);

	void clearPreview(PreviewForm previewForm, boolean flag);

	List<OrderPreviewVo> findOrderPreview(PreviewForm previewForm);

	int addOrders(PreviewForm previewForm);

	Pagination<OrderVo> orderList(OrderPreviewBo orderPreviewBo);

	OrderVo findById(String id);

	void deleteById(OrderBo orderBo);

	OrderVo findByOwnerId(String ownerId);

	void updateOrderPreviewStatus(String ownerId);

	OrderExtVo findOrderExtByTradeno(String tradeNo);

	int findSubOrderCountByTradeNo(String tradeNo);

	BigDecimal findOwnerPayByPayNo(String payNo);

	TradePayVo getTradePay(String tradeNo);

	IncomeVo statisIncome(String enterpriseId);

	OrderVo findOrderByTradeNo(String tradeNo);

	Map<String, String> getOrderPayStatus(OrderVo order, String payNo, String enterpriseId);

	String mergeOrders(String orderids, UserVo user);

	Map<String, String> orderPay(OrderExtForm orderExtForm, OrderVo order, UserVo user, Double ownerBalance,
			int payMode, CouponVo coupon);

	int addOrder(OrderBo order, PayTypeVo payType, ReduceVo reduce, PurposeVo purpose,
			List<LadderPriceDetailVo> payDetailList);

	void addOrderPreviewList(List<OrderPreviewBo> list);

	void addOrderExtList(List<OrderExtBo> extList);

	/**
	 * 删除Order_Ext数据
	 * <p>
	 */
	void clearOrderPreviewExtByCondition(Object obj);

	/**
	 * 处理自动扣费的账单
	 */
	List<AutoPayVo> getAutoPay(String enterpriseid);

	/**
	 * 账单对账处调用 查询一段时间内的账单信息
	 *
	 * @param operEid
	 * @param startDate
	 * @param endDate
	 * @return @
	 */
	List<OrderVo> getListBetweenDate(String operEid, String startDate, String endDate);

	/**
	 * 根据账单id 批量查询账单信息 账单对账调用
	 *
	 * @param orderIds
	 * @return @
	 */
	List<OrderVo> findByIds(List<String> orderIds);

	/**
	 * 根据账单流水号 批量查询 账单拓展信息 账单对账调用
	 * 
	 * @param tradenos
	 * @return @
	 */
	Map<String, OrderExtVo> findOrderExtByTradenos(List<String> tradenos);

	/**
	 * 批量查询 交易信息 账单对账处调用
	 * 
	 * @param tradenos
	 * @return @
	 */
	Map<String, TradePayVo> getTradePayByTradenos(List<String> tradenos);

	/**
	 * 批量查询是否有子菜单
	 * 
	 * @return
	 */
	Map<String, Map> findSubOrderCountByTradeNo(List<String> tradenos);

	/**
	 * 删除交易信息
	 * 
	 * @param payno @
	 */
	void deleteTradePay(String payno);

	/**
	 * 修改代金券状态为未使用
	 * 
	 * @param tradeno @
	 */
	void updateCouponStatus(String tradeno);

	/**
	 * 修改账单信息
	 * 
	 * @param orderDto @
	 */
	void revokeOrder(OrderBo orderBo);

	void updateOrder(OrderBo orderBo);

	void updateTradePayStatus(String payno, String paystatus, String remark);

	void updateSubOrder(String tradeno);

	Pagination<OrderPreviewVo> auditingOrderPreviewList(PreviewForm previewForm);

	int addOrdersByAuditing(PreviewForm previewForm);

	void clearPreviewByAuditing(PreviewForm previewForm, Boolean flag);

	void updateOrderPreviewApprovalResult(PreviewForm previewForm);

	int addTradePay(TradePayForm tradePayForm);

	TradePayVo findPayNo(TradePayForm tradePayForm);
}
