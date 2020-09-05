package com.dotop.smartwater.project.module.core.auth.vo;

import java.util.List;
import java.util.Map;

import com.dotop.smartwater.project.module.core.auth.vo.MenuVo;

import lombok.Data;

@Data
public class UserInfoVo {
	/** 用户信息 **/
	private UserParamVo userParamVo;
	/** 用户权限 **/
	private Map<String, MenuVo> permission;
	/** 系统头部菜单 **/
	private List<MenuVo> topMenuList;
	/** 水司具体信息 **/
	// private EnterpriseVo enterprise;
	private EnterpriseVo enterprise;
	/** 系统Logo图片 **/
	private String logoUrl;
	/** 系统别名 **/
	private String alias;
	/** 业务权限 **/
	private String businessPermission;

}
