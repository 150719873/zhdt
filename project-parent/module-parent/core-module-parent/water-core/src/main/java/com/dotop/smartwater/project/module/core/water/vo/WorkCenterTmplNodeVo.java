package com.dotop.smartwater.project.module.core.water.vo;

import java.util.List;

import com.dotop.smartwater.dependence.core.common.BaseVo;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**

 * @date 2019年3月4日
 * @description
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class WorkCenterTmplNodeVo extends BaseVo {

	private String id;

	private String name;

	private String desc;

	private String type;

	// 排序
	private String sort;

	private String tmplId;

	private String parentId;

	// 处理人id
	private List<String> handlers;
	// 抄送人角色id
	private List<String> carbonCopyers;
	// 处理人id
	private List<String> handlerRoles;
	// 抄送人角色id
	private List<String> carbonCopyerRoles;

	// 处理结果-字典类别id
	// private String handleDictId;
	private DictionaryVo handleDict;

	// 是否验证结果
	private String ifVerify;
	// 验证结果-是-template_id
	private String verifyTmplNodeId;
	// 验证结果-否-template_id
	private String noVerifyTmplNodeId;

	// 是否通知
	private String ifNotice;
	// 通知人id
	private List<String> noticers;
	// 通知人角色id
	private List<String> noticerRoles;

	// 是否更新数据
	private String ifUpdate;
	// 更新对象-字典类型id
	// private String updateDictChildId;
	private DictionaryChildVo updateDictChild;

	// 是否拍照
	private String ifPhoto;
	// 最大拍照张数
	private String photoNum;

	// 是否上传文件
	private String ifUpload;
	// 最大文件数
	private String uploadNum;

	// 是否处理意见
	private String ifOpinion;
	// 最大意见字数
	private String opinionNum;

}
