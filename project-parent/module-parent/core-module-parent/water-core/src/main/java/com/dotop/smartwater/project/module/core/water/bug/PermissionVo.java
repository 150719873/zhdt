package com.dotop.smartwater.project.module.core.water.bug;

import com.dotop.smartwater.dependence.core.common.BaseVo;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * TODO 与Permission_有什么区别，请联系 同role_permission
 *
 */
// 表存在
@Data
@EqualsAndHashCode(callSuper = false)
public class PermissionVo extends BaseVo {

	private Long permissionid;
	private String name;

	private String cname;

	private Integer isselect;

}