package com.dotop.smartwater.project.module.core.auth.dto;

import java.util.Date;
import java.util.Set;

import com.dotop.smartwater.dependence.core.common.BaseDto;
import com.dotop.smartwater.project.module.core.auth.bo.EnterpriseBo;
import com.dotop.smartwater.project.module.core.auth.bo.SettlementBo;
import com.dotop.smartwater.project.module.core.auth.bo.EnterpriseBo;
import com.dotop.smartwater.project.module.core.auth.bo.SettlementBo;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class UserDto extends BaseDto {
	private String userid;

	private String account;

	private String name;

	private String description;

	private String password;

	/**
	 * 工号
	 */
	private String worknum;

	/**
	 * 用户类型，0所有，1水务，2工程
	 */
	private Integer usertype;

	private Integer type;

	private String enterpriseid;

	private String roleid;

	private String eid;

	/** 代理字段-roleid 用户前端接收数据 */
	private String website;

	private String rid;

	private Integer status;

	private String phone;

	private String email;

	private String address;

	private Date createtime;

	private Date failuretime;
	//失效状态
	private Integer failurestate;

	private String createuser;

	private EnterpriseBo enterprise;

	private Set<String> permissions;

	private SettlementBo settlement;
	
	//默认系统选择
	private String syschoice;
	//绑定权限
	private String bindpermission;
}
