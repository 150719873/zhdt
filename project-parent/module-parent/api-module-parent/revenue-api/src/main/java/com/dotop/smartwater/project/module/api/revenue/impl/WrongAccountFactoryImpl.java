package com.dotop.smartwater.project.module.api.revenue.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.dotop.smartwater.project.module.api.revenue.IWrongAccountFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.dependence.core.utils.BeanUtils;
import com.dotop.smartwater.dependence.core.utils.JSONUtils;
import com.dotop.smartwater.project.module.core.auth.local.AuthCasClient;
import com.dotop.smartwater.project.module.core.auth.vo.AreaVo;
import com.dotop.smartwater.project.module.core.water.bo.CompriseBo;
import com.dotop.smartwater.project.module.core.water.bo.CouponBo;
import com.dotop.smartwater.project.module.core.water.bo.WrongAccountBo;
import com.dotop.smartwater.project.module.core.water.bo.customize.LadderPriceDetailBo;
import com.dotop.smartwater.project.module.core.water.constants.ResultCode;
import com.dotop.smartwater.project.module.core.water.constants.WaterConstants;
import com.dotop.smartwater.project.module.core.water.form.WrongAccountForm;
import com.dotop.smartwater.project.module.core.water.utils.BusinessUtil;
import com.dotop.smartwater.project.module.core.water.utils.CalUtil;
import com.dotop.smartwater.project.module.core.water.vo.CouponVo;
import com.dotop.smartwater.project.module.core.water.vo.OrderExtVo;
import com.dotop.smartwater.project.module.core.water.vo.OrderVo;
import com.dotop.smartwater.project.module.core.water.vo.PayTypeVo;
import com.dotop.smartwater.project.module.core.water.vo.PurposeVo;
import com.dotop.smartwater.project.module.core.water.vo.ReduceVo;
import com.dotop.smartwater.project.module.core.water.vo.TradePayVo;
import com.dotop.smartwater.project.module.core.water.vo.WrongAccountVo;
import com.dotop.smartwater.project.module.core.water.vo.customize.LadderPriceDetailVo;
import com.dotop.smartwater.project.module.service.revenue.ICouponService;
import com.dotop.smartwater.project.module.service.revenue.IOrderService;
import com.dotop.smartwater.project.module.service.revenue.IOwnerService;
import com.dotop.smartwater.project.module.service.revenue.IWrongAccountService;
import com.dotop.water.tool.service.BaseInf;

@Component
public class WrongAccountFactoryImpl implements IWrongAccountFactory {

	@Autowired
	private IWrongAccountService iWrongAccountService;

	@Autowired
	private IOrderService iOrderService;
	@Autowired
	private ICouponService iCouponService;
	@Autowired
	private IOwnerService iOwnerService;

	@Override
	public Pagination<WrongAccountVo> page(WrongAccountForm wrongAccountForm) throws FrameworkRuntimeException {
		// 获取用户信息
		String enterpriseid = AuthCasClient.getEnterpriseid();

		WrongAccountBo wrongAccountBo = new WrongAccountBo();
		BeanUtils.copy(wrongAccountForm, wrongAccountBo);
		wrongAccountBo.setEnterpriseid(enterpriseid);

		Pagination<WrongAccountVo> pagination = iWrongAccountService.page(wrongAccountBo);
		return pagination;
	}

	@Override
	public WrongAccountVo get(WrongAccountForm wrongAccountForm) throws FrameworkRuntimeException {
		// 获取用户信息
		String enterpriseid = AuthCasClient.getEnterpriseid();

		WrongAccountBo wrongAccountBo = new WrongAccountBo();
		BeanUtils.copy(wrongAccountForm, wrongAccountBo);
		wrongAccountBo.setEnterpriseid(enterpriseid);

		WrongAccountVo wrongAccountVo = iWrongAccountService.get(wrongAccountBo);
		if (wrongAccountVo != null) {
			// 订单扩展信息
			String orderid = wrongAccountVo.getOrderid();
			OrderVo order = iOrderService.findById(orderid);
			OrderExtVo orderExt = iOrderService.findOrderExtByTradeno(order.getTradeno());

			if (order != null && orderExt != null) {
				OrderExtVo orderExtVo = new OrderExtVo();
				orderExtVo.setOrder(order);
				List<LadderPriceDetailVo> payDetailList = JSON.parseArray(orderExt.getChargeinfo(),
						LadderPriceDetailVo.class);
				orderExtVo.setPayDetailList(payDetailList);
				// 滞纳金
				PayTypeVo payType = JSONUtils.parseObject(orderExt.getPaytypeinfo(), PayTypeVo.class);
				List<LadderPriceDetailBo> payDetailListBo = BeanUtils.copy(payDetailList, LadderPriceDetailBo.class);
				List<CompriseBo> compriseBos = BeanUtils.copy(payType.getComprises(), CompriseBo.class);

				orderExtVo.setPenalty(BusinessUtil.getPenalty(payDetailListBo, compriseBos, order.getGeneratetime(),
						payType.getOverdueday()));
				orderExtVo.setRealamount(CalUtil.add(order.getAmount(), orderExtVo.getPenalty()));

				if (order.getAmount() == null) {
					orderExtVo.setOwnerpay(null);
				} else {
					orderExtVo.setOwnerpay(new BigDecimal(Double.toString(orderExtVo.getRealamount())));
				}

				CouponVo conpon = JSONUtils.parseObject(orderExt.getCouponinfo(), CouponVo.class);
				if (conpon != null) {
					List<CouponVo> couponList = new ArrayList<>();
					couponList.add(conpon);
					orderExtVo.setCouponList(couponList);
					orderExtVo.setCouponid(conpon.getCouponid());
				}
				orderExtVo.setBalance(order.getBalance());
				// 实收
				orderExtVo.setOwnerpay(iOrderService.findOwnerPayByPayNo(order.getPayno()));
				// 滞纳金
				orderExtVo.setPenalty(orderExt.getPenalty());
				// 水表用途
				PurposeVo purpose = JSONUtils.parseObject(orderExt.getPurposeinfo(), PurposeVo.class);
				orderExtVo.setPurpose(purpose);
				// 减免类型
				ReduceVo reduce = JSONUtils.parseObject(orderExt.getReduceinfo(), ReduceVo.class);
				orderExtVo.setReduce(reduce);
				// 交易流水
				TradePayVo trade = iOrderService.getTradePay(order.getTradeno());
				orderExtVo.setTradepay(trade);
				wrongAccountVo.setOrderExt(orderExtVo);
			}
		}
		return wrongAccountVo;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public WrongAccountVo add(WrongAccountForm wrongAccountForm) throws FrameworkRuntimeException {
		// 获取用户信息
		String enterpriseid = AuthCasClient.getEnterpriseid();
		String userid = AuthCasClient.getUserid();
		Date curr = AuthCasClient.getCurr();

		// 验证订单是否存在
		OrderVo order = iOrderService.findById(wrongAccountForm.getOrderid());
		if (order == null) {
			throw new RuntimeException("该账单不存在,请稍后再试");
		}
		// Date date = wrongAccountForm.getCreatetime();

		// 提出ishandle 的方法体 取消原项目中的方法
		List<Integer> statuss = new ArrayList<Integer>();
		statuss.add(WaterConstants.WRONG_ACCOUNT_STATUS_CHULI);
		statuss.add(WaterConstants.WRONG_ACCOUNT_STATUS_SHENQING);
		WrongAccountBo wrongAccountBo = new WrongAccountBo();
		wrongAccountBo.setOrderid(wrongAccountForm.getOrderid());
		wrongAccountBo.setStatuss(statuss);
		// 验证orderid 是否存在未处理的错账
		if (iWrongAccountService.isExist(wrongAccountBo)) {
			throw new FrameworkRuntimeException(ResultCode.Fail, "账单已申请错账");
		}
		// 错账单号
		wrongAccountBo = new WrongAccountBo();
		BeanUtils.copy(wrongAccountForm, wrongAccountBo);
		wrongAccountBo.setEnterpriseid(enterpriseid);
		wrongAccountBo.setCreatetime(curr);
		wrongAccountBo.setCreateuser(userid);
		String wrongno = genNo(curr);
		wrongAccountBo.setCommunityid(order.getCommunityid()); // 小区id
		wrongAccountBo.setOwnerid(order.getOwnerid()); // 业主id
		wrongAccountBo.setWrongno(wrongno); // 错账单号
		// TODO STATUS_SHENQING 暂时没有
		wrongAccountBo.setStatus(WaterConstants.WRONG_ACCOUNT_STATUS_SHENQING); // 已申请
		wrongAccountBo.setApplytime(curr);
		return iWrongAccountService.add(wrongAccountBo);
	}

	// 生成错账单号
	private final String genNo(Date currentTime) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
		String dateString = formatter.format(currentTime);
		int x = (int) (Math.random() * 900) + 100;
		String noStr = "WA" + dateString + x;
		WrongAccountBo wrongAccountBo = new WrongAccountBo();
		wrongAccountBo.setWrongno(noStr);
		Boolean flag = iWrongAccountService.isExist(wrongAccountBo);
		if (flag) {
			return genNo(currentTime);
		}
		return noStr;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public WrongAccountVo update(WrongAccountForm wrongAccountForm) throws FrameworkRuntimeException {
		// 获取用户信息
		String enterpriseid = AuthCasClient.getEnterpriseid();
		String userid = AuthCasClient.getUserid();
		String name = AuthCasClient.getName();
		Date curr = AuthCasClient.getCurr();

		WrongAccountBo wrongAccountBo = new WrongAccountBo();
		BeanUtils.copy(wrongAccountForm, wrongAccountBo);
		wrongAccountBo.setEnterpriseid(enterpriseid);
		wrongAccountBo.setHandleid(userid);
		wrongAccountBo.setHandletime(curr);
		wrongAccountBo.setHandlename(name);
		// TODO STATUS_SHENQING 暂时没有
		wrongAccountBo.setStatus(WaterConstants.WRONG_ACCOUNT_STATUS_CHULI); // 处理中
		iWrongAccountService.update(wrongAccountBo);
		return null;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public void complete(WrongAccountForm wrongAccountForm) throws FrameworkRuntimeException {
		// 获取用户信息
		String enterpriseid = AuthCasClient.getEnterpriseid();
		String userid = AuthCasClient.getUserid();
		String name = AuthCasClient.getName();
		Date curr = AuthCasClient.getCurr();

		WrongAccountBo wrongAccountBo = new WrongAccountBo();
		BeanUtils.copy(wrongAccountForm, wrongAccountBo);
		wrongAccountBo.setEnterpriseid(enterpriseid);
		wrongAccountBo.setHandleid(userid);
		wrongAccountBo.setHandletime(curr);
		wrongAccountBo.setHandlename(name);
		// TODO STATUS_SHENQING 暂时没有
		wrongAccountBo.setStatus(WaterConstants.WRONG_ACCOUNT_STATUS_JIEJUE); // 已解决
		iWrongAccountService.complete(wrongAccountBo);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public void cancel(WrongAccountForm wrongAccountForm) throws FrameworkRuntimeException {
		// 获取用户信息
		String enterpriseid = AuthCasClient.getEnterpriseid();
		String userid = AuthCasClient.getUserid();
		String name = AuthCasClient.getName();
		Date curr = AuthCasClient.getCurr();

		WrongAccountBo wrongAccountBo = new WrongAccountBo();
		BeanUtils.copy(wrongAccountForm, wrongAccountBo);
		wrongAccountBo.setEnterpriseid(enterpriseid);
		wrongAccountBo.setHandleid(userid);
		wrongAccountBo.setHandletime(curr);
		wrongAccountBo.setHandlename(name);
		// TODO STATUS_SHENQING 暂时没有
		wrongAccountBo.setStatus(WaterConstants.WRONG_ACCOUNT_STATUS_CHEXIAO); // 已撤销
		iWrongAccountService.cancel(wrongAccountBo);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public void addCoupon(WrongAccountForm wrongAccountForm) throws FrameworkRuntimeException {
		// 获取用户信息
		String enterpriseid = AuthCasClient.getEnterpriseid();
		String userid = AuthCasClient.getUserid();
		String name = AuthCasClient.getName();
		Date curr = AuthCasClient.getCurr();

		WrongAccountBo wrongAccountBo = new WrongAccountBo();
		BeanUtils.copy(wrongAccountForm, wrongAccountBo);
		wrongAccountBo.setEnterpriseid(enterpriseid);
		wrongAccountBo.setHandleid(userid);
		wrongAccountBo.setHandletime(curr);
		wrongAccountBo.setHandlename(name);
		WrongAccountVo wrongAccountVo = iWrongAccountService.get(wrongAccountBo);
		if (wrongAccountVo == null) {
			throw new FrameworkRuntimeException(ResultCode.Fail, "该账单错账不存在,请稍后再试");
		}

		// OwnerVo ownerVo = new OwnerVo();
		// OwnerBo ownerBo = new OwnerBo();
		// ownerBo.setEnterpriseid(enterpriseid);
		// ownerBo.setOwnerid(wrongAccountVo.getOwnerid());
		// ownerBo.setUserno(wrongAccountVo.getUserno());
		// if (wrongAccountVo.getOwnerid() != null) {
		// ownerVo = iOwnerService.findByOwnerId(ownerBo);
		// } else {
		// ownerVo = iOwnerService.findByOwnerUserno(ownerBo);
		// }

		CouponBo couponBo = new CouponBo();
		couponBo.setUserid(wrongAccountVo.getOwnerid());
		couponBo.setCommunityid(wrongAccountVo.getCommunityid());
		couponBo.setName(wrongAccountForm.getCouponname());
		couponBo.setUserno(wrongAccountVo.getUserno());
		couponBo.setEnterpriseid(enterpriseid);
		// 抵扣券
		couponBo.setType(WaterConstants.COUPON_TYPE_DKQ);
		couponBo.setStarttime(wrongAccountForm.getCouponstarttime());
		couponBo.setEndtime(wrongAccountForm.getCouponendtime());
		couponBo.setFacevalue(wrongAccountForm.getFacevalue().doubleValue());
		couponBo.setUnit(wrongAccountForm.getCouponunit());
		couponBo.setCreateuser(userid);
		couponBo.setUpdateuser(userid);
		couponBo.setCreateuser(userid);
		couponBo.setCreateusername(name);
		couponBo.setCreatetime(curr);
		couponBo.setUpdateuser(userid);
		couponBo.setUpdateusername(name);
		couponBo.setUpdatetime(curr);
		couponBo.setDelflag(0);
		AreaVo area = BaseInf.getAreaById(wrongAccountVo.getCommunityid());
		if (area != null) {
			couponBo.setCommunityname(area.getName());
		}
		couponBo.setUsername(name);

		if (couponBo.getFacevalue() <= 0) {
			throw new RuntimeException("面值不能小于等于0");
		}
		if (couponBo.getEndtime().before(new Date())) {
			throw new RuntimeException("结束日期必须大于今天");
		}
		// 生成优惠券编号
		if (couponBo.getType() == 1) {
			couponBo.setNo("DJ" + iCouponService.genNo());
		} else {
			couponBo.setNo("DK" + iCouponService.genNo());
		}
		if (couponBo.getStarttime().before(new Date())) {
			couponBo.setStatus(WaterConstants.COUPON_STATUS_NORMAL);
		} else {
			couponBo.setStatus(WaterConstants.COUPON_STATUS_UNAVAILABLE);
		}
		// 新增代金抵扣券
		CouponVo couponVo = iCouponService.addCoupon(couponBo);

		// 更新错账代金抵扣券
		wrongAccountBo.setCouponid(couponVo.getCouponid());
		iWrongAccountService.updateCoupon(wrongAccountBo);

		// iWrongAccountService.addCoupon(wrongAccountBo);
	}
}
