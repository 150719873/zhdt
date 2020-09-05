package com.dotop.smartwater.project.auth.api;

import java.util.List;

import com.dotop.smartwater.dependence.core.common.BaseFactory;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.project.module.core.auth.form.AreaForm;
import com.dotop.smartwater.project.module.core.auth.form.UserForm;
import com.dotop.smartwater.project.module.core.auth.vo.AreaListVo;
import com.dotop.smartwater.project.module.core.auth.vo.AreaVo;
import com.dotop.smartwater.project.module.core.auth.vo.PermissionVo;
import com.dotop.smartwater.project.module.core.auth.vo.UserVo;

/**
 * 

 * @date 2019年5月9日
 * @description
 */
public interface IAreaFactory extends BaseFactory<UserForm, UserVo> {

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
	Integer saveCompanyArea(AreaListVo areaVo) throws FrameworkRuntimeException;
	
	/**
	 * 新增区域节点
	 */
	Integer insertAreaNode(AreaForm areaForm) throws FrameworkRuntimeException;
	
	/**
	 * 修改区域节点
	 */
	Integer updateAreaNode(AreaForm areaForm) throws FrameworkRuntimeException;
	
	/**
	 * 删除区域节点
	 */
	Integer deleteAreaNode(AreaForm areaForm) throws FrameworkRuntimeException;

	/**
	 * 
	 */
	String isUsedNode(AreaVo area) throws FrameworkRuntimeException;

	/**
	 * 
	 */
	List<PermissionVo> findAreasByEidAndUseId(String enterpriseid, String userid) throws FrameworkRuntimeException;
}
