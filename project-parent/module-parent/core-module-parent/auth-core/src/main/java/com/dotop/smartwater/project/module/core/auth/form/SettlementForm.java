package com.dotop.smartwater.project.module.core.auth.form;

import java.math.BigDecimal;

import com.dotop.smartwater.dependence.core.common.BaseForm;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class SettlementForm extends BaseForm {
	/* 企业名称 */
	private String alias;
	/* 是否开启自动月结 */
	private Integer status;
	/* 设备离线天数设置 */
	private Integer offday;
	/* 设备过期预警天数 */
	private Integer alarmday;
	/* 最多预约天数 */
	private Integer appointmentDay;
	/* 每天最多预约个数 */
	private Integer appointmentNumber;
	/**
	 * yangke扩展 iot配置信息
	 */
	// iot账号
	private String iotAccount;
	// iot密码
	private String iotPassword;
	// appEUI
	private String appEui;
	// 是否开启余额不足通知 1-提醒 0-不提醒
	private Integer isNotice;
	// 设置余额提醒数值
	private BigDecimal balance;
	// 余额提醒短信模板ID
	private String smsTemplate;
	// 设置欠费关阀
	private Integer arrearsValve;
	// 最大欠费金额
	private BigDecimal arrearsBalance;
	// 设置欠费关阀短信通知
	private Integer arrearsValveSms;
	// 欠费关阀短信通知模板
	private String valveSmsTemplate;
	
	/** 是否显示帮助文档 1-显示 0-不显示*/
	private Integer isHelp;
	/**默认天数*/
	private Integer defaultDay;
	
}
