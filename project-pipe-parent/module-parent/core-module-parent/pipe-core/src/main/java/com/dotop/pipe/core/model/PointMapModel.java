package com.dotop.pipe.core.model;

import com.dotop.pipe.auth.core.model.BaseEnterpriseModel;

import lombok.Data;
import lombok.EqualsAndHashCode;

//坐标与其他关系
@Data
@EqualsAndHashCode(callSuper = true)
public class PointMapModel extends BaseEnterpriseModel {

	// 主键
	private String mapId;

	// 坐标主键
	private String pointId;

	// 产品主键
	private String deviceId;

}
