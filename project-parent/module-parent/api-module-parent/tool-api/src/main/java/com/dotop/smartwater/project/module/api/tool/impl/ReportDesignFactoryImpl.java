package com.dotop.smartwater.project.module.api.tool.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import com.dotop.smartwater.project.module.api.tool.IReportDesignFactory;
import org.springframework.stereotype.Component;

import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.dependence.core.utils.BeanUtils;
import com.dotop.smartwater.dependence.core.utils.JSONUtils;
import com.dotop.smartwater.dependence.core.utils.UuidUtils;
import com.dotop.smartwater.project.module.core.auth.local.AuthCasClient;
import com.dotop.smartwater.project.module.core.auth.vo.UserVo;
import com.dotop.smartwater.project.module.core.water.bo.ReportDesignBo;
import com.dotop.smartwater.project.module.core.water.bo.ReportRelationBo;
import com.dotop.smartwater.project.module.core.water.form.ReportDesignForm;
import com.dotop.smartwater.project.module.core.water.vo.ReportDesignVo;
import com.dotop.smartwater.project.module.service.tool.IReportDesignService;

/**
 * 
 * 报表展示设计Factory

 * @date 2019-07-23
 *
 */
@Component
public class ReportDesignFactoryImpl implements IReportDesignFactory {

	@Resource
	private IReportDesignService iReportDesignService;
	
	@Override
	public Pagination<ReportDesignVo> page(ReportDesignForm reportDesignForm) {
		// TODO Auto-generated method stub
		UserVo user = AuthCasClient.getUser();
		ReportDesignBo reportDesignBo = BeanUtils.copy(reportDesignForm, ReportDesignBo.class);
		reportDesignBo.setEnterpriseid(user.getEnterpriseid());
		return iReportDesignService.page(reportDesignBo);
	}
	
	@Override
	public List<ReportDesignVo> list(ReportDesignForm reportDesignForm) {
		// TODO Auto-generated method stub
		UserVo user = AuthCasClient.getUser();
		ReportDesignBo reportDesignBo = BeanUtils.copy(reportDesignForm, ReportDesignBo.class);
		reportDesignBo.setEnterpriseid(user.getEnterpriseid());
		return iReportDesignService.list(reportDesignBo);
	}

	@Override
	public ReportDesignVo add(ReportDesignForm reportDesignForm) {
		// TODO Auto-generated method stub
		UserVo user = AuthCasClient.getUser();
		ReportDesignBo reportDesignBo = BeanUtils.copy(reportDesignForm, ReportDesignBo.class);
		reportDesignBo.setCreateUserId(user.getUserid());
		reportDesignBo.setReportDesignId(UuidUtils.getUuid());
		reportDesignBo.setCreateTime(new Date());
		reportDesignBo.setEnterpriseid(user.getEnterpriseid());
		if(reportDesignBo.getReportRelations() != null && !reportDesignBo.getReportRelations().isEmpty()) {
			for(ReportRelationBo rb: reportDesignBo.getReportRelations()) {
				rb.setReportRelationId(UuidUtils.getUuid());
				rb.setReportDesignId(reportDesignBo.getReportDesignId());
			}
		}
		return iReportDesignService.add(reportDesignBo);
	}

	@Override
	public ReportDesignVo get(ReportDesignForm reportDesignForm) {
		// TODO Auto-generated method stub
		UserVo user = AuthCasClient.getUser();
		ReportDesignBo reportDesignBo = BeanUtils.copy(reportDesignForm, ReportDesignBo.class);
		reportDesignBo.setEnterpriseid(user.getEnterpriseid());
		System.out.println("reportDesignBo: " + JSONUtils.toJSONString(reportDesignBo));
		return iReportDesignService.get(reportDesignBo);
	}

	@Override
	public String del(ReportDesignForm reportDesignForm) {
		// TODO Auto-generated method stub
		AuthCasClient.getUser();
		ReportDesignBo reportDesignBo = BeanUtils.copy(reportDesignForm, ReportDesignBo.class);
		
		return iReportDesignService.del(reportDesignBo);
	}

	@Override
	public ReportDesignVo edit(ReportDesignForm reportDesignForm) {
		// TODO Auto-generated method stub
		AuthCasClient.getUser();
		ReportDesignBo reportDesignBo = BeanUtils.copy(reportDesignForm, ReportDesignBo.class);
		if(reportDesignBo.getReportRelations() != null && !reportDesignBo.getReportRelations().isEmpty()) {
			for(ReportRelationBo rb: reportDesignBo.getReportRelations()) {
				if(rb.getReportRelationId() == null || rb.getReportRelationId() == "") {
					rb.setReportRelationId(UuidUtils.getUuid());
					rb.setReportDesignId(reportDesignBo.getReportDesignId());
				}
			}
		}
		return iReportDesignService.edit(reportDesignBo);
	}

}
