package com.dotop.smartwater.project.module.api.revenue;

import com.dotop.smartwater.dependence.core.common.BaseFactory;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.project.module.core.water.form.PerforExamAuditForm;
import com.dotop.smartwater.project.module.core.water.form.PerforExamForm;
import com.dotop.smartwater.project.module.core.water.form.PerforExamPersonnelForm;
import com.dotop.smartwater.project.module.core.water.vo.*;

import java.util.List;

public interface IPerformanceExamFactory extends BaseFactory<PerforExamForm, PerforExamVo> {

	/**
	 * 考核分页查询
	 * 
	 * @param form
	 * @return
	 * @
	 */
	@Override
	Pagination<PerforExamVo> page(PerforExamForm form) ;

	/**
	 * 发起考核
	 * 
	 * @param form
	 * @return
	 * @
	 */
	boolean saveExam(PerforExamForm form) ;

	/**
	 * 考核对象详情
	 * 
	 * @param form
	 * @return
	 * @
	 */
	PerforExamVo getExam(PerforExamForm form) ;

	/**
	 * 获取考核下所有考核对象人员信息
	 * 
	 * @param form
	 * @return
	 * @
	 */
	List<PerforExamPersonnelVo> getExamPersonnels(PerforExamForm form) ;

	/**
	 * 获取考核人员信息
	 * 
	 * @param form
	 * @return
	 * @
	 */
	PerforExamPersonnelVo getExamPersonnel(PerforExamPersonnelForm form) ;

	/**
	 * 删除绩效考核信息
	 * 
	 * @param form
	 * @return
	 * @
	 */
	boolean deleteExam(PerforExamForm form) ;

	/**
	 * 验证当前批次号下是否全部审核
	 * 
	 * @param form
	 * @return
	 * @
	 */
	int auditEnd(PerforExamForm form) ;

	/**
	 * 结束批次号下所有审核信息
	 * 
	 * @param form
	 * @return
	 * @
	 */
	boolean endExam(PerforExamForm form) ;

	/**
	 * 获取审核人员信息
	 * 
	 * @param form
	 * @return
	 * @
	 */
	List<PerforExamAuditVo> getExamAudits(PerforExamPersonnelForm form) ;

	/**
	 * 获取考核权重及评分信息
	 * 
	 * @param form
	 * @return
	 * @
	 */
	List<PerforExamWeightVo> getExamWeights(PerforExamPersonnelForm form) ;

	/**
	 * 个人考核列表
	 * 
	 * @param form
	 * @return
	 * @
	 */
	Pagination<PerforExamPersonnelVo> selfPage(PerforExamPersonnelForm form) ;

	/**
	 * 退回审核人提交信息
	 * 
	 * @param form
	 * @return
	 * @
	 */
	boolean returnAudit(PerforExamAuditForm form) ;

	/**
	 * 获取考核权重及评分信息
	 * 
	 * @param form
	 * @return
	 * @
	 */
	List<PerforExamFillVo> getExamFills(PerforExamPersonnelForm form) ;

	/**
	 * 提交个人考核
	 * 
	 * @param form
	 * @return
	 * @
	 */
	boolean submitFills(PerforExamPersonnelForm form) ;

	/**
	 * 绩效审核列表
	 * 
	 * @param form
	 * @return
	 * @
	 */
	Pagination<PerforExamAuditVo> auditPage(PerforExamAuditForm form) ;

	/**
	 * 获取审核信息
	 * 
	 * @param form
	 * @return
	 * @
	 */
	PerforExamAuditVo getAudit(PerforExamAuditForm form) ;

	/**
	 * 提交绩效审核
	 * 
	 * @param form
	 * @return
	 * @
	 */
	boolean submitAudit(PerforExamAuditForm form) ;

}
