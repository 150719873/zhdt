package com.dotop.smartwater.project.module.api.revenue.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.utils.BeanUtils;
import com.dotop.smartwater.project.module.api.revenue.IDiscountCouponFactory;
import com.dotop.smartwater.project.module.client.third.ICommunityService;
import com.dotop.smartwater.project.module.core.water.bo.ConditionBo;
import com.dotop.smartwater.project.module.core.water.bo.CouponBo;
import com.dotop.smartwater.project.module.core.water.bo.DiscountBo;
import com.dotop.smartwater.project.module.core.water.bo.LadderBo;
import com.dotop.smartwater.project.module.core.water.bo.OwnerBo;
import com.dotop.smartwater.project.module.core.water.form.customize.RechargeForm;
import com.dotop.smartwater.project.module.core.water.utils.BusinessUtil;
import com.dotop.smartwater.project.module.core.water.vo.CommunityVo;
import com.dotop.smartwater.project.module.core.water.vo.ConditionVo;
import com.dotop.smartwater.project.module.core.water.vo.DiscountVo;
import com.dotop.smartwater.project.module.core.water.vo.LadderVo;
import com.dotop.smartwater.project.module.core.water.vo.OwnerVo;
import com.dotop.smartwater.project.module.service.revenue.ICouponService;
import com.dotop.smartwater.project.module.service.revenue.IDiscountConditionService;
import com.dotop.smartwater.project.module.service.revenue.IDiscountService;
import com.dotop.smartwater.project.module.service.revenue.IOwnerService;
import com.dotop.smartwater.project.module.service.revenue.IPayTypeService;

@Component
public class DiscountCouponFactoryImpl implements IDiscountCouponFactory {

	private static final Logger logger = LogManager.getLogger(DiscountCouponFactoryImpl.class);

	@Autowired
	private ICouponService iCouponService;

	@Autowired
	private IDiscountService iDiscountService;

	@Autowired
	private IDiscountConditionService iDiscountConditionService;

	@Autowired
	private IOwnerService iOwnerService;

	@Autowired
	private ICommunityService iCommunityService;

	@Autowired
	private IPayTypeService iPayTypeService;

	@Override
	public void generateCoupon(RechargeForm rechargeForm) throws FrameworkRuntimeException {

		Date curr = new Date();

		String userno = rechargeForm.getUserno();
		Double amount = rechargeForm.getAmount();

		// 根据业主编号查询业主、水表信息
		OwnerBo ownerBo = new OwnerBo();
		ownerBo.setUserno(userno);
		OwnerVo ownerVo = iOwnerService.findByOwnerUserno(ownerBo);

		DiscountBo discountBo = new DiscountBo();
		discountBo.setEnterpriseid(ownerVo.getEnterpriseid());
		logger.debug("查询优惠活动开始：");
		DiscountVo discountVo = iDiscountService.getisDefaultDiscount(discountBo);
		logger.debug("查询优惠活动结束：" + discountVo);
		if (discountVo == null) {
			// lsc 修改 不抛出异常 异常会导致充值失败 没有活动就什么也不做
			// throw new FrameworkRuntimeException(ResultCode.Fail, "无优惠活动");
			return;
		}

		ConditionBo conditionBo = new ConditionBo();
		conditionBo.setDiscountid(discountVo.getId());
		logger.debug("查询优惠活动规则开始：");
		List<ConditionVo> conds = iDiscountConditionService.getcondtions(conditionBo);
		logger.debug("查询优惠活动规则结束：" + conds);
		if (conds == null || conds.size() < 1) {
			// lsc 修改 不抛出异常 异常会导致充值失败 没有活动就什么也不做 没有规则就没有优惠券
			return;
			// throw new FrameworkRuntimeException(ResultCode.Fail, "优惠活动下未设置规则");
		}

		for (int i = 0; i < conds.size(); i++) {
			ConditionVo cond = conds.get(i);

			if (i == 0 && amount < cond.getAmount()) { // 如果充值金额小于第一区间
				// lsc 修改 不抛出异常 不匹配就没有优惠券
				logger.debug("充值金额小于设定范围：" + amount);
				continue;
				// throw new FrameworkRuntimeException(ResultCode.Fail, "充值金额小于设定范围");
			}

			if ((i + 1) < (conds.size()) && amount > conds.get(i + 1).getAmount()) { // 如果下一个下标小于最后一个下标，且充值金额大于设定范围
				logger.debug("下一个下标小于最后一个下标，且充值金额大于设定范围");
				continue;
			} else if ((i + 1) == (conds.size() - 1) && amount >= conds.get(i).getAmount()) {// 如果充值金额大于等于最高限定区域，则按照最大的代金券计算
				logger.debug("充值金额大于等于最高限定区域，则按照最大的代金券计算");
			}

			/** 生成代金券 */
			CouponBo coupon = new CouponBo();
			coupon.setNo("DJ" + iCouponService.genNo());
			coupon.setEnterpriseid(ownerVo.getEnterpriseid());
			coupon.setName(discountVo.getName());
			coupon.setCommunityid(ownerVo.getCommunityid());
			logger.debug("查询区域名称开始：");
			CommunityVo communityVo = iCommunityService.findById(ownerVo.getCommunityid());
			logger.debug("查询区域名称结束：" + communityVo);
			if (communityVo != null) {
				coupon.setCommunityname(communityVo.getName());
			}

			coupon.setDiscountname(discountVo.getName());
			coupon.setType(1);
			coupon.setDiscountid(discountVo.getId());
			coupon.setStatus(0);
			coupon.setUserid(ownerVo.getOwnerid());
			coupon.setUserno(ownerVo.getUserno());
			coupon.setDelflag(0);
			coupon.setCreatetime(curr);
			coupon.setUsername(ownerVo.getUsername());
			coupon.setCreateuser(ownerVo.getOwnerid());
			coupon.setCreateusername(ownerVo.getUsername());

			/** 代金券有效时间 */
			coupon.setStarttime(curr);
			Calendar ca = Calendar.getInstance();
			ca.add(Calendar.DATE, discountVo.getValidityday());
			coupon.setEndtime(ca.getTime());

			if (cond.getMode() == 1) { // 元
				coupon.setFacevalue(cond.getValue());
			} else { // 吨
				List<LadderVo> ladders = iPayTypeService.getLadders(ownerVo.getPaytypeid());
				List<LadderBo> ladderBos = BeanUtils.copy(ladders, LadderBo.class);
				coupon.setFacevalue(BusinessUtil.getRealPayV2(ladderBos, cond.getValue()));
			}
			coupon.setUnit(1);
			logger.debug("添加优惠信息开始：" + coupon);
			iCouponService.addCoupon(coupon);
			logger.debug("添加优惠信息结束：");
			// 不明白旧源码要加break
			break;
		}

	}

}
