package com.dotop.smartwater.project.auth.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.dotop.smartwater.dependence.core.common.BaseDao;
import com.dotop.smartwater.project.module.core.auth.dto.UserDto;
import com.dotop.smartwater.project.module.core.auth.vo.UserVo;
import org.springframework.stereotype.Repository;

/**
 * 

 * @date 2019年5月9日
 * @description
 */
public interface IUserDao extends BaseDao<UserDto, UserVo> {

	/**
	 * 
	 */
	@Override
	void add(UserDto user);

	/**
	 * 
	 */
	int update(UserDto user);

	/**
	 * 
	 */
	int delete(@Param("userid") String userid, @Param("status") Integer status);

	/**
	 * 
	 */
	List<UserVo> getUserList(UserDto user);

	/**
	 * 
	 */
	List<UserVo> getUsers(@Param("userIds") String userIds);

	/**
	 * 
	 */
	UserVo login(UserDto user);

	/**
	 * 
	 */
	UserVo loginByWorkNum(UserDto user);

	/**
	 * 
	 */
	UserVo findUserByAccount(UserDto user);

	/**
	 * 
	 */
	UserVo findUserByWorknum(UserDto user);

	/**
	 * 
	 */
	UserVo findUserByAccountAndId(UserDto user);

	/**
	 * 
	 */
	UserVo findUserByWorknumAndId(UserDto user);

	/**
	 * 
	 */
	UserVo findUserById(@Param("userid") String userid);

	/**
	 * 
	 */
	int delUserArea(String userid);

	/**
	 * 
	 */
	int addUserArea(@Param("userid") String userid, @Param("list") List<String> list);

	/**
	 * 
	 */
	int changePwd(@Param("userid") String userid, @Param("password") String password);
}
