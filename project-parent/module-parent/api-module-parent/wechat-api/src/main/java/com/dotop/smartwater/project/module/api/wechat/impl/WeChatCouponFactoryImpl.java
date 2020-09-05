package com.dotop.smartwater.project.module.api.wechat.impl;

import com.dotop.smartwater.project.module.api.wechat.IWechatCouponFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.project.module.core.auth.wechat.WechatAuthClient;
import com.dotop.smartwater.project.module.core.auth.wechat.WechatUser;
import com.dotop.smartwater.project.module.core.water.bo.CouponBo;
import com.dotop.smartwater.project.module.core.water.form.customize.WechatParamForm;
import com.dotop.smartwater.project.module.core.water.vo.CouponVo;
import com.dotop.smartwater.project.module.service.revenue.ICouponService;

/**
 * 迁移改造
 * 

 * @date 2019年3月22日
 */
@Component
public class WeChatCouponFactoryImpl implements IWechatCouponFactory {

	@Autowired
	private ICouponService iCouponService;

	@Override
	public Pagination<CouponVo> page(WechatParamForm wechatParamForm) {
		WechatUser wechatUser = WechatAuthClient.get();
		String ownerId = wechatUser.getOwnerid();
		String enterpriseid = wechatUser.getEnterpriseid();
		CouponBo couponBo = new CouponBo();
		couponBo.setUserid(ownerId);
		couponBo.setPage(wechatParamForm.getPage());
		couponBo.setPageCount(wechatParamForm.getPageCount());
		couponBo.setEnterpriseid(enterpriseid);
		return iCouponService.getCouponList(couponBo);
	}
}
