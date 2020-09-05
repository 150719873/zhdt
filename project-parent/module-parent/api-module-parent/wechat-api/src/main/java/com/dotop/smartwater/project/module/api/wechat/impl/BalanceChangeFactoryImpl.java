package com.dotop.smartwater.project.module.api.wechat.impl;

import com.dotop.smartwater.project.module.api.wechat.IBalanceChangeFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.project.module.core.auth.wechat.WechatAuthClient;
import com.dotop.smartwater.project.module.core.auth.wechat.WechatUser;
import com.dotop.smartwater.project.module.core.water.bo.PayDetailBo;
import com.dotop.smartwater.project.module.core.water.form.customize.WechatParamForm;
import com.dotop.smartwater.project.module.core.water.vo.PayDetailVo;
import com.dotop.smartwater.project.module.service.revenue.IPayDetailService;

/**
 * 迁移改造
 * 

 * @date 2019年3月22日
 */
@Component
public class BalanceChangeFactoryImpl implements IBalanceChangeFactory {


	@Autowired
	private IPayDetailService iPayDetailService;

	@Override
	public Pagination<PayDetailVo> page(WechatParamForm wechatParamForm)  {
		WechatUser wechatUser = WechatAuthClient.get();
		String ownerId = wechatUser.getOwnerid();
		String enterpriseid = wechatUser.getEnterpriseid();
		PayDetailBo payDetailBo = new PayDetailBo();
		payDetailBo.setEnterpriseid(enterpriseid);
		payDetailBo.setPage(wechatParamForm.getPage());
		payDetailBo.setPageCount(wechatParamForm.getPageCount());
		payDetailBo.setOwnerid(ownerId);
		return iPayDetailService.page(payDetailBo);
	}
}
