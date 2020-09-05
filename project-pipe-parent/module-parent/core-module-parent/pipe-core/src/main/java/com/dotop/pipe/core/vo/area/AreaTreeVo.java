package com.dotop.pipe.core.vo.area;

import java.util.List;

import lombok.Data;

@Data
public class AreaTreeVo {

	private String title;
	private String key;
	private String isLeaf;
	private String disabled;
	private Boolean expanded;
	private Boolean selected;
	private String areaCode;
	private String areaColorNum;
	private String scale;
	private String des;
	private Integer isParent;
	private List<AreaTreeVo> children;
	private String extent;

}
