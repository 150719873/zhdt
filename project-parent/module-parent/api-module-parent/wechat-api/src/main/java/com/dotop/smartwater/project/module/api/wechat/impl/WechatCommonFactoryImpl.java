package com.dotop.smartwater.project.module.api.wechat.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.dotop.smartwater.project.module.api.wechat.IWechatCommonFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.utils.UuidUtils;
import com.dotop.smartwater.project.module.api.revenue.IDiscountCouponFactory;
import com.dotop.smartwater.project.module.core.third.bo.wechat.WechatOrderBo;
import com.dotop.smartwater.project.module.core.water.bo.OwnerBo;
import com.dotop.smartwater.project.module.core.water.bo.PayDetailBo;
import com.dotop.smartwater.project.module.core.water.form.customize.RechargeForm;
import com.dotop.smartwater.project.module.core.water.vo.OwnerVo;
import com.dotop.smartwater.project.module.service.revenue.IOwnerService;
import com.dotop.smartwater.project.module.service.revenue.IPayDetailService;
import com.dotop.smartwater.project.module.service.wechat.IWechatRechargeService;

/**
 * 迁移改造
 * 

 * @date 2019年3月22日
 */
@Component
public class WechatCommonFactoryImpl implements IWechatCommonFactory {

	private static final Logger logger = LogManager.getLogger(WechatCommonFactoryImpl.class);

	@Autowired
	private IWechatRechargeService iWechatRechargeService;

	@Autowired
	private IOwnerService iOwnerService;

	@Autowired
	private IPayDetailService iPayDetailService;

	@Autowired
	private IDiscountCouponFactory iDiscountCouponFactory;

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public int updateOrderRecord(WechatOrderBo wechatOrder, String ownerid) throws FrameworkRuntimeException {
		int insertRechargeRecord = iWechatRechargeService.updateRechargeRecord(wechatOrder);
		if (insertRechargeRecord == 1) {
			logger.debug("微信充值记录表新增成功" + wechatOrder.getWechatmchno());
			OwnerBo ownerBo = new OwnerBo();
			ownerBo.setOwnerid(ownerid);
			OwnerVo rechargeowner = iOwnerService.findByOwnerDetail(ownerBo);
			// 被充值的业主信息
			OwnerBo ownerBo1 = new OwnerBo();
			ownerBo1.setOwnerid(wechatOrder.getOwnerid());
			OwnerVo owner = iOwnerService.findByOwnerDetail(ownerBo1);
			// 添加充值记录
			PayDetailBo payDetail = new PayDetailBo();
			payDetail.setBeforemoney(new BigDecimal(owner.getAlreadypay()));
			payDetail.setAftermoney(
					new BigDecimal(owner.getAlreadypay().doubleValue() + wechatOrder.getWechatamount().doubleValue()));
			payDetail.setCreatetime(new Date());
			payDetail.setCreateuser(ownerid);
			payDetail.setOwnerid(owner.getOwnerid());
			payDetail.setOwnername(owner.getUsername());
			payDetail.setOwnerno(owner.getUserno());
			payDetail.setPayno(wechatOrder.getWechatmchno());
			payDetail.setRemark("业主:" + rechargeowner.getUsername() + "微信充值");
			payDetail.setType(1);
			payDetail.setPaymoney(new BigDecimal(wechatOrder.getWechatamount()));
			payDetail.setUsername(rechargeowner.getUsername());

			payDetail.setId(UuidUtils.getUuid());
			if (iPayDetailService.addPayDetail(payDetail) == 1) {
				logger.debug("新增payDtail的记录成功" + payDetail.getPayno());
			}

			// 请求获取抵扣卷
			logger.debug("***********开始请求获取抵扣卷*********");
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("userno", owner.getUserno());
			map.put("amount", wechatOrder.getWechatamount());
			map.put("enterpriseid", owner.getEnterpriseid());

			RechargeForm rechargeForm1 = new RechargeForm();
			rechargeForm1.setAmount(wechatOrder.getWechatamount());
			rechargeForm1.setUserno(owner.getUserno());
			rechargeForm1.setEnterpriseid(owner.getEnterpriseid());
			logger.debug("生成代金券开始：rechargeForm1");
			iDiscountCouponFactory.generateCoupon(rechargeForm1);
			logger.debug("生成代金券结束：" + wechatOrder.getId());
			logger.debug("更新充值记录wechatOrder--->成功：" + wechatOrder.getId());
			// 更新业主的剩余金额
			owner.setAlreadypay(payDetail.getAftermoney().doubleValue());

			OwnerBo ownerBo2 = new OwnerBo();
			BeanUtils.copyProperties(owner, ownerBo2);
			if (iOwnerService.updateStatusAndAlreadypay(ownerBo2) == 1) {
				logger.debug("更新业主" + owner.getOwnerid() + "的剩余金额 成功");
			}
		}
		return insertRechargeRecord;
	}

}
