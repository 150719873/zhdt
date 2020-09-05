package com.dotop.smartwater.project.module.core.water.dto;

import java.util.List;

import com.dotop.smartwater.dependence.core.common.BaseDto;
import com.dotop.smartwater.project.module.core.water.vo.InstallTemplateRelationVo;
import com.dotop.smartwater.project.module.core.water.vo.InstallTemplateRelationVo;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 报装-预约管理
 * 

 * @date 2019年3月8日
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class InstallAppointmentDto extends BaseDto {
	/** 主键 */
	private String id;
	/** 预约单号 */
	private String number;
	/** 业务类型ID */
	private String typeId;
	/** 业务名称 */
	private String typeName;
	/** 模板ID */
	private String templateId;
	/** 模板名称 */
	private String templateName;
	/** 申请人ID（openid、uuid） */
	private String applyId;
	/** 申请人 */
	private String applyName;
	/** 手机号 */
	private String phone;
	/** 证件类型 */
	private String cardType;
	/** 证件号码 */
	private String cardId;
	/** 预约时间 */
	private String appTime;
	/** 处理状态 */
	private String status;
	/** 预约状态 */
	private String appStatus;

	/** 办理地址 */
	private String addr;
	/** 区域ID */
	private String communityId;
	/** 区域名称 */
	private String communityName;
	/** 用途ID */
	private String purposeId;
	/** 用途名称 */
	private String purposeName;

	/** 功能表 */
	private List<InstallTemplateRelationVo> relations;
}
