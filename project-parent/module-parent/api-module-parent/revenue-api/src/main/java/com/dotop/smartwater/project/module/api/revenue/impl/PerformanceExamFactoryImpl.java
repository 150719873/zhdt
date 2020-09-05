package com.dotop.smartwater.project.module.api.revenue.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.dotop.smartwater.project.module.api.revenue.INumRuleSetFactory;
import com.dotop.smartwater.project.module.api.revenue.IPerformanceExamFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.project.module.core.auth.bo.UserBo;
import com.dotop.smartwater.project.module.core.auth.local.AuthCasClient;
import com.dotop.smartwater.project.module.core.auth.vo.UserVo;
import com.dotop.smartwater.project.module.core.water.bo.PerforExamAuditBo;
import com.dotop.smartwater.project.module.core.water.bo.PerforExamBo;
import com.dotop.smartwater.project.module.core.water.bo.PerforExamFillBo;
import com.dotop.smartwater.project.module.core.water.bo.PerforExamPersonnelBo;
import com.dotop.smartwater.project.module.core.water.config.Config;
import com.dotop.smartwater.project.module.core.water.constants.ExamStatus;
import com.dotop.smartwater.project.module.core.water.constants.ResultCode;
import com.dotop.smartwater.project.module.core.water.form.PerforExamAuditForm;
import com.dotop.smartwater.project.module.core.water.form.PerforExamFillForm;
import com.dotop.smartwater.project.module.core.water.form.PerforExamForm;
import com.dotop.smartwater.project.module.core.water.form.PerforExamPersonnelForm;
import com.dotop.smartwater.project.module.core.water.form.customize.MakeNumRequest;
import com.dotop.smartwater.project.module.core.water.vo.PerforExamAuditVo;
import com.dotop.smartwater.project.module.core.water.vo.PerforExamFillVo;
import com.dotop.smartwater.project.module.core.water.vo.PerforExamPersonnelVo;
import com.dotop.smartwater.project.module.core.water.vo.PerforExamVo;
import com.dotop.smartwater.project.module.core.water.vo.PerforExamWeightVo;
import com.dotop.smartwater.project.module.core.water.vo.customize.MakeNumVo;
import com.dotop.smartwater.project.module.service.revenue.IPerformanceExamService;
import com.dotop.water.tool.service.BaseInf;

/**
 * 绩效考核-考核
 * 

 * @date 2019年2月28日
 *
 */
@Component
public class PerformanceExamFactoryImpl implements IPerformanceExamFactory {

	@Autowired
	private IPerformanceExamService performanceExamService;

	@Autowired
	private INumRuleSetFactory iNumRuleSetFactory;

	@Override
	public Pagination<PerforExamVo> page(PerforExamForm form) throws FrameworkRuntimeException {
		UserVo user = AuthCasClient.getUser();
		PerforExamBo bo = new PerforExamBo();
		BeanUtils.copyProperties(form, bo);
		bo.setEnterpriseid(user.getEnterpriseid());
		Pagination<PerforExamVo> pagination = performanceExamService.page(bo);
		return pagination;
	}

	@Override
	public boolean saveExam(PerforExamForm form) throws FrameworkRuntimeException {
		UserVo user = AuthCasClient.getUser();
		PerforExamBo bo = new PerforExamBo();
		BeanUtils.copyProperties(form, bo);
		bo.setEnterpriseid(user.getEnterpriseid());
		bo.setUserBy(user.getName());
		bo.setCurr(new Date());

		// TODO 生成批次号
		MakeNumRequest make = new MakeNumRequest();
		make.setRuleid(1);
		make.setCount(1);
		MakeNumVo vo = iNumRuleSetFactory.makeNo(make);
		if (vo != null && vo.getNumbers() != null && vo.getNumbers().size() > 0) {
			bo.setNumber(vo.getNumbers().get(0));
		} else {
			bo.setNumber(String.valueOf(Config.Generator.nextId()));
		}

		// TODO 根据考核人员ID、审核人员ID获取人员信息
		List<UserBo> userAudits = new ArrayList<UserBo>();
		String ids = "";
		for (String id : form.getAuditIds().split(",")) {
			if (ids.equals("")) {
				ids = "'" + id + "'";
			} else {
				ids += ",'" + id + "'";
			}
		}

		List<UserVo> audits = BaseInf.getUsers(user.getUserid(), user.getTicket(), ids);
		if (audits != null && audits.size() > 0) {
			for (UserVo userVo : audits) {
				UserBo userBo = new UserBo();
				BeanUtils.copyProperties(userVo, userBo);
				userAudits.add(userBo);
			}
		} else {
			throw new FrameworkRuntimeException(ResultCode.Fail, "无法获取用户信息");
		}

		ids = "";
		for (String id : form.getExamineIds().split(",")) {
			if (ids.equals("")) {
				ids = "'" + id + "'";
			} else {
				ids += ",'" + id + "'";
			}
		}
		List<UserBo> userExamines = new ArrayList<UserBo>();
		List<UserVo> examines = BaseInf.getUsers(user.getUserid(), user.getTicket(), ids);
		if (examines != null && examines.size() > 0) {
			for (UserVo userVo : examines) {
				UserBo userBo = new UserBo();
				BeanUtils.copyProperties(userVo, userBo);
				userExamines.add(userBo);
			}
		} else {
			throw new FrameworkRuntimeException(ResultCode.Fail, "无法获取用户信息");
		}

		bo.setStatus(ExamStatus.NOSTART);
		bo.setExamineUsers(userExamines);
		bo.setAuditUsers(userAudits);
		return performanceExamService.saveExam(bo);
	}

	public PerforExamVo getExam(PerforExamForm form) throws FrameworkRuntimeException {
		UserVo user = AuthCasClient.getUser();
		PerforExamBo bo = new PerforExamBo();
		BeanUtils.copyProperties(form, bo);
		bo.setEnterpriseid(user.getEnterpriseid());
		return performanceExamService.getExam(bo);
	}

	public boolean deleteExam(PerforExamForm form) throws FrameworkRuntimeException {
		UserVo user = AuthCasClient.getUser();
		PerforExamBo bo = new PerforExamBo();
		BeanUtils.copyProperties(form, bo);
		bo.setEnterpriseid(user.getEnterpriseid());
		return performanceExamService.deleteExam(bo);
	}

	public List<PerforExamPersonnelVo> getExamPersonnels(PerforExamForm form) throws FrameworkRuntimeException {
		UserVo user = AuthCasClient.getUser();
		PerforExamBo bo = new PerforExamBo();
		BeanUtils.copyProperties(form, bo);
		bo.setEnterpriseid(user.getEnterpriseid());
		return performanceExamService.getExamPersonnels(bo);
	}

	public PerforExamPersonnelVo getExamPersonnel(PerforExamPersonnelForm form) throws FrameworkRuntimeException {
		UserVo user = AuthCasClient.getUser();
		PerforExamPersonnelBo bo = new PerforExamPersonnelBo();
		BeanUtils.copyProperties(form, bo);
		bo.setEnterpriseid(user.getEnterpriseid());
		return performanceExamService.getExamPersonnel(bo);
	}

	public int auditEnd(PerforExamForm form) throws FrameworkRuntimeException {
		UserVo user = AuthCasClient.getUser();
		PerforExamBo bo = new PerforExamBo();
		BeanUtils.copyProperties(form, bo);
		bo.setEnterpriseid(user.getEnterpriseid());
		return performanceExamService.auditEnd(bo);
	}

	public boolean endExam(PerforExamForm form) throws FrameworkRuntimeException {
		UserVo user = AuthCasClient.getUser();
		PerforExamBo bo = new PerforExamBo();
		BeanUtils.copyProperties(form, bo);
		bo.setEnterpriseid(user.getEnterpriseid());
		return performanceExamService.endExam(bo);
	}

	@Override
	public List<PerforExamAuditVo> getExamAudits(PerforExamPersonnelForm form) throws FrameworkRuntimeException {
		UserVo user = AuthCasClient.getUser();
		PerforExamPersonnelBo bo = new PerforExamPersonnelBo();
		BeanUtils.copyProperties(form, bo);
		bo.setEnterpriseid(user.getEnterpriseid());
		return performanceExamService.getExamAudits(bo);
	}

	@Override
	public List<PerforExamWeightVo> getExamWeights(PerforExamPersonnelForm form) throws FrameworkRuntimeException {
		UserVo user = AuthCasClient.getUser();
		PerforExamPersonnelBo bo = new PerforExamPersonnelBo();
		BeanUtils.copyProperties(form, bo);
		bo.setEnterpriseid(user.getEnterpriseid());
		return performanceExamService.getExamWeights(bo);
	}

	@Override
	public Pagination<PerforExamPersonnelVo> selfPage(PerforExamPersonnelForm form) throws FrameworkRuntimeException {
		UserVo user = AuthCasClient.getUser();
		PerforExamPersonnelBo bo = new PerforExamPersonnelBo();
		BeanUtils.copyProperties(form, bo);
		bo.setAssessmentId(user.getUserid());
		bo.setEnterpriseid(user.getEnterpriseid());
		Pagination<PerforExamPersonnelVo> pagination = performanceExamService.selfPage(bo);
		return pagination;
	}

	@Override
	public List<PerforExamFillVo> getExamFills(PerforExamPersonnelForm form) throws FrameworkRuntimeException {
		UserVo user = AuthCasClient.getUser();
		PerforExamPersonnelBo bo = new PerforExamPersonnelBo();
		BeanUtils.copyProperties(form, bo);
		bo.setEnterpriseid(user.getEnterpriseid());
		return performanceExamService.getExamFills(bo);
	}

	@Override
	public boolean submitFills(PerforExamPersonnelForm form) throws FrameworkRuntimeException {
		UserVo user = AuthCasClient.getUser();
		PerforExamPersonnelBo bo = new PerforExamPersonnelBo();
		BeanUtils.copyProperties(form, bo);
		List<PerforExamFillBo> fills = new ArrayList<PerforExamFillBo>();
		for (PerforExamFillForm fm : form.getFillArry()) {
			PerforExamFillBo fillbo = new PerforExamFillBo();
			fillbo.setScore(fm.getScore());
			fillbo.setNumber(form.getNumber());
			fillbo.setEnterpriseid(form.getEnterpriseid());
			fillbo.setAssessmentId(form.getAssessmentId());
			fillbo.setWeightId(fm.getWeightId());
			fills.add(fillbo);
		}
		bo.setFillArry(fills);
		bo.setEnterpriseid(user.getEnterpriseid());
		bo.setUserBy(user.getName());
		bo.setCurr(new Date());
		return performanceExamService.submitFills(bo);
	}

	@Override
	public Pagination<PerforExamAuditVo> auditPage(PerforExamAuditForm form) throws FrameworkRuntimeException {
		UserVo user = AuthCasClient.getUser();
		PerforExamAuditBo bo = new PerforExamAuditBo();
		BeanUtils.copyProperties(form, bo);
		bo.setEnterpriseid(user.getEnterpriseid());
		Pagination<PerforExamAuditVo> pagination = performanceExamService.auditPage(bo);
		return pagination;
	}

	@Override
	public PerforExamAuditVo getAudit(PerforExamAuditForm form) throws FrameworkRuntimeException {
		UserVo user = AuthCasClient.getUser();
		PerforExamAuditBo bo = new PerforExamAuditBo();
		BeanUtils.copyProperties(form, bo);
		bo.setEnterpriseid(user.getEnterpriseid());
		return performanceExamService.getAudit(bo);
	}

	@Override
	public boolean submitAudit(PerforExamAuditForm form) throws FrameworkRuntimeException {
		UserVo user = AuthCasClient.getUser();
		PerforExamAuditBo bo = new PerforExamAuditBo();
		BeanUtils.copyProperties(form, bo);
		bo.setEnterpriseid(user.getEnterpriseid());
		bo.setUserBy(user.getName());
		bo.setCurr(new Date());
		bo.setAuditTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		return performanceExamService.submitAudit(bo);
	}

	@Override
	public boolean returnAudit(PerforExamAuditForm form) throws FrameworkRuntimeException {
		UserVo user = AuthCasClient.getUser();
		PerforExamAuditBo bo = new PerforExamAuditBo();
		BeanUtils.copyProperties(form, bo);
		bo.setEnterpriseid(user.getEnterpriseid());
		bo.setUserBy(user.getName());
		bo.setCurr(new Date());
		return performanceExamService.returnAudit(bo);
	}

}
