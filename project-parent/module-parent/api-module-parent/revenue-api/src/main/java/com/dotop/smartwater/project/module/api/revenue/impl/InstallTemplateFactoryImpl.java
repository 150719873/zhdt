package com.dotop.smartwater.project.module.api.revenue.impl;

import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.project.module.api.revenue.IInstallTemplateFactory;
import com.dotop.smartwater.project.module.core.auth.local.AuthCasClient;
import com.dotop.smartwater.project.module.core.auth.vo.UserVo;
import com.dotop.smartwater.project.module.core.water.bo.InstallTemplateBo;
import com.dotop.smartwater.project.module.core.water.bo.InstallTemplateRelationBo;
import com.dotop.smartwater.project.module.core.water.form.InstallTemplateForm;
import com.dotop.smartwater.project.module.core.water.form.InstallTemplateRelationForm;
import com.dotop.smartwater.project.module.core.water.vo.InstallFunctionVo;
import com.dotop.smartwater.project.module.core.water.vo.InstallTemplateRelationVo;
import com.dotop.smartwater.project.module.core.water.vo.InstallTemplateVo;
import com.dotop.smartwater.project.module.service.revenue.IInstallTemplateService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 报装-模板
 * 

 * @date 2019年3月9日
 *
 */
@Component
public class InstallTemplateFactoryImpl implements IInstallTemplateFactory {

	@Autowired
	private IInstallTemplateService InstallTemplateService;

	@Override
	public Pagination<InstallTemplateVo> page(InstallTemplateForm form) throws FrameworkRuntimeException {
		UserVo user = AuthCasClient.getUser();
		InstallTemplateBo bo = new InstallTemplateBo();
		BeanUtils.copyProperties(form, bo);
		bo.setEnterpriseid(user.getEnterpriseid());
		Pagination<InstallTemplateVo> pagination = InstallTemplateService.page(bo);
		return pagination;
	}

	@Override
	public boolean saveTemp(InstallTemplateForm form) throws FrameworkRuntimeException {
		UserVo user = AuthCasClient.getUser();
		InstallTemplateBo bo = new InstallTemplateBo();
		BeanUtils.copyProperties(form, bo);
		bo.setEnterpriseid(user.getEnterpriseid());
		bo.setUserBy(user.getName());
		bo.setCurr(new Date());
		bo.setNodes(String.valueOf(form.getRelations().size()));

		List<InstallTemplateRelationBo> relations = new ArrayList<InstallTemplateRelationBo>();
		for (InstallTemplateRelationForm fbo : form.getRelations()) {
			InstallTemplateRelationBo relation = new InstallTemplateRelationBo();
			BeanUtils.copyProperties(fbo, relation);
			relations.add(relation);
		}
		bo.setRelations(relations);
		return InstallTemplateService.saveTemp(bo);
	}

	@Override
	public boolean editTemp(InstallTemplateForm form) throws FrameworkRuntimeException {
		UserVo user = AuthCasClient.getUser();
		InstallTemplateBo bo = new InstallTemplateBo();
		BeanUtils.copyProperties(form, bo);
		bo.setEnterpriseid(user.getEnterpriseid());
		bo.setUserBy(user.getName());
		bo.setCurr(new Date());
		bo.setNodes(String.valueOf(form.getRelations().size()));

		List<InstallTemplateRelationBo> relations = new ArrayList<InstallTemplateRelationBo>();
		for (InstallTemplateRelationForm fbo : form.getRelations()) {
			InstallTemplateRelationBo relation = new InstallTemplateRelationBo();
			BeanUtils.copyProperties(fbo, relation);
			relations.add(relation);
		}
		bo.setRelations(relations);
		return InstallTemplateService.editTemp(bo);
	}

	@Override
	public boolean deleteTemp(InstallTemplateForm form) throws FrameworkRuntimeException {
		InstallTemplateBo bo = new InstallTemplateBo();
		BeanUtils.copyProperties(form, bo);
		return InstallTemplateService.deleteTemp(bo);
	}

	@Override
	public List<InstallFunctionVo> getFuncs() throws FrameworkRuntimeException {
		return InstallTemplateService.getFuncs();
	}

	@Override
	public List<InstallTemplateRelationVo> getTempNodes(InstallTemplateForm form) throws FrameworkRuntimeException {
		UserVo user = AuthCasClient.getUser();
		InstallTemplateBo bo = new InstallTemplateBo();
		BeanUtils.copyProperties(form, bo);
		bo.setEnterpriseid(user.getEnterpriseid());
		return InstallTemplateService.getTempNodes(bo);
	}

	@Override
	public InstallTemplateVo getTemp(InstallTemplateForm form) throws FrameworkRuntimeException {
		UserVo user = AuthCasClient.getUser();
		InstallTemplateBo bo = new InstallTemplateBo();
		BeanUtils.copyProperties(form, bo);
		bo.setEnterpriseid(user.getEnterpriseid());
		return InstallTemplateService.getTemp(bo);
	}

}
