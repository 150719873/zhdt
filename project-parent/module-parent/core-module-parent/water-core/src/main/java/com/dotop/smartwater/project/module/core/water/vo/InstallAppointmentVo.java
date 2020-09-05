package com.dotop.smartwater.project.module.core.water.vo;

import java.util.List;

import com.dotop.smartwater.dependence.core.common.BaseVo;
import com.dotop.smartwater.project.module.core.water.constants.InstallStatus;
import com.dotop.smartwater.project.module.core.water.constants.InstallStatus;

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
public class InstallAppointmentVo extends BaseVo {
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
	/** 处理状态 -文本 */
	private String statusText;
	/** 预约状态 */
	private String appStatus;
	/** 预约状态 -文本 */
	private String appStatusText;

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

	/** 预约-功能 */
	List<InstallAppointmentRelationVo> relations;

	/** 报装申请 */
	private InstallApplyVo apply;
	/** 换表申请 */
	private InstallChangeVo change;
	/** 现场勘测 */
	private InstallSurveyVo survey;
	/** 签订合同 */
	private InstallContractVo contract;
	/** 签订合同 */
	private InstallAmountVo amount;
	/** 仓库出货 */
	private InstallShipmentVo shipment;
	/** 工程验收 */
	private InstallAcceptanceVo acceptance;
	/** 用户档案 */
	private List<InstallUserVo> users;

	public String getStatusText() {
		if (status != null) {
			return InstallStatus.getText(status);
		} else {
			return "";
		}
	}

	public String getAppStatusText() {
		if (appStatus != null && appStatus.equals("1")) {
			return "已预约";
		} else if (appStatus != null && appStatus.equals("2")) {
			return "已过期";
		} else {
			return "";
		}
	}

}
