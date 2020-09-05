package com.dotop.smartwater.project.module.core.water.enums;

import lombok.Getter;

/**
 * 操作日志类型枚举
 *

 * @date 2019/7/12.
 */
@Getter
public enum OperateTypeEnum {

	/**
	 * 用户档案
	 */
	PAYMENT_LOG("营业厅功能"),
	USER_FILE("用户档案"),
	METER_MANAGEMENT("水表管理"),
	HISTORY_QUERY("历史数据查询"),
	EQUIPMENT_CALIBRATION("设备校准"),
	METER_READING_TASK("抄表任务"),
	PRODUCT_MANAGEMENT("产品管理"),
	STORAGE_MANAGEMENT("库存管理"),
	ARTIFICIAL_METER_READING("paymentTradeOrderForm"),
	INSTALL_TEMPLATE_MANAGEMENT("报装模板管理"),
	INSTALL_APPOINTMENT_MANAGEMENT("报装预约管理"),
	DEVICE_WARNING_SETTING("预警通知设置"),
	ROLE_MANAGEMENT("角色管理"),
	USER_ACCOUNT("用户账号"),
	PLATFORM_ROLE("平台角色"),
	MENU_MANAGEMENT("菜单管理"),
	AREA_MANAGEMENT("区域管理"),
	OPERATIONS_LOG("运维日志"),
	APPVERSION_CONTROl("APP版控"),
	NOTICE_MANAGEMENT("通知管理"),
	MESSAGE_CENTER("消息中心"),
	REPORT_DESIGN("报表设计"),
	DEVICE_MIGRATION("设备迁移");
	/**
	 * 类型名称
	 */
	private String value;

	OperateTypeEnum(String value) {
		this.value = value;
	}
}
