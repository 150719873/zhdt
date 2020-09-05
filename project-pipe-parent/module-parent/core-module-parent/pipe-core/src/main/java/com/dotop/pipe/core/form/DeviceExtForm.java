package com.dotop.pipe.core.form;

import java.util.List;

import com.dotop.smartwater.dependence.core.common.pipe.BasePipeForm;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class DeviceExtForm extends BasePipeForm {

	// Id
	private String deviceId;
	// 编号
	private String code;

	// 名称
	private String name;

	// 描述
	private String des;

	// 长度
	private String length;

	// 坐标
	private List<PointForm> points;

	
}
