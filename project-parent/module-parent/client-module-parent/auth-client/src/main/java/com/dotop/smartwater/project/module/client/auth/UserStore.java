package com.dotop.smartwater.project.module.client.auth;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.BeanUtils;

import com.dotop.smartwater.dependence.core.utils.JSONUtils;
import com.dotop.smartwater.project.module.core.auth.config.WaterClientConfig;
import com.dotop.smartwater.project.module.core.auth.utils.CipherEncryptors;
import com.dotop.smartwater.project.module.core.auth.vo.UserLoginVo;
import com.dotop.smartwater.project.module.core.auth.vo.UserVo;
import com.dotop.water.tool.service.AppInf;

public interface UserStore {

	default String getUserid(HttpServletRequest request) {
		String userid = request.getHeader("userid");
		return userid;
	}

	default String getTicket(HttpServletRequest request) {
		// String ticket = request.getHeader("usertoken");
		String ticket = request.getHeader("ticket");
		return ticket;
	}

	default UserVo getUser(HttpServletRequest request) {
		return getUser(getUserid(request), getTicket(request));
	}

	default UserVo getUser(String userid, String ticket) {
		UserVo user = null;
		try {
			UserVo u = AppInf.getUserBaseInfo(userid, ticket);
			if (u != null) {
				user = new UserVo();
			}
			BeanUtils.copyProperties(u, user);
			user.setTicket(ticket);
		} catch (Exception e) {

		}
		return user;
	}

	default UserLoginVo getUserLogin(String dataString) {
		if (dataString == null) {
			return null;
		}
		String data = CipherEncryptors.decrypt(WaterClientConfig.WaterCasKey1, WaterClientConfig.WaterCasKey2,
				dataString);
		return JSONUtils.parseObject(data, UserLoginVo.class);
	}
}
