package com.dotop.smartwater.project.module.api.tool.impl;

import java.util.Date;

import com.dotop.smartwater.project.module.api.tool.IWechatPublicSettingFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.project.module.core.auth.local.AuthCasClient;
import com.dotop.smartwater.project.module.core.auth.vo.UserVo;
import com.dotop.smartwater.project.module.core.water.bo.WechatPublicSettingBo;
import com.dotop.smartwater.project.module.core.water.constants.ResultCode;
import com.dotop.smartwater.project.module.core.water.constants.WaterConstants;
import com.dotop.smartwater.project.module.core.water.form.WechatPublicSettingForm;
import com.dotop.smartwater.project.module.core.water.vo.customize.WechatPublicSettingVo;
import com.dotop.smartwater.project.module.service.tool.IWechatPublicSettingService;

/**
 * 短信配置
 * 

 * @date 2019年2月23日
 */

@Component
public class WechatPublicSettingFactoryImpl implements IWechatPublicSettingFactory {

	@Autowired
	private IWechatPublicSettingService iWechatPublicSettingService;

	@Override
	public Pagination<WechatPublicSettingVo> page(WechatPublicSettingForm wechatPublicSettingForm)
			 {
		UserVo user = AuthCasClient.getUser();
		String operEid = user.getEnterpriseid();
		WechatPublicSettingBo wechatPublicSettingBo = new WechatPublicSettingBo();
		BeanUtils.copyProperties(wechatPublicSettingForm, wechatPublicSettingBo);
		wechatPublicSettingBo.setEnterpriseid(operEid);
		return iWechatPublicSettingService.page(wechatPublicSettingBo);
	}

	@Override
	public WechatPublicSettingVo get(WechatPublicSettingForm wechatPublicSettingForm)  {
		WechatPublicSettingBo wechatPublicSettingBo = new WechatPublicSettingBo();
		wechatPublicSettingBo.setWechatpublicid(wechatPublicSettingForm.getWechatpublicid());
		return iWechatPublicSettingService.get(wechatPublicSettingBo);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public WechatPublicSettingVo add(WechatPublicSettingForm wechatPublicSettingForm)  {
		UserVo user = AuthCasClient.getUser();
		Date curr = new Date();
		String userBy = user.getName();
		// 复制属性
		WechatPublicSettingBo wechatPublicSettingBo = new WechatPublicSettingBo();
		BeanUtils.copyProperties(wechatPublicSettingForm, wechatPublicSettingBo);
		// 属性复制
		wechatPublicSettingBo.setUserBy(userBy);
		wechatPublicSettingBo.setCurr(curr);
		// 企业id是传参 不能setEnterpriseid
		// 添加之前 先校验是否存在
		boolean flag = iWechatPublicSettingService.isExist(wechatPublicSettingBo);
		if (flag) {
			throw new FrameworkRuntimeException(ResultCode.Fail, "微信配置的企业已经存在");
		} else {
			wechatPublicSettingBo.setCreatetime(new Date());
			wechatPublicSettingBo.setCreateuser(user.getUserid());
			wechatPublicSettingBo.setUpdatetime(new Date());
			wechatPublicSettingBo.setGatewayauthorizecode(WaterConstants.WECHAT_GATEWAYAUTHORIZECODE);
			wechatPublicSettingBo.setGatewayopenidbycode(WaterConstants.WECHAT_GATEWAYOPENIDBYCODE);
			wechatPublicSettingBo.setUnifiedorderurl(WaterConstants.WECHAT_UNIFIEDORDERURL);
			wechatPublicSettingBo.setOrderqueryurl(WaterConstants.WECHAT_ORDERQUERYURL);
			iWechatPublicSettingService.add(wechatPublicSettingBo);
		}
		return null;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public WechatPublicSettingVo edit(WechatPublicSettingForm wechatPublicSettingForm)
			 {
		UserVo user = AuthCasClient.getUser();
		Date curr = new Date();
		String userBy = user.getName();
		String operEid = user.getEnterpriseid();
		// 复制属性
		WechatPublicSettingBo wechatPublicSettingBo = new WechatPublicSettingBo();
		BeanUtils.copyProperties(wechatPublicSettingForm, wechatPublicSettingBo);
		wechatPublicSettingBo.setEnterpriseid(operEid);
		wechatPublicSettingBo.setUserBy(userBy);
		wechatPublicSettingBo.setCurr(curr);
		return iWechatPublicSettingService.edit(wechatPublicSettingBo);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public String del(WechatPublicSettingForm wechatPublicSettingForm)  {
		UserVo user = AuthCasClient.getUser();
		String operEid = user.getEnterpriseid();
		String id = wechatPublicSettingForm.getWechatpublicid();
		WechatPublicSettingBo wechatPublicSettingBo = new WechatPublicSettingBo();
		wechatPublicSettingBo.setWechatpublicid(id);
		wechatPublicSettingBo.setEnterpriseid(operEid);
		iWechatPublicSettingService.del(wechatPublicSettingBo);
		return id;
	}

}
