package com.dotop.smartwater.project.module.api.revenue.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.dotop.smartwater.project.module.api.revenue.ICouponFactory;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.dependence.core.utils.BeanUtils;
import com.dotop.smartwater.dependence.core.utils.DateUtils;
import com.dotop.smartwater.project.module.client.third.ICommunityService;
import com.dotop.smartwater.project.module.core.auth.local.AuthCasClient;
import com.dotop.smartwater.project.module.core.auth.vo.AreaNodeVo;
import com.dotop.smartwater.project.module.core.auth.vo.UserVo;
import com.dotop.smartwater.project.module.core.water.bo.CouponBo;
import com.dotop.smartwater.project.module.core.water.bo.OwnerBo;
import com.dotop.smartwater.project.module.core.water.constants.ResultCode;
import com.dotop.smartwater.project.module.core.water.constants.WaterConstants;
import com.dotop.smartwater.project.module.core.water.form.CouponForm;
import com.dotop.smartwater.project.module.core.water.vo.CommunityVo;
import com.dotop.smartwater.project.module.core.water.vo.CouponVo;
import com.dotop.smartwater.project.module.core.water.vo.DiscountVo;
import com.dotop.smartwater.project.module.core.water.vo.OwnerVo;
import com.dotop.smartwater.project.module.service.revenue.ICouponService;
import com.dotop.smartwater.project.module.service.revenue.IDiscountService;
import com.dotop.smartwater.project.module.service.revenue.IOwnerService;
import com.dotop.water.tool.service.BaseInf;

@Component
public class CouponFactoryImpl implements ICouponFactory {

	private final static Logger logger = LogManager.getLogger(CouponFactoryImpl.class);

	@Autowired
	private ICouponService iCouponService;

	@Autowired
	private IDiscountService iDiscountService;

	@Autowired
	private IOwnerService iOwnerService;

	@Autowired
	private ICommunityService iCommunityService;

	@Override
	public Pagination<CouponVo> getCouponList(CouponForm couponForm) throws FrameworkRuntimeException {
		// 获取用户信息
		String enterpriseid = AuthCasClient.getEnterpriseid();

		CouponBo couponBo = BeanUtils.copy(couponForm, CouponBo.class);
		couponBo.setEnterpriseid(enterpriseid);

		Pagination<CouponVo> pagination = iCouponService.getCouponList(couponBo);
		return pagination;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public CouponVo add(CouponForm couponForm) throws FrameworkRuntimeException {
		// 获取用户信息
		UserVo user = AuthCasClient.getUser();
		String userid = user.getUserid();
		String ticket = user.getTicket();
		String name = user.getName();
		Date curr = new Date();
		// 参数
		Integer addType = couponForm.getAddType();
		CouponBo couponBo = BeanUtils.copy(couponForm, CouponBo.class);
		// 校验
		Map<String, AreaNodeVo> map = BaseInf.getAreaMaps(userid, ticket);
		if (map == null || map.size() == 0) {
			throw new FrameworkRuntimeException(ResultCode.ParamIllegal, "没有为当前用户分配区域，请先分配区域");
		}

		if (addType == 1) {// 区域
			String[] communityids = couponForm.getCommunityids().split(",");
			for (String communityid : communityids) {
				if (StringUtils.isNotBlank(communityid)) {
					List<OwnerVo> owners = iOwnerService.findByCommunity(communityid);
					if (owners == null || owners.size() == 0) {
						throw new FrameworkRuntimeException(ResultCode.Fail, "所属区域没有业主");
					}
					for (OwnerVo owner : owners) {
						AreaNodeVo areaNode = map.get(owner.getCommunityid());
						if (areaNode != null) {
							couponBo.setCommunityname(areaNode.getTitle());
						}
						couponBo.setCommunityid(communityid);
						couponBo.setUserid(owner.getOwnerid());
						couponBo.setUserno(owner.getUserno());
						couponBo.setUsername(owner.getUsername());
						couponBo.setEnterpriseid(owner.getEnterpriseid());
						addCoupon(couponBo, userid, name, curr);
						areaNode = null;
					}
				}
			}
		} else {// 多用户
			String[] usernos = couponForm.getUsernos().split(",");
			for (String userno : usernos) {
				if (StringUtils.isNotBlank(userno)) {
					OwnerBo ownerBo = new OwnerBo();
					ownerBo.setUserno(userno);
					OwnerVo owner = iOwnerService.findByOwnerUserno(ownerBo);
					if (owner != null) {
						AreaNodeVo areaNode = map.get(owner.getCommunityid());
						if (areaNode != null) {
							couponBo.setCommunityname(areaNode.getTitle());
						}
						couponBo.setCommunityid(owner.getCommunityid());
						couponBo.setUserid(owner.getOwnerid());
						couponBo.setUserno(owner.getUserno());
						couponBo.setUsername(owner.getUsername());
						couponBo.setEnterpriseid(owner.getEnterpriseid());
						addCoupon(couponBo, userid, name, curr);
						areaNode = null;
					}
				}
			}
		}
		return null;
	}

	private void addCoupon(CouponBo couponBo, String userid, String userBy, Date curr)
			throws FrameworkRuntimeException {
		// 判断业主是否有效
		OwnerBo ownerBo = new OwnerBo();
		OwnerVo ownerVo;
		if (StringUtils.isNotBlank(couponBo.getUserid())) {
			// couponBo.getUserid()
			ownerBo.setOwnerid(couponBo.getUserid());
			ownerVo = iOwnerService.findByOwnerId(ownerBo);
		} else {
			// couponBo.getUserno()
			ownerBo.setUserno(couponBo.getUserno());
			ownerVo = iOwnerService.findByOwnerUserno(ownerBo);
		}
		if (ownerVo == null) {
			throw new FrameworkRuntimeException(ResultCode.Fail, "业主不存在");
		}
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

		couponBo.setUserid(ownerVo.getOwnerid());
		couponBo.setUserno(ownerVo.getUserno());
		couponBo.setUsername(ownerVo.getUsername());
		couponBo.setEnterpriseid(ownerVo.getEnterpriseid());
		// 优惠活动填入，则记录
		if (StringUtils.isNotBlank(couponBo.getDiscountid())) {
			DiscountVo discount = iDiscountService.getDiscountById(couponBo.getDiscountid());
			couponBo.setDiscountname(discount.getName());
		}
		couponBo.setCreateuser(userid);
		couponBo.setCreateusername(userBy);
		couponBo.setCreatetime(curr);
		couponBo.setUpdateuser(userid);
		couponBo.setUpdateusername(userBy);
		couponBo.setUpdatetime(curr);
		couponBo.setDelflag(0);
		iCouponService.addCoupon(couponBo);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public void batchAdd(CouponForm couponForm) throws FrameworkRuntimeException {
		// 获取用户信息
		UserVo user = AuthCasClient.getUser();
		String userid = user.getUserid();
		String userBy = user.getAccount();
		Date curr = new Date();

		// 检查优惠活动是否存在
		DiscountVo discount = iDiscountService.getDiscountById(couponForm.getDiscountid());
		if (discount == null) {
			throw new FrameworkRuntimeException(ResultCode.Fail, "优惠活动不存在");
		}
		// 获取社区名称
		CommunityVo community = iCommunityService.findById(couponForm.getCommunityid());
		if (community == null) {
			throw new FrameworkRuntimeException(ResultCode.Fail, "社区不存在");
		}
		// 参数
		Date starttime = couponForm.getStarttime();
		String communityid = couponForm.getCommunityid();
		Integer type = couponForm.getType();
		CouponBo couponBo = BeanUtils.copy(couponForm, CouponBo.class);

		if (!DateUtils.compare(starttime, curr)) {
			couponBo.setStatus(0);
		} else {
			couponBo.setStatus(1);
		}
		// 获取社区下的所有用户
		List<OwnerVo> owners = iOwnerService.findByCommunity(communityid);
		for (OwnerVo owner : owners) {
			// 生成优惠券编号
			if (type == 1) {
				couponBo.setNo("DJ" + iCouponService.genNo());
			} else {
				couponBo.setNo("DK" + iCouponService.genNo());
			}
			couponBo.setCommunityname(community.getName());
			couponBo.setUserid(owner.getOwnerid());
			couponBo.setUserno(owner.getUserno());
			couponBo.setUsername(owner.getUsername());
			couponBo.setEnterpriseid(owner.getEnterpriseid());
			couponBo.setDiscountname(discount.getName());
			couponBo.setCreateuser(userid);
			couponBo.setCreateusername(userBy);
			couponBo.setCreatetime(curr);
			couponBo.setUpdateuser(userid);
			couponBo.setUpdateusername(userBy);
			couponBo.setUpdatetime(curr);
			couponBo.setDelflag(0);
			// 插入数据库
			iCouponService.addCoupon(couponBo);
		}

	}

	@Override
	public CouponVo getCoupon(CouponForm couponForm) throws FrameworkRuntimeException {
		// 获取用户信息
		UserVo user = AuthCasClient.getUser();
		String enterpriseid = user.getEnterpriseid();

		CouponBo couponBo = BeanUtils.copy(couponForm, CouponBo.class);
		couponBo.setEnterpriseid(enterpriseid);

		CouponVo coupon = iCouponService.getCoupon(couponBo);
		return coupon;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public void delete(CouponForm couponForm) throws FrameworkRuntimeException {
		// 获取用户信息
		UserVo user = AuthCasClient.getUser();
		String userid = user.getUserid();
		String enterpriseid = user.getEnterpriseid();
		String userBy = user.getAccount();
		Date curr = new Date();

		CouponBo couponBo = BeanUtils.copy(couponForm, CouponBo.class);

		couponBo.setEnterpriseid(enterpriseid);
		couponBo.setUpdateuser(userid);
		couponBo.setUpdateusername(userBy);
		couponBo.setUpdatetime(curr);
		couponBo.setDelflag(1);

		iCouponService.delete(couponBo);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public void disable(CouponForm couponForm) throws FrameworkRuntimeException {
		// 获取用户信息
		UserVo user = AuthCasClient.getUser();
		String userid = user.getUserid();
		String enterpriseid = user.getEnterpriseid();
		String userBy = user.getAccount();
		Date curr = new Date();

		CouponBo couponBo = BeanUtils.copy(couponForm, CouponBo.class);

		couponBo.setEnterpriseid(enterpriseid);
		CouponVo couponVo = iCouponService.getCoupon(couponBo);
		if (couponVo == null) {
			throw new FrameworkRuntimeException(ResultCode.Fail, "优惠券不存在,请刷新");
		}
		couponBo.setUpdateuser(userid);
		couponBo.setUpdateusername(userBy);
		couponBo.setUpdatetime(curr);
		couponBo.setStatus(WaterConstants.COUPON_STATUS_DISABLE);

		iCouponService.disable(couponBo);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public void checkTask() throws FrameworkRuntimeException {
		Date curr = new Date();
		// 获取处于有效和未启用状态的优惠券 condition = " and (a.status=0 or a.status=1)";
		CouponBo couponBo = new CouponBo();
		List<Integer> statuss = new ArrayList<>();
		statuss.add(0);
		statuss.add(1);
		couponBo.setStatuss(statuss);
		List<CouponVo> coupons = iCouponService.list(couponBo);
		// 启用到时的优惠券
		for (CouponVo coupon : coupons) {
			if (coupon.getStatus() == WaterConstants.COUPON_STATUS_UNAVAILABLE && coupon.getStarttime().before(curr)) {
				coupon.setStatus(WaterConstants.COUPON_STATUS_NORMAL);
				logger.info(coupon.getCouponid() + "");
				couponBo = BeanUtils.copy(coupon, CouponBo.class);
				int num = iCouponService.editStatus(couponBo);
				if (num > 0) {
					logger.info("启用优惠券，编号=" + coupon.getNo());
				}
			}
		}
		// 失效结束的优惠券
		for (CouponVo coupon : coupons) {
			if (coupon.getStatus() == WaterConstants.COUPON_STATUS_NORMAL
					&& DateUtils.day(coupon.getEndtime(), 1).before(curr)) {
				coupon.setStatus(WaterConstants.COUPON_STATUS_EXPIRE);
				logger.info(coupon.getCouponid() + "");
				couponBo = BeanUtils.copy(coupon, CouponBo.class);
				int num = iCouponService.editStatus(couponBo);
				if (num > 0) {
					logger.info("过期优惠券，编号=" + coupon.getNo());
				}
			}
		}
	}
}
