package com.dotop.smartwater.project.module.api.revenue.impl;

import java.util.Date;

import com.dotop.smartwater.project.module.api.revenue.IPerformanceTemplateFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.project.module.core.auth.local.AuthCasClient;
import com.dotop.smartwater.project.module.core.auth.vo.UserVo;
import com.dotop.smartwater.project.module.core.water.bo.PerforTemplateBo;
import com.dotop.smartwater.project.module.core.water.form.PerforTemplateForm;
import com.dotop.smartwater.project.module.core.water.vo.PerforTemplateVo;
import com.dotop.smartwater.project.module.service.revenue.IPerformanceTemplateService;

@Component
public class PerformanceTemplateFactoryImpl implements IPerformanceTemplateFactory {

	@Autowired
	private IPerformanceTemplateService iPerformanceTemplateService;

	@Override
	public Pagination<PerforTemplateVo> page(PerforTemplateForm form) throws FrameworkRuntimeException {
		UserVo user = AuthCasClient.getUser();
		PerforTemplateBo bo = new PerforTemplateBo();
		BeanUtils.copyProperties(form, bo);
		// 调用service
		bo.setEnterpriseid(user.getEnterpriseid());
		Pagination<PerforTemplateVo> pagination = iPerformanceTemplateService.page(bo);
		return pagination;
	}

	@Override
	public boolean saveTemp(PerforTemplateForm form) throws FrameworkRuntimeException {
		UserVo user = AuthCasClient.getUser();
		PerforTemplateBo bo = new PerforTemplateBo();
		BeanUtils.copyProperties(form, bo);
		bo.setEnterpriseid(user.getEnterpriseid());
		bo.setUserBy(user.getName());
		bo.setCurr(new Date());
		return iPerformanceTemplateService.saveTemp(bo);
	}

	public boolean updateTemp(PerforTemplateForm form) throws FrameworkRuntimeException {
		UserVo user = AuthCasClient.getUser();
		PerforTemplateBo bo = new PerforTemplateBo();
		BeanUtils.copyProperties(form, bo);
		bo.setEnterpriseid(user.getEnterpriseid());
		bo.setUserBy(user.getName());
		bo.setCurr(new Date());
		return iPerformanceTemplateService.updateTemp(bo);
	}

	public boolean deleteTemp(PerforTemplateForm form) throws FrameworkRuntimeException {
		UserVo user = AuthCasClient.getUser();
		PerforTemplateBo bo = new PerforTemplateBo();
		BeanUtils.copyProperties(form, bo);
		bo.setEnterpriseid(user.getEnterpriseid());
		return iPerformanceTemplateService.deleteTemp(bo);
	}

	public PerforTemplateVo getTemp(PerforTemplateForm form) throws FrameworkRuntimeException {
		UserVo user = AuthCasClient.getUser();
		PerforTemplateBo bo = new PerforTemplateBo();
		BeanUtils.copyProperties(form, bo);
		bo.setEnterpriseid(user.getEnterpriseid());
		return iPerformanceTemplateService.getTemp(bo);
	}
}
