package com.dotop.pipe.data.receiver.core.model;

import java.util.Date;

import lombok.Data;

@Data
public class InfoRD {

	// 传感器编号
	private String code;

	// 类型
	private String type;

	// 厂家
	private String factory;

	// 版本
	private String version;

	// 描述
	private String des;

	// 设备发送时间
	private Date devSendDate;

	// 服务器接收时间
	private Date serReceDate;

	// 数据插入创建时间
	private Date createDate;

}
