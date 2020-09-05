package com.dotop.smartwater.project.auth.api;

import com.dotop.smartwater.dependence.core.common.BaseFactory;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.project.module.core.auth.form.RoleForm;
import com.dotop.smartwater.project.module.core.auth.form.UserForm;
import com.dotop.smartwater.project.module.core.auth.vo.*;

import java.util.List;

public interface IRoleFactory extends BaseFactory<RoleForm, RoleVo> {

	/**
	 * 
	 */
	void updateRolePermission(UserForm userLogin, String proleid);

	/**
	 * 
	 */
	RoleVo findRoleByName(RoleForm role);

	/**
	 * 
	 */
	Integer addRole(RoleForm role);

	/**
	 * 
	 */
	Integer editRole(RoleForm role);

	/**
	 * 
	 */
	RoleVo findRoleByNameAndId(RoleForm role);

	/**
	 * 
	 */
	Integer getCount(String roleid);

	/**
	 * 
	 */
	RoleVo findRoleById(String roleid);

	/**
	 * 
	 */
	Integer delRole(String roleid);

	/**
	 * 
	 */
	Pagination<RoleVo> getRoleList(RoleForm role);

	/**
	 *
	 * @param ids
	 * @return
	 */
	List<MenuVo> getMenus(List<String> ids);


	/**
	 * 
	 */
	List<MenuVo> getAdminMenuByParentid(String parentid);

	/**
	 * 
	 */
	List<MenuVo> getMenuByParentidAndProleid(String parentid, String proleId);

	/**
	 * 
	 */
	List<MenuVo> getMenuByRoleid(String roleid, String parentid);

	/**
	 * 
	 */
	List<PermissionVo> getPermissionByRoleidAndProleid(String roleid, String proleId);

	/**
	 * 
	 */
	Integer addRolePermission(String roleid, List<String> ids);

	/**
	 * 树格式加载权限
	 *
	 * @param roleParam
	 * @return
	 */
	List<AreaNodeVo> loadPermissionTree(RoleParamVo roleParam, String proleId);

}
