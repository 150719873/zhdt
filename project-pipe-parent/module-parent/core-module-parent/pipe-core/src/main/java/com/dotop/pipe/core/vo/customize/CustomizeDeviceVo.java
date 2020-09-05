package com.dotop.pipe.core.vo.customize;

import java.util.Date;
import java.util.List;

import com.dotop.pipe.core.vo.area.AreaModelVo;
import com.dotop.pipe.core.vo.point.PointVo;
import com.dotop.pipe.core.vo.product.ProductVo;
import com.dotop.smartwater.dependence.core.common.pipe.BasePipeVo;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class CustomizeDeviceVo extends BasePipeVo {

	// 节点Id
	private String customizeId;

	// 节点编号
	private String code;

	// 节点名称
	private String name;

	// 节点描述
	private String des;

	// 节点地址
	private String address;

	// 区域id
	private String areaId;

	// 占地面积
	private String acreage;

	// 颜色
	private String color;

	// 类型

	private String type;

	// 产品
	private ProductVo product;

	// 坐标
	private AreaModelVo area;

	// 最后修改人
	private String lastBy;

	// 最后修改时间
	private Date lastDate;

	// 所属区域
	private String areaName;

	// 点集合
	private  List<PointVo> points;

}
