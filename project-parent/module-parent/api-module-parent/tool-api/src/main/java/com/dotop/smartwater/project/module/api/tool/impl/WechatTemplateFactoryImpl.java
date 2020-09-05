package com.dotop.smartwater.project.module.api.tool.impl;

import java.util.Date;

import com.dotop.smartwater.project.module.api.tool.IWechatTemplateFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.project.module.core.auth.enums.SmsEnum;
import com.dotop.smartwater.project.module.core.auth.local.AuthCasClient;
import com.dotop.smartwater.project.module.core.auth.local.IAuthCasClient;
import com.dotop.smartwater.project.module.core.auth.vo.UserVo;
import com.dotop.smartwater.project.module.core.water.bo.customize.WechatTemplateBo;
import com.dotop.smartwater.project.module.core.water.constants.ResultCode;
import com.dotop.smartwater.project.module.core.water.constants.WaterConstants;
import com.dotop.smartwater.project.module.core.water.form.customize.WechatTemplateForm;
import com.dotop.smartwater.project.module.core.water.vo.WechatTemplateVo;
import com.dotop.smartwater.project.module.service.tool.IWechatTemplateService;

/**
 * 短信配置
 * 

 * @date 2019年2月23日
 */

@Component
public class WechatTemplateFactoryImpl implements IWechatTemplateFactory, IAuthCasClient{

	@Autowired
	private IWechatTemplateService iWechatTemplateService;

	@Override
	public Pagination<WechatTemplateVo> page(WechatTemplateForm wechatTemplateForm)  {
		WechatTemplateBo wechatTemplateBo = new WechatTemplateBo();
		BeanUtils.copyProperties(wechatTemplateForm, wechatTemplateBo);
		if (getUsertype() == WaterConstants.USER_TYPE_ADMIN) {
			// 最高管理员admin
		} else {
			wechatTemplateBo.setEnterpriseid(getEnterpriseid());
		}
		return iWechatTemplateService.page(wechatTemplateBo);
	}

	@Override
	public WechatTemplateVo get(WechatTemplateForm wechatTemplateForm)  {
		WechatTemplateBo wechatTemplateBo = new WechatTemplateBo();
		wechatTemplateBo.setId(wechatTemplateForm.getId());
		if (getUsertype() == WaterConstants.USER_TYPE_ADMIN) {
			// 最高管理员admin
		} else {
			wechatTemplateBo.setEnterpriseid(getEnterpriseid());
		}
		wechatTemplateBo.setSmstypename(SmsEnum.getText(wechatTemplateForm.getSmstype()));
		return iWechatTemplateService.get(wechatTemplateBo);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public WechatTemplateVo add(WechatTemplateForm wechatTemplateForm)  {
		Date curr = new Date();
		// 复制属性
		WechatTemplateBo wechatTemplateBo = new WechatTemplateBo();
		BeanUtils.copyProperties(wechatTemplateForm, wechatTemplateBo);
		// 属性复制
		
		wechatTemplateBo.setCurr(curr);
		if (getUsertype() == WaterConstants.USER_TYPE_ADMIN) {
			// 最高管理员admin
			wechatTemplateBo.setUserBy("admin");
		} else {
			wechatTemplateBo.setEnterpriseid(getEnterpriseid());
			wechatTemplateBo.setUserBy(getName());
		}

		// 校验是否存在
		Boolean flag = iWechatTemplateService.isExist(wechatTemplateBo);
		if (flag) {
			throw new FrameworkRuntimeException(ResultCode.Fail, "该消息类型在这个企业下已经存在");
		} else {
			wechatTemplateBo.setCreateuser(getUserid());
			wechatTemplateBo.setCreatetime(curr);
			wechatTemplateBo.setUpdatetime(curr);
			wechatTemplateBo.setUpdateuser(getUserid());
			wechatTemplateBo.setDelflag(0);
			wechatTemplateBo.setStatus(WaterConstants.WECHAT_TEMPLATE_STATUS_ENABLE);
			wechatTemplateBo.setBindtime(curr);
			wechatTemplateBo.setSmstypename(SmsEnum.getText(wechatTemplateBo.getSmstype()));
			iWechatTemplateService.add(wechatTemplateBo);
		}

		return null;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public WechatTemplateVo edit(WechatTemplateForm wechatTemplateForm)  {
		UserVo user = AuthCasClient.getUser();
		Date curr = new Date();
		String userBy = user.getName();
		// 复制属性
		WechatTemplateBo wechatTemplateBo = new WechatTemplateBo();
		BeanUtils.copyProperties(wechatTemplateForm, wechatTemplateBo);
		wechatTemplateBo.setUserBy(userBy);
		wechatTemplateBo.setCurr(curr);
		wechatTemplateBo.setUpdatetime(curr);
		return iWechatTemplateService.edit(wechatTemplateBo);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public String del(WechatTemplateForm wechatTemplateForm)  {
		String id = wechatTemplateForm.getId();
		WechatTemplateBo wechatTemplateBo = new WechatTemplateBo();
		wechatTemplateBo.setId(id);
		wechatTemplateBo.setUpdatetime(new Date());
		wechatTemplateBo.setDelflag(1);
		iWechatTemplateService.edit(wechatTemplateBo);
		return id;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public WechatTemplateVo disable(WechatTemplateForm wechatTemplateForm)  {
		UserVo user = AuthCasClient.getUser();
		Date curr = new Date();
		String userBy = user.getName();
		// 复制属性
		WechatTemplateBo wechatTemplateBo = new WechatTemplateBo();
		BeanUtils.copyProperties(wechatTemplateForm, wechatTemplateBo);
		wechatTemplateBo.setUserBy(userBy);
		wechatTemplateBo.setCurr(curr);
		wechatTemplateBo.setUpdatetime(curr);
		wechatTemplateBo.setStatus(WaterConstants.WECHAT_TEMPLATE_STATUS_DISABLE);
		return iWechatTemplateService.edit(wechatTemplateBo);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public WechatTemplateVo enable(WechatTemplateForm wechatTemplateForm)  {
		UserVo user = AuthCasClient.getUser();
		Date curr = new Date();
		String userBy = user.getName();
		// 复制属性
		WechatTemplateBo wechatTemplateBo = new WechatTemplateBo();
		BeanUtils.copyProperties(wechatTemplateForm, wechatTemplateBo);
		wechatTemplateBo.setUserBy(userBy);
		wechatTemplateBo.setCurr(curr);
		wechatTemplateBo.setUpdatetime(curr);
		wechatTemplateBo.setStatus(WaterConstants.WECHAT_TEMPLATE_STATUS_ENABLE);
		return iWechatTemplateService.edit(wechatTemplateBo);
	}

}
