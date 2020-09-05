package com.dotop.smartwater.project.module.core.auth.vo;

import java.util.Set;

import com.dotop.smartwater.dependence.core.common.BaseVo;

import lombok.Data;
import lombok.EqualsAndHashCode;
@Data
@EqualsAndHashCode(callSuper = false)
public class RolePermissionVo extends BaseVo {
	private String roleid;
	private Set<String> permissions;
}
