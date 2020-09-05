package com.dotop.smartwater.project.auth.service;

import java.util.List;

import com.dotop.smartwater.dependence.core.common.BaseService;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.project.module.core.auth.bo.AreaBo;
import com.dotop.smartwater.project.module.core.auth.bo.UserBo;
import com.dotop.smartwater.project.module.core.auth.vo.AreaListVo;
import com.dotop.smartwater.project.module.core.auth.vo.AreaVo;
import com.dotop.smartwater.project.module.core.auth.vo.PermissionVo;
import com.dotop.smartwater.project.module.core.auth.vo.UserVo;

/**
 * 

 * @date 2019年5月9日
 * @description
 */
public interface IAreaService extends BaseService<UserBo, UserVo> {

	/**
	 * 
	 */
	String getMaxId() throws FrameworkRuntimeException;

	/**
	 * 
	 */
	List<AreaVo> loadCompanyArea(String enterpriseid) throws FrameworkRuntimeException;

	/**
	 * 
	 */
	int saveCompanyArea(AreaListVo areaVo) throws FrameworkRuntimeException;
	
	/**
	 *新增区域节点 
	 */
	Integer insertAreaNode(AreaBo areaBo) throws FrameworkRuntimeException;
	
	/**
	 *修改区域节点 
	 */
	Integer updateAreaNode(AreaBo areaBo) throws FrameworkRuntimeException;
	
	/**
	 *删除区域节点 
	 */
	Integer deleteAreaNode(AreaBo areaBo) throws FrameworkRuntimeException;

	/**
	 * 
	 */
	List<PermissionVo> findAreasByEidAndUseId(String enterpriseid, String userid) throws FrameworkRuntimeException;

	/**
	 * 
	 */
	String isUsedNode(AreaVo area) throws FrameworkRuntimeException;
}
