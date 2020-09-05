package com.dotop.smartwater.project.module.core.water.form;

import java.util.List;

import com.dotop.smartwater.dependence.core.common.BaseForm;

import lombok.Data;
import lombok.EqualsAndHashCode;

// 表存在
@Data
@EqualsAndHashCode(callSuper = false)
public class MenuForm extends BaseForm {
	private String menuid;

	private String parentid;

	private String name;

	private String uri;

	private String iconuri;

	private String introduction;

	private Integer level;

	private Integer type;

	private Integer isselect;

	private List<MenuForm> child;
	// 顶级路由标识
    private String routenode;
}