package com.dotop.smartwater.project.module.core.water.bug;

import com.dotop.smartwater.dependence.core.common.BaseVo;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * TODO 与PermissionVo有什么区别，请联系 同role_permission
 *
 */
// 表存在
@Data
@EqualsAndHashCode(callSuper = false)
public class Permission_ extends BaseVo {

	public final static int TYPE_MENU = 1;
	public final static int TYPE_BUTTON = 2;

	private Long permissionid;

	private Long parentid;

	private Long menuid;

	private String name;

	private Integer type;

	private String uri;

	private String alias;

}