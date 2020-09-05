package com.dotop.pipe.core.dto.third;

import com.dotop.smartwater.dependence.core.common.pipe.BasePipeDto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ThirdMapDto extends BasePipeDto {

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

	// 厂商编码
	private String factoryCode;

	// 设备类型
	private String deviceType;
	/**
	 * 厂商名称
	 */
	private String factoryName;
	/**
	 * 通信协议
 	 */
	private String protocol;
}
