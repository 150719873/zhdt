package com.dotop.smartwater.project.auth.dao;

import java.util.List;
import java.util.Set;

import com.dotop.smartwater.project.module.core.auth.vo.AreaNodeVo;
import org.apache.ibatis.annotations.Param;

import com.dotop.smartwater.dependence.core.common.BaseDao;
import com.dotop.smartwater.project.module.core.auth.dto.RoleDto;
import com.dotop.smartwater.project.module.core.auth.vo.MenuVo;
import com.dotop.smartwater.project.module.core.auth.vo.PermissionVo;
import com.dotop.smartwater.project.module.core.auth.vo.RoleVo;

/**
 * 

 * @date 2019年5月9日
 * @description
 */
public interface IRoleDao extends BaseDao<RoleDto, RoleVo> {

	/**
	 * 
	 */
	@Override
	List<RoleVo> list(RoleDto roleDto);

	/**
	 * 
	 */
	int addRole(RoleDto role);

	/**
	 * 
	 */
	int editRole(RoleDto role);

	/**
	 * 
	 */
	int delRole(String roleid);

	/**
	 * 
	 */
	RoleVo findRoleById(@Param("roleid") String roleid);

	/**
	 * 
	 */
	List<RoleVo> getRoleList(RoleDto role);

	/**
	 * 
	 */
	RoleVo findRoleByName(RoleDto role);

	/**
	 * 
	 */
	RoleVo findRoleByNameAndId(RoleDto role);

	/**
	 * 
	 */
	List<MenuVo> getAdminMenuByParentid(@Param("parentid") String parentid);

	/**
	 * 
	 */
	List<MenuVo> getAdminTreeByModelid(@Param("modelid") String modelid);

	/**
	 * 
	 */
	List<MenuVo> getMenuByParentid(@Param("parentid") String parentid, @Param("proleid") String proleid);

	/**
	 * 
	 */
	List<MenuVo> getMenuByModelidAndProleid(@Param("modelid") String parentid, @Param("proleid") String proleid);

	/**
	 * 
	 */
	List<MenuVo> getMenuByRoleid(@Param("roleid") String roleid, @Param("parentid") String parentid);

	/**
	 * 
	 */
	List<MenuVo> getMenuByRoleidAndModelid(@Param("roleid") String roleid, @Param("modelid") String modelid);


	List<MenuVo> getMenus(@Param("menuids") List<String> menuids);
	/**
	 * 
	 */
	Set<String> getPsidByRoleId(@Param("roleid") String roleid);

	/**
	 * 
	 */
	int delRolePermission(String roleid);

	/**
	 *
	 * @param permissionid
	 * @return
	 */
	int delPermission(@Param("permissionid")String permissionid);
	/**
	 * 
	 */
	int addRolePermission(@Param("roleid") String roleid, @Param("list") List<String> permissionids);

	/**
	 * 
	 */
	List<PermissionVo> getPermissionByRoleidAndProleid(@Param("roleid") String roleid,
			@Param("proleid") String proleid);

	/**
	 * 
	 */
	int getCount(@Param("roleid") String roleid);

	/**
	 *
	 * @param roleid
	 * @return
	 */
	List<AreaNodeVo> findPermissionByRoleid(@Param("roleid")String roleid, @Param("proleid") String proleid);
}
