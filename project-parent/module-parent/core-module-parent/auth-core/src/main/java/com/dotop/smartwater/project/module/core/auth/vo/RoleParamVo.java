package com.dotop.smartwater.project.module.core.auth.vo;

import java.util.List;

import com.dotop.smartwater.dependence.core.common.BaseVo;

import lombok.Data;
import lombok.EqualsAndHashCode;
@Data
@EqualsAndHashCode(callSuper = false)
public class RoleParamVo extends BaseVo {
	private String roleid;

	private List<String> permissionids;
}
