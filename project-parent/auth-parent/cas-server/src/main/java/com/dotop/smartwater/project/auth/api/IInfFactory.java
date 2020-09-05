package com.dotop.smartwater.project.auth.api;

import java.util.List;
import java.util.Map;

import com.dotop.smartwater.dependence.core.common.BaseFactory;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.project.module.core.auth.form.UserForm;
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
public interface IInfFactory extends BaseFactory<UserForm, UserVo> {

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
	Map<String, MenuVo> getPermissions(UserVo user, String modelid) throws FrameworkRuntimeException;

	/**
	 * 
	 */
	Map<String, AreaNodeVo> getAreas(String enterpriseid) throws FrameworkRuntimeException;

	/**
	 * 
	 */
	Map<String, AreaNodeVo> getAreasByUserId(String userid) throws FrameworkRuntimeException;

	/**
	 * 
	 */
	List<SettlementVo> getSettlements() throws FrameworkRuntimeException;

	/**
	 * 
	 */
	AreaVo getAreaById(String cid) throws FrameworkRuntimeException;

	/**
	 * 
	 */
	Map<String, Obj> getOrganizationChartMap(String enterpriseid) throws FrameworkRuntimeException;
}
