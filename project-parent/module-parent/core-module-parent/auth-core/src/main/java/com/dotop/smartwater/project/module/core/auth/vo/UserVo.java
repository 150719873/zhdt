package com.dotop.smartwater.project.module.core.auth.vo;

import com.dotop.smartwater.dependence.core.common.BaseVo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
import java.util.Set;

@Data
@EqualsAndHashCode(callSuper = false)
public class UserVo extends BaseVo {

	public static final int USER_TYPE_ADMIN = 0;// 最高级系统管理员
	public static final int USER_TYPE_ADMIN_ENTERPRISE = 1;// 水司最高级系统管理员
	public static final int USER_TYPE_ENTERPRISE_NORMAL = 2;// 水司普通用户
	
	public static final int USER_FAILURESTATE_VALID = 0;//有效
	public static final int USER_FAILURESTATE_INVALID = 1;//失效
	
	public static final String USER_BINDPREMISSION_PRODUCT = "product"; //绑定权限 生产
	public static final String USER_BINDPREMISSION_SAMPLE = "sample"; //绑定权限 样品

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
	
	//水司名称
	private String enterpriseName;

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
	//失效日期
	private Date failuretime;
	//失效状态
	private Integer failurestate;
	
	private String createuser;
	
	private String createName;
	
	private String accounttype;

	private EnterpriseVo enterprise;

	private Set<String> permissions;

	private SettlementVo settlement;

	private String ticket;
	//默认系统选择
	private String syschoice;
	//绑定权限
	private String bindpermission;
}
