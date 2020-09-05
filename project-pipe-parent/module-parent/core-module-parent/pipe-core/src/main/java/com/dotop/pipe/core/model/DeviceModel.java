package com.dotop.pipe.core.model;

import java.util.Date;

import com.dotop.pipe.auth.core.model.BaseEnterpriseModel;

import lombok.Data;
import lombok.EqualsAndHashCode;

// 设备表 
@Data
@EqualsAndHashCode(callSuper = true)
public class DeviceModel extends BaseEnterpriseModel {

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
	private String laying;
	// 安装时间
	private Date installDate;
	// 备注
	private String remark;
	// 版本
	private String version;
	// 产品主键
	private String productId;
	// 区域主键
	private String areaId;
}
