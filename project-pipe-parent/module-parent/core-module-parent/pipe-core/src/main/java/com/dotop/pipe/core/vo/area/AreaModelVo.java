package com.dotop.pipe.core.vo.area;

import com.dotop.pipe.core.vo.point.PointVo;
import com.dotop.smartwater.dependence.core.common.pipe.BasePipeVo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
public class AreaModelVo extends BasePipeVo {

	private String areaId;
	private String areaCode;
	private String name;
	private String des;
	private String parentCode;
	private Integer isLeaf;
	private Integer isParent;
	private String enterpriseId;
	private String enterpriseName;
	private Date createDate;
	private String areaColorNum;
	private String scale;
	// 地图区域描边 点的集合
	private List<PointVo> points;
	// 区域坐标的extent字段
	private String extent;

	// 通信协议
	private List<String> protocols;

}
