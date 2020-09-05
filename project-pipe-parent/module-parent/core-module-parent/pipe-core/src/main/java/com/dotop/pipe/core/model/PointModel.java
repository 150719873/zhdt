package com.dotop.pipe.core.model;

import java.math.BigDecimal;

import com.dotop.pipe.auth.core.model.BaseEnterpriseModel;

import lombok.Data;
import lombok.EqualsAndHashCode;

// 坐标
@Data
@EqualsAndHashCode(callSuper = true)
public class PointModel extends BaseEnterpriseModel {

	// 主键
	private String pointId;

	// 坐标编号
	private String code;

	// 坐标名字
	private String name;

	// 坐标描述
	private String des;

	// 经度
	private BigDecimal longitude;

	// 纬度
	private BigDecimal latitude;

	// 备注
	private String remark;

}
