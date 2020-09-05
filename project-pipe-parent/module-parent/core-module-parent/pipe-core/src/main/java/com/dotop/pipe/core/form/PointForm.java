package com.dotop.pipe.core.form;

import java.math.BigDecimal;

import com.dotop.smartwater.dependence.core.common.pipe.BasePipeForm;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class PointForm extends BasePipeForm {

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

	// 分割管道id
	private String breakPipeId;

	public PointForm() {
		super();
	}

	public PointForm(String pointId, String code, BigDecimal longitude, BigDecimal latitude) {
		super();
		this.pointId = pointId;
		this.code = code;
		this.longitude = longitude;
		this.latitude = latitude;
	}

}
