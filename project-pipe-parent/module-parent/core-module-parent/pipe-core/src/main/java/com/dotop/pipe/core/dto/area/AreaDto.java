package com.dotop.pipe.core.dto.area;

import com.dotop.smartwater.dependence.core.common.pipe.BasePipeDto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class AreaDto extends BasePipeDto {

	// 主键
	private String areaId;

	// 区域编号
	private String areaCode;

	// 区域名字
	private String name;

	// 区域描述
	private String des;

	// 片区颜色
	private String areaColorNum;

	// 比例尺
	private String scale;

	// 是否根节点(0:非叶子;1:非叶子)
	private Integer isLeaf;

	// 父节点
	private String parentCode;

	// 区域坐标的exten字段
	private String extent;

}
