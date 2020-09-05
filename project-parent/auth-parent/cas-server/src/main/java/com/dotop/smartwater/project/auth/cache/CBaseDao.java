package com.dotop.smartwater.project.auth.cache;

import com.dotop.smartwater.project.module.core.auth.local.AuthCasClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dotop.smartwater.dependence.core.utils.JSONUtils;
import com.dotop.smartwater.project.module.core.auth.config.WaterClientConfig;
import com.dotop.smartwater.project.module.core.auth.utils.CipherEncryptors;
import com.dotop.smartwater.project.module.core.auth.vo.UserLoginVo;
import com.dotop.smartwater.project.module.core.auth.vo.UserVo;

/**
 * 

 * @date 2019年5月9日
 * @description
 */
@Component
public class CBaseDao {

	@Autowired
	private UserCacheDao userCacheDao;

	@Autowired
	private RedisDao redisDao;

	public String respString(Object object) {
		return JSONUtils.toJSONString(object);
	}

	public boolean webAuth(String userid, String ticket) {
		boolean flag = false;
		String t = redisDao.getWebUserTicket(userid);
		if (ticket.equals(t)) {
			flag = true;
			// 鉴权成功 增加过期时间
			redisDao.setWebUserTicket(userid, ticket);
		}
		return flag;
	}

	public UserVo getRedisUser(String userid) {
		UserVo user =userCacheDao.getUser(userid);
		AuthCasClient.add(user);
		return user;
	}

	public static UserLoginVo getUserLogin(String dataString) {

		if (dataString == null) {
			return null;
		}

		String data = CipherEncryptors.decrypt(WaterClientConfig.WaterCasKey1, WaterClientConfig.WaterCasKey2,
				dataString);
		return JSONUtils.parseObject(data, UserLoginVo.class);
	}

	public static String generateCasKey(UserLoginVo userLogin) {
		if (userLogin == null) {
			return null;
		}
		return CipherEncryptors.encrypt(WaterClientConfig.WaterCasKey1, WaterClientConfig.WaterCasKey2,
				JSONUtils.toJSONString(userLogin));
	}

	public boolean waterAppAuth(String userid, String ticket) {
		boolean flag = false;
		if (ticket.equals(redisDao.getWaterAppUserTicket(userid))) {
			flag = true;
		}
		return flag;
	}
}
