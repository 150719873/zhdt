package com.dotop.smartwater.project.auth.dao;

import com.dotop.smartwater.project.module.core.auth.vo.CheckboxOptionVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**

 * @date 2019/8/7.
 */
public interface IDataPermissionDao {

	/**
	 * 加载数据权限数据
	 * @param roleId
	 * @return
	 */
	List<CheckboxOptionVo> loadPermissionByRoleId(@Param("roleId") String roleId);

	/**
	 * 删除权限
	 * @param roleId
	 */
	void deletePermissionByRoleId(@Param("roleId") String roleId);

	/**
	 * 添加权限
	 * @param roleId
	 * @param permissionids
	 * @return
	 */
	int addPermission(@Param("roleId") String roleId, @Param("list") List<String> permissionids);
}
