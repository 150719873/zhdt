package com.dotop.smartwater.project.module.core.water.vo;

import com.dotop.smartwater.dependence.core.common.BaseVo;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class InstallAcceptanceVo extends BaseVo {

	/** 主键 */
	private String id;
	/** 单号 */
	private String number;
	/** 验收人ID */
	private String acceptId;
	/** 验收人姓名 */
	private String acceptName;
	/** 验收人截止时间 */
	private String endTime;
	/** 上报地址 */
	private String place;
	/** 验收时间 */
	private String acceptTime;
	/** 验收状态 */
	private String status;
	/** 提交状态 */
	private String submitStatus;
	/** 验收地址 */
	private String addr;
	/** 说明 */
	private String explan;
	/** 附件 */
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
