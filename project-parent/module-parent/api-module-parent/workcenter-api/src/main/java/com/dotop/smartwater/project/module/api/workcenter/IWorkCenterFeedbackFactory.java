package com.dotop.smartwater.project.module.api.workcenter;

import com.dotop.smartwater.project.module.core.water.bo.customize.WorkCenterFeedbackBo;

/**
 * 工作中心流程反馈管理
 * 

 * @date 2019年4月17日
 * @description
 */
public interface IWorkCenterFeedbackFactory {

	/**
	 * 新增流程反馈
	 */
	void add(WorkCenterFeedbackBo feedbackBo) ;

	/**
	 * 流程流转反馈
	 */
	void exchange(WorkCenterFeedbackBo feedbackBo) ;

	/**
	 * 结束流程反馈
	 */
	void end(WorkCenterFeedbackBo feedbackBo) ;
}
