package com.dotop.pipe.web.factory.user;

import com.dotop.pipe.auth.cas.web.IAuthCasWeb;
import com.dotop.pipe.auth.core.vo.auth.LoginCas;
import com.dotop.pipe.web.api.factory.user.IUserFactory;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.project.module.core.auth.vo.UserVo;
import com.dotop.water.tool.service.BaseInf;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 获取用户信息
 *
 */
@Component
public class UserFactoryImpl implements IUserFactory {

	@Autowired
	private IAuthCasWeb iAuthCasApi;

	/**
	 * 获取用户列表
	 * @param enterpriseId
	 * @return
	 * @throws FrameworkRuntimeException
	 */
	@Override
	public List<UserVo> getUserList(String enterpriseId) throws FrameworkRuntimeException {
		if (StringUtils.isBlank(enterpriseId)) {
			LoginCas loginCas = iAuthCasApi.get();
			return BaseInf.getUserList(loginCas.getEnterpriseId());
		}
		return BaseInf.getUserList(enterpriseId);
	}
}
