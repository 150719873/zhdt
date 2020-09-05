package com.dotop.smartwater.project.auth.dao;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.dotop.smartwater.project.module.core.auth.form.RoleAreaForm;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;
import org.springframework.dao.DataAccessException;

import com.dotop.smartwater.project.module.core.auth.dto.PlatformRoleDto;
import com.dotop.smartwater.project.module.core.auth.vo.AreaNodeVo;
import com.dotop.smartwater.project.module.core.auth.vo.PermissionVo;
import com.dotop.smartwater.project.module.core.auth.vo.PlatformRoleVo;

/**

 */
public interface IPlatformRoleDao {

	/**
	 * 
	 */
	PlatformRoleVo selectById(@Param("proleid") String proleid);

	/**
	 * 
	 */
	void deleteById(@Param("proleid") String proleid);

	/**
	 * 
	 */
	int insert(PlatformRoleDto platformRole);

	/**
	 * 
	 */
	int updateById(PlatformRoleDto platformRole);

	/**
	 * 
	 */
	List<PlatformRoleVo> getPlatformRoleList(PlatformRoleDto platformRoleDto);

	/**
	 * 
	 */
	int getCount(@Param("proleid") String proleid);

	/**
	 * 
	 */
	PlatformRoleVo findPlatformRoleByName(@Param("name") String name);


	/**
	 *
	 * @param proleid
	 * @return
	 */
	List<PermissionVo> getPermissionByRoleId(@Param("proleid")String proleid);

	/**
	 * 
	 */
	List<AreaNodeVo> findPermissionByProleid(@Param("proleid") String proleid);

	/**
	 * 
	 */
	void delPlatformRolePermissionByProleid(@Param("proleid") String proleid);

	/**
	 *
	 * @param menuid
	 */
	void delPlatformRolePermission(@Param("menuid") String menuid);

	/**
	 * 
	 */
	int addPlatformRolePermission(@Param("proleid") String proleid, @Param("list") List<String> menuids)
			throws DataAccessException;

	/**
	 * 加载角色区域数据
	 * @param roleAreaForm
	 * @return
	 */
	List<AreaNodeVo> loadRoleArea(RoleAreaForm roleAreaForm);

	/**
	 * 删除角色区域数据
	 * @param roleId
	 */
	void deleteRoleArea(@Param("roleId") String roleId);

	/**
	 * 添加角色区域
	 * @param roleId
	 * @param areaIds
	 * @return
	 */
	int addRoleArea(@Param("roleId") String roleId, @Param("list") List<String> areaIds);
	/**
	 * 
	 */
	Set<String> findMenuidsByProleId(@Param("proleid") String proleid);

	/**
	 * 
	 */
	PlatformRoleVo findPlatformRoleByNameAndProleid(PlatformRoleDto platformRoleDto);

	/**
	 * 
	 */
	@MapKey("key")
	Map<String, AreaNodeVo> loadMenuDataByType(@Param("type") String type);
}
