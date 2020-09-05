package com.dotop.smartwater.project.module.core.water.vo;

import com.dotop.smartwater.dependence.core.common.BaseVo;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 报装-勘测
 * 

 * @date 2019年3月8日
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class InstallSurveyVo extends BaseVo {
	/** 主键 */
	private String id;
	/** 单号 */
	private String number;
	/** 勘测人ID */
	private String surveyId;
	/** 勘测人名称 */
	private String surveyName;
	/** 截止日期 */
	private String endTime;
	/** 勘测时间 */
	private String surveyTime;
	/** 勘测结果 */
	private String status;
	/** 提交状态 */
	private String submitStatus;
	/** 勘测地址 */
	private String addr;
	/** 上报地址 */
	private String place;
	/** 说明 */
	private String explan;
	/** 上传附件 */
	private String uploadFile;

	/** 区域ID */
	private String communityId;
	/** 区域名称 */
	private String communityName;
	/** 用途ID */
	private String purposeId;
	/** 用途名称 */
	private String purposeName;
	/** 手机号 */
	private String phone;
	/** 对接人 */
	private String applyName;
	/** 报装类型 */
	private String typeName;
	/** 报装类型ID */
	private String typeId;
	/** 设备数量 */
	private String deviceNumber;
}
