package com.dotop.smartwater.project.auth.cache;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dotop.smartwater.dependence.cache.StringValueCache;

/**
 * 

 * @date 2019年5月9日
 * @description
 */
@Component
public class RedisDao {

	private static final Logger LOGGER = LogManager.getLogger(RedisDao.class);

	@Autowired
	private StringValueCache svc;

	private static final String TICKET = "auth:ticket:web:";
	private static final long LIVE_TIME = 7200L * 12 * 30;

	private static final String APITICKET = "auth:ticket:api:";
	private static final int APITIMEOUT = 60 * 60 * 24;

	private static final String WATERAPPTICKET = "auth:ticket:water:";
	private static final int WATERAPPOUT = 3600 * 24 * 7;

	public void setWebUserTicket(String userid, String ticket) {
		try {
			if (userid == null || ticket == null) {
				return;
			}
			svc.set(TICKET + userid, ticket, LIVE_TIME);
		} catch (Exception e) {
			LOGGER.error("setWebUserToken", e);
		}
	}

	public String getWebUserTicket(String userid) {
		try {
			if (userid == null) {
				return null;
			}
			return svc.get(TICKET + userid);
		} catch (Exception e) {
			LOGGER.error("getWebUserToken", e);
			return null;
		}
	}

	public void delWebUserTicket(String userid) {
		try {
			if (userid == null) {
				return;
			}
			svc.del(TICKET + userid);
		} catch (Exception e) {
			LOGGER.error("delWebUserToken", e);
		}
	}

	public void setApiUserTicket(String userid, String ticket) {
		try {
			if (userid == null || ticket == null) {
				return;
			}
			svc.set(APITICKET + userid, ticket, APITIMEOUT);
		} catch (Exception e) {
			LOGGER.error("setApiUserToken", e);
		}
	}

	public String getApiUserTicket(String userid) {
		try {
			if (userid == null) {
				return null;
			}
			return svc.get(APITICKET + userid, String.class);

		} catch (Exception e) {
			LOGGER.error("getApiUserToken", e);
			return null;
		}
	}

	public void delApiUserTicket(String userid) {
		try {
			if (userid == null) {
				return;
			}
			svc.del(APITICKET + userid);
		} catch (Exception e) {
			LOGGER.error("delApiUserToken", e);
		}
	}

	public void setWaterAppUserTicket(String userid, String ticket) {
		try {
			if (userid == null || ticket == null) {
				return;
			}
			svc.set(WATERAPPTICKET + userid, ticket, WATERAPPOUT);
		} catch (Exception e) {
			LOGGER.error("setWaterAppUserTicket", e);
		}
	}

	public String getWaterAppUserTicket(String userid) {
		try {
			if (userid == null) {
				return null;
			}
			return svc.get(WATERAPPTICKET + userid, String.class);

		} catch (Exception e) {
			LOGGER.error("getWaterAppUserTicket", e);
			return null;
		}
	}

	public void setValue(String key, String value) {
		if (StringUtils.isBlank(key) || StringUtils.isBlank(value)) {
			return;
		}
		svc.set(key, value);
	}

	public String getValue(String key) {
		if (StringUtils.isBlank(key)) {
			return null;
		}
		return svc.get(key, String.class);
	}

	public void setValueTtl(String key, String value, Long seconds) {
		if (StringUtils.isBlank(key) || StringUtils.isBlank(value) || seconds == null) {
			return;
		}
		svc.set(key, value, seconds);
	}

	public void delValue(String key) {
		if (StringUtils.isBlank(key)) {
			return;
		}
		svc.del(key);
	}
}
