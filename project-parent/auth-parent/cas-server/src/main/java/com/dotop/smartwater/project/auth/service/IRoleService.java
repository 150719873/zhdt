package com.dotop.smartwater.project.auth.service;

import com.dotop.smartwater.dependence.core.common.BaseService;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.project.module.core.auth.bo.RoleBo;
import com.dotop.smartwater.project.module.core.auth.bo.UserBo;
import com.dotop.smartwater.project.module.core.auth.vo.AreaNodeVo;
import com.dotop.smartwater.project.module.core.auth.vo.MenuVo;
import com.dotop.smartwater.project.module.core.auth.vo.PermissionVo;
import com.dotop.smartwater.project.module.core.auth.vo.RoleVo;

import java.util.List;

/**

 * @date 2019年5月9日
 * @description
 */
public interface IRoleService extends BaseService<RoleBo, RoleVo> {

	/**
	 *
	 */
	int addRole(RoleBo role);

	/**
	 *
	 */
	int editRole(RoleBo role);

	/**
	 *
	 */
	int delRole(String roleid);

	/**
	 *
	 */
	int getCount(String roleid);

	/**
	 *
	 */
	RoleVo findRoleById(String roleid);

	/**
	 *
	 */
	Pagination<RoleVo> getRoleList(RoleBo role);

	/**
	 *
	 */
	RoleVo findRoleByName(RoleBo role);

	/**
	 *
	 */
	RoleVo findRoleByNameAndId(RoleBo role);

	/**
	 * 单个公司管理员菜单获取
	 */
	List<MenuVo> getMenuByParentidAndProleid(String parentid, String proleid);

	/**
	 *
	 */
	List<MenuVo> getMenuByModelidAndProleid(String modelid, String proleid);

	/**
	 * 全局管理员菜单获取
	 */
	List<MenuVo> getAdminMenuByParentid(String parentid);

	/**
	 *
	 */
	List<MenuVo> getAdminTreeByModelid(String modelid);

	/**
	 * 普通角色用户菜单
	 */
	List<MenuVo> getMenuByRoleid(String roleid, String parentid);

	/**
	 *
	 * @param ids
	 * @return
	 */
	List<MenuVo> getMenus(List<String> ids);

	/**
	 *
	 */
	List<MenuVo> getMenuByRoleidAndModelid(String roleid, String modelid);

	/**
	 * 更新角色权限
	 */
	void updateRolePermission(UserBo user, String proleid);

	/**
	 *
	 */
	int addRolePermission(String roleid, List<String> ids);

	/**
	 * 获取角色权限点
	 */
	List<PermissionVo> getPermissionByRoleidAndProleid(String roleid, String proleid);

	/**
	 * @param roleid
	 * @return
	 */
	List<AreaNodeVo> findPermissionByRoleid(String roleid, String proleId);

}
