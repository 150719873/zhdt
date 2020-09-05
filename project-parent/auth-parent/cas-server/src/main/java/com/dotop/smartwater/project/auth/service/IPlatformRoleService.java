package com.dotop.smartwater.project.auth.service;

import java.util.List;
import java.util.Map;

import com.dotop.smartwater.dependence.core.common.BaseService;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.project.module.core.auth.bo.PlatformRoleBo;
import com.dotop.smartwater.project.module.core.auth.form.PlatFormPerForm;
import com.dotop.smartwater.project.module.core.auth.form.RoleAreaForm;
import com.dotop.smartwater.project.module.core.auth.vo.AreaNodeVo;
import com.dotop.smartwater.project.module.core.auth.vo.PermissionVo;
import com.dotop.smartwater.project.module.core.auth.vo.PlatformRoleVo;

/**

 * @date 2019年5月9日
 * @description
 */
public interface IPlatformRoleService extends BaseService<PlatformRoleBo, PlatformRoleVo> {

	/**
	 *
	 * @param platformRoleBo
	 * @return
	 */
	Pagination<PlatformRoleVo> getPlatformRoleList(PlatformRoleBo platformRoleBo);

	/**
	 *
	 * @param name
	 * @return
	 */
	PlatformRoleVo findPlatformRoleByName(String name);

	/**
	 *
	 * @param proleid
	 * @return
	 */
	PlatformRoleVo findPlatformRoleById(String proleid);

	/**
	 *
	 */
	int getCount(String proleid);

	/**
	 *
	 * @param proleid
	 */
	void delPlatformRole(String proleid);

	/**
	 *
	 * @param platformRoleBo
	 * @return
	 */
	PlatformRoleVo findPlatformRoleByNameAndId(PlatformRoleBo platformRoleBo);

	/**
	 *
	 * @param platformRoleBo
	 * @return
	 */
	PlatformRoleVo editPlatformRole(PlatformRoleBo platformRoleBo);

	/**
	 *
	 * @param roleid
	 * @return
	 */
	List<PermissionVo> getPermissionByRoleId(String roleid);

	/**
	 *
	 * @param proleid
	 * @return
	 */
	List<AreaNodeVo> findPermissionByProleid(String proleid);

	/**
	 *
	 * @param platFormPerForm
	 */
	void updatePermissionData(PlatFormPerForm platFormPerForm);

	/**
	 * @param roleAreaForm
	 * @return
	 */
	List<AreaNodeVo> loadRoleArea(RoleAreaForm roleAreaForm);

	/**
	 * @param roleAreaForm
	 */
	void updateRoleArea(RoleAreaForm roleAreaForm);

	/**
	 *
	 * @param type
	 * @return
	 */
	Map<String, AreaNodeVo> loadMenuDataByType(String type);
}
