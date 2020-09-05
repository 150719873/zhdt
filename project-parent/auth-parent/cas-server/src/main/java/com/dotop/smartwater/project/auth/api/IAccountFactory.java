package com.dotop.smartwater.project.auth.api;

import java.util.List;

import com.dotop.smartwater.dependence.core.common.BaseFactory;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.project.module.core.auth.form.EnterpriseForm;
import com.dotop.smartwater.project.module.core.auth.form.UserAreaForm;
import com.dotop.smartwater.project.module.core.auth.form.UserForm;
import com.dotop.smartwater.project.module.core.auth.vo.EnterpriseVo;
import com.dotop.smartwater.project.module.core.auth.vo.UserVo;

/**
 * 

 * @date 2019年5月9日
 * @description 用户信息服务
 */
public interface IAccountFactory extends BaseFactory<UserForm, UserVo> {

	/**
	 * 登录
	 */
	UserVo login(String eid, String account, String pwd) throws FrameworkRuntimeException;

	/**
	 * 
	 */
	EnterpriseVo findEnterpriseById(UserForm user) throws FrameworkRuntimeException;

	/**
	 * 
	 */
	EnterpriseVo findEnterpriseByWebsite(String website) throws FrameworkRuntimeException;

	/**
	 * 
	 */
	UserVo findUserByAccount(UserForm user) throws FrameworkRuntimeException;

	/**
	 * 
	 */
	UserVo findUserByWorknum(UserForm user) throws FrameworkRuntimeException;

	/**
	 * 
	 */
	Integer addUser(UserForm user) throws FrameworkRuntimeException;

	/**
	 * 
	 */
	UserVo findUserById(String userid) throws FrameworkRuntimeException;

	/**
	 * 
	 */
	UserVo findUserByAccountAndId(UserForm user) throws FrameworkRuntimeException;

	/**
	 * 
	 */
	UserVo findUserByWorknumAndId(UserForm user) throws FrameworkRuntimeException;

	/**
	 * 
	 */
	Integer editUser(UserForm user) throws FrameworkRuntimeException;
	
	Integer editUserState(UserForm user) throws FrameworkRuntimeException;

	/**
	 * 
	 */
	Pagination<UserVo> getUserList(UserForm user) throws FrameworkRuntimeException;

	/**
	 * 
	 */
	EnterpriseVo findEnterpriseByName(EnterpriseForm enterprise) throws FrameworkRuntimeException;

	/**
	 * 
	 */
	Integer addEnterprise(EnterpriseForm enterprise) throws FrameworkRuntimeException;

	/**
	 * 开通域名
	 * @param enterprise
	 * @return
	 * @throws FrameworkRuntimeException
	 */
	void openDoMain(EnterpriseForm enterprise) throws FrameworkRuntimeException;
	
	
	/**
	 * 
	 */
	EnterpriseVo findEnterpriseByNameAndId(EnterpriseForm enterprise) throws FrameworkRuntimeException;

	/**
	 * 
	 */
	Integer editEnterprise(EnterpriseForm enterprise) throws FrameworkRuntimeException;

	/**
	 * 
	 */
	EnterpriseVo findEnterpriseById(String enterpriseid) throws FrameworkRuntimeException;

	/**
	 * 
	 */
	Integer delEnterprise(String enterpriseid) throws FrameworkRuntimeException;

	/**
	 * 删除阿里DNS域名
	 * @param enterprise
	 * @return
	 * @throws FrameworkRuntimeException
	 */
	void deleteDoMain(EnterpriseForm enterprise) throws FrameworkRuntimeException;
	
	
	/**
	 * 远程修改阿里DNS域名
	 * @param oldEn
	 * @param newEn
	 * @throws FrameworkRuntimeException
	 */
	void editDoMain(EnterpriseVo oldEn , EnterpriseForm newEn) throws FrameworkRuntimeException;
	
	/**
	 * 
	 */
	Pagination<EnterpriseVo> getEnterpriseList(EnterpriseForm etp) throws FrameworkRuntimeException;

	/**
	 * 
	 */
	List<UserVo> getUsers(String userIds) throws FrameworkRuntimeException;

	/**
	 * 
	 */
	Integer addUserArea(UserAreaForm userArea) throws FrameworkRuntimeException;

}
