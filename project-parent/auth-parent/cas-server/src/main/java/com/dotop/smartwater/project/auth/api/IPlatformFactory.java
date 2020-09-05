package com.dotop.smartwater.project.auth.api;

import java.util.List;
import java.util.Map;

import com.dotop.smartwater.dependence.core.common.BaseFactory;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.project.module.core.auth.form.PlatFormPerForm;
import com.dotop.smartwater.project.module.core.auth.form.PlatformRoleForm;
import com.dotop.smartwater.project.module.core.auth.form.RoleAreaForm;
import com.dotop.smartwater.project.module.core.auth.vo.AreaNodeVo;
import com.dotop.smartwater.project.module.core.auth.vo.PermissionVo;
import com.dotop.smartwater.project.module.core.auth.vo.PlatformRoleVo;
import com.dotop.smartwater.project.module.core.auth.vo.UserVo;

/**

 */
public interface IPlatformFactory extends BaseFactory<PlatformRoleForm, PlatformRoleVo> {

	/**
	 *
	 */
	Pagination<PlatformRoleVo> getPlatformRoleList(PlatformRoleForm platformRoleForm);


	/**
	 * 加载权限
	 *
	 * @param platformRoleForm
	 * @return
	 */
	List<PermissionVo> loadPermissionData(PlatformRoleForm platformRoleForm);

	/**
	 * 树格式加载权限
	 *
	 * @param platformRoleForm
	 * @return
	 */
	List<AreaNodeVo> loadPermissionTree(PlatformRoleForm platformRoleForm);

	/**
	 *
	 */
	void updatePermissionData(PlatFormPerForm platFormPerForm, UserVo user);

	/**
	 * 加载角色区域树
	 *
	 * @param roleAreaForm
	 * @return
	 */
	List<AreaNodeVo> loadRoleAreaTree(RoleAreaForm roleAreaForm);

	/**
	 * 编辑角色区域
	 *
	 * @param roleAreaForm
	 * @param user
	 */
	void updateRoleArea(RoleAreaForm roleAreaForm, UserVo user);

	/**
	 *
	 */
	Map<String, AreaNodeVo> loadMenuDataByType(Integer type);

}
