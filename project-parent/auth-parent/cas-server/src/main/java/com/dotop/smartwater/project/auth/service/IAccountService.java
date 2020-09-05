package com.dotop.smartwater.project.auth.service;

import java.util.List;

import com.dotop.smartwater.dependence.core.common.BaseService;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.project.module.core.auth.bo.EnterpriseBo;
import com.dotop.smartwater.project.module.core.auth.bo.SettlementBo;
import com.dotop.smartwater.project.module.core.auth.bo.UserAreaBo;
import com.dotop.smartwater.project.module.core.auth.bo.UserBo;
import com.dotop.smartwater.project.module.core.auth.vo.EnterpriseVo;
import com.dotop.smartwater.project.module.core.auth.vo.LogoVo;
import com.dotop.smartwater.project.module.core.auth.vo.SettlementVo;
import com.dotop.smartwater.project.module.core.auth.vo.UserVo;

/**
 * 

 * @date 2019年5月9日
 * @description
 */
public interface IAccountService extends BaseService<UserBo, UserVo> {

	/**
	 * 
	 */
	UserVo login(UserBo user) throws FrameworkRuntimeException;

	/**
	 * 
	 */
	int addUser(UserBo user) throws FrameworkRuntimeException;

	/**
	 * 
	 */
	int editUser(UserBo user) throws FrameworkRuntimeException;
	
	int editUserState(UserBo user) throws FrameworkRuntimeException;

	/**
	 * 
	 */
	UserVo findUserByAccount(UserBo user) throws FrameworkRuntimeException;

	/**
	 * 
	 */
	UserVo findUserByWorknum(UserBo user) throws FrameworkRuntimeException;

	/**
	 * 
	 */
	UserVo findUserByAccountAndId(UserBo user) throws FrameworkRuntimeException;

	/**
	 * 
	 */
	UserVo findUserByWorknumAndId(UserBo user) throws FrameworkRuntimeException;

	/**
	 * 
	 */
	Pagination<UserVo> getUserList(UserBo user) throws FrameworkRuntimeException;

	/**
	 * 
	 */
	UserVo findUserById(String userid) throws FrameworkRuntimeException;

	/**
	 * 
	 */
	int addEnterprise(EnterpriseBo enterprise) throws FrameworkRuntimeException;

	/**
	 * 
	 */
	int editEnterprise(EnterpriseBo enterprise) throws FrameworkRuntimeException;

	/**
	 * 
	 */
	int delEnterprise(String enterpriseid) throws FrameworkRuntimeException;

	/**
	 * 
	 */
	EnterpriseVo findEnterpriseById(String enterpriseid) throws FrameworkRuntimeException;

	/**
	 * 
	 */
	EnterpriseVo findEnterpriseByWebsite(String website) throws FrameworkRuntimeException;

	/**
	 * 
	 */
	Pagination<EnterpriseVo> getEnterpriseList(EnterpriseBo etp) throws FrameworkRuntimeException;

	/**
	 * 
	 */
	List<UserVo> getEnterpriseList(UserVo vo) throws FrameworkRuntimeException;

	/**
	 * 
	 */
	List<UserVo> getUsers(String userIds) throws FrameworkRuntimeException;

	/**
	 * 
	 */
	EnterpriseVo findEnterpriseByName(EnterpriseBo enterprise) throws FrameworkRuntimeException;

	/**
	 * 
	 */
	EnterpriseVo findEnterpriseByNameAndId(EnterpriseBo enterprise) throws FrameworkRuntimeException;

	/**
	 * 
	 */
	int addUserArea(UserAreaBo userArea) throws FrameworkRuntimeException;

	/**
	 * 
	 */
	SettlementVo getSettlement(String enterpriseid) throws FrameworkRuntimeException;

	/**
	 * 
	 */
	LogoVo getLogo(String enterpriseid) throws FrameworkRuntimeException;

	/**
	 * 
	 */
	int addSettlement(SettlementBo settlement) throws FrameworkRuntimeException;

	/**
	 * 
	 */
	int changePwd(String userid, String newPwd) throws FrameworkRuntimeException;
}
