package com.dotop.smartwater.project.module.core.third.vo.pipe;

import java.util.Date;

import lombok.Data;

@Data
public class PipeDeviceVo {

	public PipeDeviceVo() {
		super();
	}

	public PipeDeviceVo(String deviceId, String code) {
		super();
		this.deviceId = deviceId;
		this.code = code;
	}

	// 主键
	private String deviceId;

	// 编码
	private String code;

	// 名字
	private String name;

	// 描述
	private String des;

	// 地址
	private String address;

	// 长度
	private String length;

	// 平均深度
	private String depth;

	// 管顶高程
	private String pipeElevation;

	// 地面高程
	private String groundElevation;

	// 敷设类型
	private PipeDictionaryVo laying;

	// 安装时间
	private Date installDate;

	// 备注
	private String remark;

	// 版本
	private String version;

	// 企业id
	private String enterpriseId;

	// 产品
	private PipeProductVo product;

}
