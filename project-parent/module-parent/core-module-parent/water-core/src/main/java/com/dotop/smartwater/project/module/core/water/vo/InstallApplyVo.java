package com.dotop.smartwater.project.module.core.water.vo;

import com.dotop.smartwater.dependence.core.common.BaseVo;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 报装-报装申请
 * 

 * @date 2019年3月8日
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class InstallApplyVo extends BaseVo {

	/** 主键 */
	private String id;
	/** 单号 */
	private String number;
	/** 申请编号 */
	private String no;
	/** 申请户名 */
	private String name;
	/** 联系人 */
	private String contacts;
	/** 联系电话 */
	private String phone;
	/** 证件类型 */
	private String cardType;
	/** 证件号码 */
	private String cardId;
	/** 申请户数 */
	private String households;
	/** 设备数量 */
	private String deviceNumbers;
	/** 区域ID */
	private String communityId;
	/** 区域名称 */
	private String communityName;
	/** 申请类型 */
	private String applyTypeId;
	/** 类型名称 */
	private String applyTypeName;
	/** 用途ID */
	private String purposeId;
	/** 用途名称 */
	private String purposeName;
	/** 详细地址 */
	private String addr;
	/** 证件齐全 */
	private String cardComplete;
	/** 用水报告齐全 */
	private String reportComplete;
	/** 上传附件 */
	private String uploadFile;

}
