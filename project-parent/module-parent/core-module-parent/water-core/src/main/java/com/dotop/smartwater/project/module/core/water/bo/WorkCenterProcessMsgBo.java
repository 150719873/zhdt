package com.dotop.smartwater.project.module.core.water.bo;

import java.util.Date;
import java.util.List;

import com.dotop.smartwater.dependence.core.common.BaseBo;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**

 * @date 2019年3月4日
 * @description
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class WorkCenterProcessMsgBo extends BaseBo {

	private String id;

	private String processId;

	private String parentId;

	private String status;

	private String processNodeId;

	// 处理结果-字典类型id
	// private String handleDictChildId;
	private DictionaryChildBo handleDictChild;

	private List<String> uploadPhotos;

	private List<String> uploadFiles;

	// 意见内容
	private String opinionContent;

	// 处理人 (完成节点提交人)
	private String completer;

	// 处理人 (完成节点提交人)
	private String completerName;

	// 处理时间 (完成节点提交人)
	private Date completeDate;

	// 上报位置 (经度，纬度)
	private List<String> coordinates;

	// 处理人id
	private List<String> handlers;
	// 抄送人角色id
	private List<String> carbonCopyers;
	// 处理人id
	private List<String> handlerRoles;
	// 抄送人角色id
	private List<String> carbonCopyerRoles;

	// 通知人id
	private List<String> noticers;
	// 通知人角色id
	private List<String> noticerRoles;
}
