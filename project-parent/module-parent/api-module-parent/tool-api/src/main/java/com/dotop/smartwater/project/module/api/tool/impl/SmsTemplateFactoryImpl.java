package com.dotop.smartwater.project.module.api.tool.impl;

import java.util.Date;

import com.dotop.smartwater.project.module.api.tool.ISmsTemplateFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.dependence.core.utils.UuidUtils;
import com.dotop.smartwater.project.module.core.auth.enums.SmsEnum;
import com.dotop.smartwater.project.module.core.auth.local.IAuthCasClient;
import com.dotop.smartwater.project.module.core.water.bo.SmsTemplateBo;
import com.dotop.smartwater.project.module.core.water.constants.WaterConstants;
import com.dotop.smartwater.project.module.core.water.form.SmsTemplateForm;
import com.dotop.smartwater.project.module.core.water.vo.SmsTemplateVo;
import com.dotop.smartwater.project.module.service.tool.ISmsTemplateService;

@Component
public class SmsTemplateFactoryImpl implements ISmsTemplateFactory, IAuthCasClient {
	
	@Autowired
	private ISmsTemplateService iSmsTemplateService;

	@Override
	public Pagination<SmsTemplateVo> getPageList(SmsTemplateForm smsTemplate)  {
		SmsTemplateBo smsTemplateBo = new SmsTemplateBo();
		BeanUtils.copyProperties(smsTemplate, smsTemplateBo);
		if (getUsertype() == WaterConstants.USER_TYPE_ADMIN) {
			// 最高管理员admin
		} else {
			smsTemplateBo.setEnterpriseid(getEnterpriseid());
		}
		return iSmsTemplateService.getPageList(smsTemplateBo);
	}

	@Override
	public int addSmsTemplate(SmsTemplateForm smsTemplate)  {
		Date curr = new Date();
		SmsTemplateBo smsTemplateBo = new SmsTemplateBo();
		BeanUtils.copyProperties(smsTemplate, smsTemplateBo);
		smsTemplateBo.setId(UuidUtils.getUuid());
		smsTemplateBo.setCreatetime(curr);
		smsTemplateBo.setUpdatetime(curr);
		smsTemplateBo.setDelflag(0);
		smsTemplateBo.setBindtime(curr);
		if (getUsertype() == WaterConstants.USER_TYPE_ADMIN) {
			smsTemplateBo.setCreateuser("admin");
		} else {
			smsTemplateBo.setEnterpriseid(getEnterpriseid());
			smsTemplateBo.setCreateuser(getName());
		}
		smsTemplateBo.setSmstypename(SmsEnum.getText(smsTemplateBo.getSmstype()));
		return iSmsTemplateService.addSmsTemplate(smsTemplateBo);
	}

	@Override
	public SmsTemplateVo getSmsTemplateVo(SmsTemplateForm smsTemplate)  {
		SmsTemplateBo smsTemplateBo = new SmsTemplateBo();
		BeanUtils.copyProperties(smsTemplate, smsTemplateBo);
		return iSmsTemplateService.getSmsTemplateVo(smsTemplateBo);
	}

	@Override
	public SmsTemplateVo getBySmstype(String enterpriseid, Integer smstype)  {
		return iSmsTemplateService.getBySmstype(enterpriseid, smstype);
	}
	@Override
	public SmsTemplateVo getEnableByCode(String enterpriseid, Integer modeType)  {
		return iSmsTemplateService.getEnableByCode(enterpriseid, modeType);
	}

	@Override
	public int updateSmsTemplate(SmsTemplateForm smsTemplate)  {
		Date curr = new Date();
		SmsTemplateBo smsTemplateBo = new SmsTemplateBo();
		BeanUtils.copyProperties(smsTemplate, smsTemplateBo);
		smsTemplateBo.setUpdateuser("admin");
		smsTemplateBo.setUpdatetime(curr);
		return iSmsTemplateService.updateSmsTemplate(smsTemplateBo);
	}

	@Override
	public int delete(String id)  {
		return iSmsTemplateService.delete(id);
	}

	@Override
	public int updateStatus(String id, int status)  {
		return iSmsTemplateService.updateStatus(id, status);
	}
	
}
