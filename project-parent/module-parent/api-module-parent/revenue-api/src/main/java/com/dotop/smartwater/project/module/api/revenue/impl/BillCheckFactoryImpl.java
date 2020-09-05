package com.dotop.smartwater.project.module.api.revenue.impl;

import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.dependence.core.utils.DateUtils;
import com.dotop.smartwater.dependence.core.utils.JSONUtils;
import com.dotop.smartwater.dependence.core.utils.UuidUtils;
import com.dotop.smartwater.project.module.api.revenue.IBillCheckFactory;
import com.dotop.smartwater.project.module.core.auth.local.AuthCasClient;
import com.dotop.smartwater.project.module.core.auth.vo.UserVo;
import com.dotop.smartwater.project.module.core.water.bo.BillBadBo;
import com.dotop.smartwater.project.module.core.water.bo.BillCheckBo;
import com.dotop.smartwater.project.module.core.water.constants.ResultCode;
import com.dotop.smartwater.project.module.core.water.form.BillCheckForm;
import com.dotop.smartwater.project.module.core.water.utils.CalUtil;
import com.dotop.smartwater.project.module.core.water.vo.*;
import com.dotop.smartwater.project.module.core.water.vo.customize.LadderPriceDetailVo;
import com.dotop.smartwater.project.module.service.revenue.IBillBadService;
import com.dotop.smartwater.project.module.service.revenue.IBillCheckService;
import com.dotop.smartwater.project.module.service.revenue.IOrderService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 账单对账
 * 

 * @date 2019年2月23日
 */

@Component
public class BillCheckFactoryImpl implements IBillCheckFactory {

	@Autowired
	private IBillCheckService iBillCheckService;

	@Autowired
	private IOrderService iOrderService;

	@Autowired
	private IBillBadService iBillBadService;

	@Override
	public Pagination<BillCheckVo> page(BillCheckForm billCheckForm) throws FrameworkRuntimeException {
		UserVo user = AuthCasClient.getUser();
		String operEid = user.getEnterpriseid();
		BillCheckBo billCheckBo = new BillCheckBo();
		BeanUtils.copyProperties(billCheckForm, billCheckBo);
		billCheckBo.setEnterpriseid(operEid);
		Pagination<BillCheckVo> pagination = iBillCheckService.page(billCheckBo);
		return pagination;
	}

	@Override
	public BillCheckVo get(BillCheckForm billCheckForm) throws FrameworkRuntimeException {
		UserVo user = AuthCasClient.getUser();
		String operEid = user.getEnterpriseid();
		BillCheckBo billCheckBo = new BillCheckBo();
		billCheckBo.setBillCheckId(billCheckForm.getBillCheckId());
		billCheckBo.setEnterpriseid(operEid);
		return iBillCheckService.get(billCheckBo);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public BillCheckVo add(BillCheckForm billCheckForm) throws FrameworkRuntimeException {
		UserVo user = AuthCasClient.getUser();
		Date curr = new Date();
		String userBy = user.getName();
		String operEid = user.getEnterpriseid();
		String startDate = DateUtils.format(billCheckForm.getStartDate(), "yyyy-MM");
		String endDate = DateUtils.format(billCheckForm.getEndDate(), "yyyy-MM");
		// 复制属性
		BillCheckBo billCheckBo;
		List<OrderVo> list = iOrderService.getListBetweenDate(operEid, startDate, endDate);
		if (list == null || list.size() == 0) {
			throw new FrameworkRuntimeException(ResultCode.Fail, "没有账单,无需对账");
		}
		List<String> orderIds = new ArrayList<>();
		List<String> tradenos = new ArrayList<>();
		List<OrderVo> billBad = new ArrayList<>();
		for (OrderVo orderVo : list) {
			System.out.println("id:" + orderVo.getId() + "--------- 滞纳金:" + orderVo.getPenalty() + "------状态:"
					+ orderVo.getPaystatus());
			orderIds.add(orderVo.getId());
			tradenos.add(orderVo.getTradeno());
			// 0 未缴 1 已缴
			if (orderVo.getPaystatus() == 0) {
				billBad.add(orderVo);
			}
		}
		// 计算处理
		billCheckBo = getBillDetails(list, tradenos);

		// 如果有订单没有缴费 则状态异常
		if (billCheckBo.getAmountNotPaid().compareTo(new BigDecimal(0)) == 1
				|| billCheckBo.getPenaltyNotPaid().compareTo(new BigDecimal(0)) == 1) {
			billCheckBo.setBillStatus("1"); // 账单状态 0 正常 1 异常
		} else {
			billCheckBo.setBillStatus("0"); // 正常
		}
		billCheckBo.setBillTitle(billCheckForm.getBillTitle());
		billCheckBo.setSerialNumber(billCheckForm.getSerialNumber());
		billCheckBo.setStartDate(startDate);
		billCheckBo.setEndDate(endDate);
		billCheckBo.setUserBy(userBy);
		billCheckBo.setCurr(curr);
		billCheckBo.setEnterpriseid(operEid);
		BillCheckVo billCheckVo = iBillCheckService.add(billCheckBo);

		if (billBad != null && billBad.size() > 0) {
			BillBadBo billBadBo = new BillBadBo();
			billBadBo.setUserBy(userBy);
			billBadBo.setCurr(curr);
			billBadBo.setEnterpriseid(operEid);
			billBadBo.setBillCheckId(billCheckVo.getBillCheckId());
			iBillBadService.addList(billBadBo, billBad);
		}
		return billCheckVo;
	}

	// 批量处理 账单信息 查询总额
	public BillCheckBo getBillDetails(List<OrderVo> orderVoList, List<String> tradenos) {
		/*
		 * 1 根据orderIds 查询账单信息
		 */
		// List<OrderVo> orderVoList = null;
		// if (orderIds != null) {
		// orderVoList = iOrderService.findByIds(orderIds);
		// }
		/*
		 * 2 根据账单流水号 tradeno 查询账单的拓展信息
		 */
		Map<String, OrderExtVo> orderMap = new HashMap<String, OrderExtVo>();
		Map<String, TradePayVo> tradeMap = new HashMap<String, TradePayVo>();
		if (tradenos != null) {
			orderMap = iOrderService.findOrderExtByTradenos(tradenos);
			// 交易流水
			tradeMap = iOrderService.getTradePayByTradenos(tradenos);
		}
		List<OrderExtVo> orderExtVoList = new ArrayList<OrderExtVo>();

		Map<String, Map> subOrderMap = iOrderService.findSubOrderCountByTradeNo(tradenos);
		// 循环处理数据
		for (OrderVo order : orderVoList) {
			OrderExtVo orderExtVo = new OrderExtVo();
			orderExtVo.setId(order.getId()); // 账单id
			orderExtVo.setOrder(order);// 账单信息
			OrderExtVo orderExt = orderMap.get(order.getTradeno());
			if (order.getPaystatus() == 1) { // 已缴
				// 水费组成明细
				List<LadderPriceDetailVo> payDetailList = JSONUtils.parseArray(orderExt.getChargeinfo(),
						LadderPriceDetailVo.class);
				// TODO 待优化
				if (payDetailList != null && payDetailList.size() > 0) {
					// iOrderService.findSubOrderCountByTradeNo(order.getTradeno()) == 0
					Map sub = subOrderMap.get(order.getTradeno());
					if (sub != null && Integer.parseInt(sub.get("count").toString()) == 0) {
						Map<String, LadderPriceDetailVo> payDetailMap = payDetailList.stream()
								.collect(Collectors.toMap(x -> x.getName(), x -> x));
						LadderPriceDetailVo lpd = payDetailMap.get("保底收费");
						if (lpd != null) {
							payDetailList.clear();
							payDetailList.add(lpd);
						}
					}
				}
				// 水费组成明细
				orderExtVo.setPayDetailList(payDetailList);
				// 收费种类详情
				PayTypeVo payType = JSONUtils.parseObject(orderExt.getPaytypeinfo(), PayTypeVo.class);
				if (payType == null) {
					throw new FrameworkRuntimeException(ResultCode.ParamIllegal, "不存在的收费种类,请先设置收费种类，再生成账单", null);
				}
				// 滞纳金 已缴费的有记录 未交费的已经在查询账单是算完
				orderExtVo.setPenalty(orderExt.getPenalty());
				// 实缴金额 = 减免后的金额 +滞纳金
				orderExtVo.setRealamount(
						CalUtil.add(order.getAmount(), orderExtVo.getPenalty() == null ? 0 : orderExtVo.getPenalty()));
				if (order.getAmount() == null) {
					orderExtVo.setOwnerpay(null);
				} else {
					orderExtVo.setOwnerpay(new BigDecimal(Double.toString(orderExtVo.getRealamount())));
				}
				// 优惠券信息
				CouponVo conpon = JSONUtils.parseObject(orderExt.getCouponinfo(), CouponVo.class);
				if (conpon != null) {
					List<CouponVo> couponList = new ArrayList<>();
					couponList.add(conpon);
					orderExtVo.setCouponList(couponList);
					orderExtVo.setCouponid(conpon.getCouponid());
				}
				// 余额抵扣
				orderExtVo.setBalance(order.getBalance());
				// 实收(个人支付)
				// orderExtVo.setOwnerpay(iOrderService.findOwnerPayByPayNo(order.getPayno()));
				// 减免类型
				ReduceVo reduce = JSONUtils.parseObject(orderExt.getReduceinfo(), ReduceVo.class);
				orderExtVo.setReduce(reduce);
				orderExtVo.setTradepay(tradeMap.get(orderExt.getTradeno()));
			}
			orderExtVoList.add(orderExtVo);
		}
		return calculation(orderExtVoList);
	}

	public BillCheckBo calculation(List<OrderExtVo> orderExtVoList) {
		BillCheckBo billCheckBo = new BillCheckBo();

		// 已缴-水费
		BigDecimal alloriginalPaid = new BigDecimal(0);
		// 未缴-水费
		BigDecimal alloriginalNotPaid = new BigDecimal(0);
		// 减免
		BigDecimal allreduce = new BigDecimal(0);
		// 已缴账单 - 逾期费
		BigDecimal allpenaltyPaid = new BigDecimal(0);
		// 未缴账单- 逾期费
		BigDecimal allpenaltyNotPaid = new BigDecimal(0);
		// 实收金额
		BigDecimal allActual = new BigDecimal(0);
		// 支付方式 1-现金支付 2-微信支付 3-支付宝支付 4-微信刷卡支付
		// 现金
		BigDecimal allCash = new BigDecimal(0);
		// 微信
		BigDecimal allWeChat = new BigDecimal(0);
		// 微信 刷卡
		BigDecimal allWeChatCard = new BigDecimal(0);
		// 支付宝 Alipay
		BigDecimal allAlipay = new BigDecimal(0);
		// 代金券 Voucher
		BigDecimal allVoucher = new BigDecimal(0);
		// 抵扣券 coupon
		BigDecimal allCoupon = new BigDecimal(0);
		// 未付账单_减免后金额
		BigDecimal amountNotPaid = new BigDecimal(0);
		// 已付账单_减免后金额
		BigDecimal amountPaid = new BigDecimal(0);

		// 未收
		BigDecimal Uncollected = new BigDecimal(0);

		for (OrderExtVo orderExtVo : orderExtVoList) {
			OrderVo orderVo = orderExtVo.getOrder();
			TradePayVo tradePayVo = orderExtVo.getTradepay();
			// 减免 = 所有减免之和
			if (orderExtVo.getReduce() != null) {
				allreduce = allreduce.add(new BigDecimal(orderExtVo.getReduce().getRvalue()));
			}
			// 已缴账单
			if (orderVo.getPaystatus() == 1) {
				if (orderExtVo.getPenalty() != null) {
					allpenaltyPaid = allpenaltyPaid.add(new BigDecimal(orderExtVo.getPenalty()));
				}

				// 水费 = 所有原始水费之和
				alloriginalPaid = alloriginalPaid.add(orderVo.getOriginal());
				amountPaid = amountPaid.add(new BigDecimal(orderVo.getAmount()));
				// 有交易记录
				if (tradePayVo != null) {
					BigDecimal actualamount = new BigDecimal(0);
					if (tradePayVo.getActualamount() != null) {
						// 实缴
						actualamount = new BigDecimal(tradePayVo.getActualamount());
					}
					// 判断支付类型
					if (tradePayVo.getPaytype() == 1) { // 现金实缴
						allCash = allCash.add(actualamount);
					} else if (tradePayVo.getPaytype() == 2) { // 微信实缴
						allWeChat = allWeChat.add(actualamount);
					} else if (tradePayVo.getPaytype() == 3) { // 支付宝
						allAlipay = allAlipay.add(actualamount);
					} else if (tradePayVo.getPaytype() == 4) { // 微信刷卡
						allWeChatCard = allWeChatCard.add(actualamount);
					}
				}

			} else {
				// 未缴账单
				if (orderVo.getPenalty() != null) {
					allpenaltyNotPaid = allpenaltyNotPaid.add(new BigDecimal(orderVo.getPenalty()));
				}
				// 水费 = 所有原始水费之和
				alloriginalNotPaid = alloriginalNotPaid.add(orderVo.getOriginal());
				amountNotPaid = amountNotPaid.add(new BigDecimal(orderVo.getAmount()));
			}

		}

		// 已缴-原始金额
		billCheckBo.setOriginalPaid(alloriginalPaid);
		billCheckBo.setOriginalNotPaid(alloriginalNotPaid);
		// 减免金额
		billCheckBo.setReduce(allreduce);
		// 已缴费 逾期金额
		billCheckBo.setPenaltyPaid(allpenaltyPaid);
		// 未缴费 逾期金额
		billCheckBo.setPenaltyNotPaid(allpenaltyNotPaid);
		// 现金
		billCheckBo.setCash(allCash);
		// 微信
		billCheckBo.setWechat(allWeChat);
		// 微信刷卡
		billCheckBo.setWechatCard(allWeChatCard);
		// 支付宝
		billCheckBo.setAlipay(allAlipay);
		// 代金券
		billCheckBo.setVoucher(allVoucher);
		// 抵扣券
		billCheckBo.setCoupon(allCoupon);
		billCheckBo.setAmountNotPaid(amountNotPaid);
		billCheckBo.setAmountPaid(amountPaid);
		return billCheckBo;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public String del(BillCheckForm billCheckForm) throws FrameworkRuntimeException {
		UserVo user = AuthCasClient.getUser();
		String operEid = user.getEnterpriseid();
		Date curr = new Date();
		String id = billCheckForm.getBillCheckId();
		BillCheckBo billCheckBo = new BillCheckBo();
		billCheckBo.setBillCheckId(id);
		billCheckBo.setEnterpriseid(operEid);
		billCheckBo.setCurr(curr);
		billCheckBo.setUserBy(user.getName());
		iBillCheckService.del(billCheckBo);
		return id;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public BillCheckVo sendCheck(BillCheckForm billCheckForm) throws FrameworkRuntimeException {
		UserVo user = AuthCasClient.getUser();
		String operEid = user.getEnterpriseid();
		Date curr = new Date();
		String id = billCheckForm.getBillCheckId();
		BillCheckBo billCheckBo = new BillCheckBo();
		billCheckBo.setBillCheckId(id);
		billCheckBo.setEnterpriseid(operEid);
		billCheckBo.setCurr(curr);
		billCheckBo.setUserBy(user.getName());
		BillBadBo billBadBo = new BillBadBo();
		BeanUtils.copyProperties(billCheckBo, billBadBo);
		// 校验工单中是否存在 标记为坏账的工单
		Boolean flag = iBillBadService.isExist(billBadBo);
		if (!flag) {
			throw new FrameworkRuntimeException(ResultCode.Fail, "没有标记为坏账的账单，不能发起审核");
		}
		return null;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public BillCheckVo closeCheck(BillCheckForm billCheckForm) throws FrameworkRuntimeException {
		UserVo user = AuthCasClient.getUser();
		String operEid = user.getEnterpriseid();
		Date curr = new Date();
		String id = billCheckForm.getBillCheckId();
		BillCheckBo billCheckBo = new BillCheckBo();
		billCheckBo.setBillCheckId(id);
		billCheckBo.setEnterpriseid(operEid);
		billCheckBo.setCurr(curr);
		billCheckBo.setUserBy(user.getName());
		BillBadBo billBadBo = new BillBadBo();
		BeanUtils.copyProperties(billCheckBo, billBadBo);
		// 校验工单中是否存在 标记为坏账的工单
		String processId = null;
		String processStatus = null;
		// TODO 结束工单信息
		processId = UuidUtils.getUuid();
		processStatus = "5"; // 工单结束

		billCheckBo.setProcessId(processId);
		billCheckBo.setProcessStatus(processStatus);
		// 发起工单
		// 更新对账账单信息
		iBillCheckService.edit(billCheckBo);
		return null;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public BillCheckVo editStatus(BillCheckForm billCheckForm) throws FrameworkRuntimeException {
		UserVo user = AuthCasClient.getUser();
		String operEid = user.getEnterpriseid();
		Date curr = new Date();
		BillCheckBo billCheckBo = new BillCheckBo();
		BeanUtils.copyProperties(billCheckForm, billCheckBo);
		billCheckBo.setEnterpriseid(operEid);
		billCheckBo.setCurr(curr);
		billCheckBo.setUserBy(user.getName());
		// TODO 更新工单流转状态
		// 更新对账账单信息
		iBillCheckService.editStatus(billCheckBo);
		return null;
	}

}
