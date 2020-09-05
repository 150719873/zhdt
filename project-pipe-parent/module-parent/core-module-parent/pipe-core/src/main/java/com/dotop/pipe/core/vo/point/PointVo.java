package com.dotop.pipe.core.vo.point;

import java.math.BigDecimal;
import java.util.Date;

import com.dotop.smartwater.dependence.core.common.pipe.BasePipeVo;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class PointVo extends BasePipeVo {

	// 所属企业
	private String enterpriseId;

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

	// 最后修改人
	private String lastBy;

	// 最后修改时间
	private Date lastDate;
	
	// private int sortcode;
	
	public int sortBycode() {
		return Integer.parseInt(getCode());
	}

	public PointVo() {
		super();
	}

	public PointVo(String pointId, String code) {
		super();
		this.pointId = pointId;
		this.code = code;
	}

	public PointVo(String pointId, String code, BigDecimal longitude, BigDecimal latitude) {
		super();
		this.pointId = pointId;
		this.code = code;
		this.longitude = longitude;
		this.latitude = latitude;
	}

}
