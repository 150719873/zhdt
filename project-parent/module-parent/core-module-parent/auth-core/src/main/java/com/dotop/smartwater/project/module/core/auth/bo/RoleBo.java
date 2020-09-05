package com.dotop.smartwater.project.module.core.auth.bo;

import com.dotop.smartwater.dependence.core.common.BaseBo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 同role
 * 
 * 合并 旧原名 RoleParam
 */
// 表存在
@Data
@EqualsAndHashCode(callSuper = false)
public class RoleBo extends BaseBo {
	private String roleid;

	private String name;

	private String description;

	private Date createtime;

	private String createuser;

	private String permissionids;

	private String parentid;

}