package com.dotop.pipe.core.model;

import java.util.Date;

import com.dotop.pipe.auth.core.model.BaseEnterpriseModel;

import lombok.Data;
import lombok.EqualsAndHashCode;

// 设备属性日志 
@Data
@EqualsAndHashCode(callSuper = true)
public class DevicePropertyLogModel extends BaseEnterpriseModel {

	// 主键
	private String logId;

	// 设备主键
	private String deviceId;

	// 设备编号
	private String deviceCode;

	// 表字段名
	private String field;

	// 传输协议TAG
	private String tag;

	// 名称
	private String name;

	// 单位
	private String unit;

	// 值
	private String val;

	// 设备发送时间
	private Date devSendDate;

	// 服务器接收时间
	private Date serReceDate;

	// 分区字段(yyyyMMddHHmmss)与createDate同
	private String ctime;

}
