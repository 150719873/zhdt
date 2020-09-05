package com.dotop.pipe.data.receiver.core.model;

import lombok.Data;

@Data
public class PropertyRD {

	// 表字段名
	private String field;
	// 传输协议TAG
	private String tag;
	// 名称
	private String name;
	// 类型
	private String type;
	// 上限阀值
	private String upVal;
	// 下限阀值
	private String downVal;
	// 描述
	private String des;
	// 单位
	private String unit;

	// 数值(采集数据设置)
	private String val;
}
