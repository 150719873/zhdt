package com.dotop.smartwater.project.auth.service;

import java.util.List;
import java.util.Map;

import com.dotop.smartwater.dependence.core.common.BaseService;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.project.module.core.auth.bo.UserBo;
import com.dotop.smartwater.project.module.core.auth.vo.AreaNodeVo;
import com.dotop.smartwater.project.module.core.auth.vo.AreaVo;
import com.dotop.smartwater.project.module.core.auth.vo.EnterpriseVo;
import com.dotop.smartwater.project.module.core.auth.vo.MenuVo;
import com.dotop.smartwater.project.module.core.auth.vo.SettlementVo;
import com.dotop.smartwater.project.module.core.auth.vo.UserVo;
import com.dotop.smartwater.project.module.core.auth.vo.select.Obj;

/**
 * 

 * @date 2019年5月9日
 * @description
 */
public interface IInfService extends BaseService<UserBo, UserVo> {

	/**
	 * 
	 */
	List<EnterpriseVo> getErpList() throws FrameworkRuntimeException;

	/**
	 * 
	 */
	Map<String, EnterpriseVo> getEnterpriseMap() throws FrameworkRuntimeException;

	/**
	 * 
	 */
	Map<String, MenuVo> getPermissions(UserVo user, String modelId) throws FrameworkRuntimeException;

	/**
	 * 
	 */
	Map<String, AreaNodeVo> getAreas(String eId) throws FrameworkRuntimeException;

	/**
	 * 
	 */
	Map<String, AreaNodeVo> getAreasByUserId(String userId) throws FrameworkRuntimeException;

	/**
	 * 
	 */
	List<SettlementVo> getSettlements() throws FrameworkRuntimeException;

	/**
	 * 
	 */
	AreaVo getAreaById(String id) throws FrameworkRuntimeException;

	/**
	 * 
	 */
	Map<String, Obj> getOrganizationChartMap(String enterpriseid) throws FrameworkRuntimeException;
}
