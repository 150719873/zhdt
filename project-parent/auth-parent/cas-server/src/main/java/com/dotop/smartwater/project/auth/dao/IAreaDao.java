package com.dotop.smartwater.project.auth.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;

import com.dotop.smartwater.project.module.core.auth.dto.AreaDto;
import com.dotop.smartwater.project.module.core.auth.vo.AreaNodeVo;
import com.dotop.smartwater.project.module.core.auth.vo.AreaVo;
import com.dotop.smartwater.project.module.core.auth.vo.PermissionVo;
import org.springframework.stereotype.Component;

/**
 * 

 * @date 2019年5月9日
 * @description
 */
@Component
public interface IAreaDao {

	/**
	 * 
	 */
	int addArea(@Param("enterpriseid") String enterpriseid, @Param("list") List<AreaVo> list);
	
	/**
	 * 新增区域节点
	 */
	int insertAreaNode(AreaDto areaDto);
	
	/**
	 * 修改区域节点
	 */
	int updateAreaNode(AreaDto areaDto);
	
	/**
	 * 删除区域节点
	 */
	int deleteAreaNode(AreaDto areaDto);
	
	/**
	 * 判断某节点下是否存在子节点
	 * @param areaDto
	 * @return
	 */
	int checkAreaChild(AreaDto areaDto);
	
	/**
	 * 校验区域编号唯一
	 * @param areaDto
	 * @return
	 */
	int checkAreaCode(AreaDto areaDto);

	/**
	 * 
	 */
	int delAreaByEid(@Param("enterpriseid") String enterpriseid);

	/**
	 * 
	 */
	List<AreaVo> findAreaNodesByEid(@Param("enterpriseid") String enterpriseid);

	/**
	 * 
	 */
	AreaVo getAreaById(@Param("id") String id);

	/**
	 * 
	 */
	List<PermissionVo> findAreasByEidAndUseId(@Param("enterpriseid") String enterpriseid,
			@Param("userid") String userid);

	/**
	 * 验证该节点是否被用户使用
	 */
	long isUsedNode(AreaDto area);
	
	/**
	 * 验证该节点下是否存在报表
	 */
	long hasBookReport(AreaDto area);

	/**
	 * 
	 */
	@MapKey("key")
	Map<String, AreaNodeVo> getAreaMapByEid(@Param("enterpriseid") String enterpriseid);

	/**
	 * 
	 */
	@MapKey("key")
	Map<String, AreaNodeVo> getAreaMapByUserid(@Param("userid") String userid);
}
