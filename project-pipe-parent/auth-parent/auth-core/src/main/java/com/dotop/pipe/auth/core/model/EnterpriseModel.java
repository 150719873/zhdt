package com.dotop.pipe.auth.core.model;

import java.math.BigDecimal;

import com.dotop.smartwater.dependence.core.common.RootModel;

import lombok.Data;
import lombok.EqualsAndHashCode;

// 与cas的企业对于关联
@Data
@EqualsAndHashCode(callSuper = true)
public class EnterpriseModel extends RootModel {

	// 主键
	private String enterpriseId;

	// 企业名
	private String enterpriseName;

	// 关联cas企业
	private String eid;

	// 经度
	private BigDecimal longitude;

	// 纬度
	private BigDecimal latitude;

	// 备注
	private String remark;
}
