package com.dotop.smartwater.project.module.core.water.vo;

import com.dotop.smartwater.dependence.core.common.BaseVo;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 报装-换表
 * 

 * @date 2019年3月8日
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class InstallChangeVo extends BaseVo {
	/** 主键 */
	private String id;
	/** 单号 */
	private String number;
	/** 用户编号 */
	private String userNo;
	/** 用户名称 */
	private String userName;
	/** 手机号 */
	private String userPhone;
	/** 证件类型 */
	private String cardType;
	/** 证件号码 */
	private String cardId;
	/** 上传附件 */
	private String uploadFile;

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
}
