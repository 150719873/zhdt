package com.dotop.smartwater.project.module.core.water.vo;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.dotop.smartwater.dependence.core.common.BaseVo;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**

 * @date 2019年3月4日
 * @description
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class WorkCenterProcessVo extends BaseVo {

	private String id;

	private String code;

	private String title;

	private String businessId;

	private String businessType;

	private Map<String, String> sqlParams;

	private Map<String, String> showParams;

	private Map<String, String> fillParams;

	private Map<String, String> carryParams;

	private String status;

	private String applicant;

	private String applicantName;

	private Date applicationDate;

	/**
	 * 原模板id
	 */
	private String tmplId;

	private WorkCenterProcessFormVo processForm;

	private List<WorkCenterProcessNodeVo> processNodes;

	// 以下字段设计在process表中，日后可扩展独立的日志表
	// 当前节点字典处理结果
	private DictionaryChildVo currHandleDictChild;

	// 下一个需要做的流程节点
	private String nextProcessNodeId;
	// 下一处理节点状态，例如：选择退回，则下一个节点为退回标记，流程为处理中；选择挂起，则下一个节点为挂起，流程标记为挂起
	private String nextStatus;
	// 处理人id
	private List<String> nextHandlers;
	// 抄送人角色id
	private List<String> nextCarbonCopyers;
	// 处理人id
	private List<String> nextHandlerRoles;
	// 抄送人角色id
	private List<String> nextCarbonCopyerRoles;
	// 指定处理人
	private String assignHandler;
	// 指定处理人
	private String assignHandlerName;

	private WorkCenterProcessNodeVo nextProcessNode;

}
