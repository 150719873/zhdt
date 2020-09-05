package com.dotop.smartwater.project.module.core.water.bo;

import java.util.List;

import com.dotop.smartwater.dependence.core.common.BaseBo;

import lombok.Data;
import lombok.EqualsAndHashCode;

// 表存在
@Data
@EqualsAndHashCode(callSuper = false)
public class MenuBo extends BaseBo {
	private String menuid;

	private String parentid;

	private String name;

	private String uri;

	private String iconuri;

	private String introduction;

	private Integer level;

	private Integer type;

	private Integer isselect;

	private List<MenuBo> child;

	private String modelid;

	private String systype;
	// 顶级路由标识
    private String routenode;
}