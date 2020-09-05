package com.dotop.pipe.core.vo.device;

import lombok.Data;

/**
 * 设备上传数据属性类
 * 
 *
 *
 */
@Data
public class DevicePropertyFieldVo {

	private String name;
	private String field;
	private String tag;
	private String unit;

	DevicePropertyFieldVo() {

	}

	public DevicePropertyFieldVo(String name, String field, String tag, String unit) {
		this.name = name;
		this.field = field;
		this.tag = tag;
		this.unit = unit;
	}

}
