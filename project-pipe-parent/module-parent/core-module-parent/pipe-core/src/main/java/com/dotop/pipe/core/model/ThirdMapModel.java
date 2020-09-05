package com.dotop.pipe.core.model;

import com.dotop.pipe.auth.core.model.BaseEnterpriseModel;

import lombok.Data;
import lombok.EqualsAndHashCode;

//第三方与传感器关系
@Data
@EqualsAndHashCode(callSuper = true)
public class ThirdMapModel extends BaseEnterpriseModel {

	// 主键
	private String mapId;

	// 设备主键
	private String deviceId;

	// 设备编码
	private String deviceCode;

	// 第三方主键
	private String thirdId;

	// 第三方编码
	private String thirdCode;
}
