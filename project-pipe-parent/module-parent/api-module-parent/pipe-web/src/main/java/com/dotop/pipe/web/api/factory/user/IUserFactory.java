package com.dotop.pipe.web.api.factory.user;

import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.project.module.core.auth.vo.UserVo;

import java.util.List;

/**
 * 获取用户信息
 *
 */
public interface IUserFactory {

	/**
	 * 获取用户列表
	 * @param enterpriseId
	 * @return
	 * @throws FrameworkRuntimeException
	 */
	List<UserVo> getUserList(String enterpriseId) throws FrameworkRuntimeException;
}
