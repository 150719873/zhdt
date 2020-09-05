package com.dotop.smartwater.dependence.lock;

import java.nio.charset.Charset;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisStringCommands.SetOption;
import org.springframework.data.redis.connection.ReturnType;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.types.Expiration;

import com.dotop.smartwater.dependence.core.log.LogMsg;

public class RedisDistributedLock extends AbstractDistributedLock {

	private static final Logger LOGGER = LogManager.getLogger(RedisDistributedLock.class);

	private RedisTemplate<String, String> redisTemplate;

	private ThreadLocal<String> lockFlag = new ThreadLocal<>();

	public static final String UNLOCK_SCRIPT;

	static {
		StringBuilder sb = new StringBuilder();
		sb.append("if redis.call(\"get\",KEYS[1]) == ARGV[1] ");
		sb.append("then ");
		sb.append("    return redis.call(\"del\",KEYS[1]) ");
		sb.append("else ");
		sb.append("    return 0 ");
		sb.append("end ");
		UNLOCK_SCRIPT = sb.toString();
	}

	public RedisDistributedLock() {
		super();
	}

	public RedisDistributedLock(RedisTemplate<String, String> redisTemplate) {
		super();
		this.redisTemplate = redisTemplate;
	}

	public RedisTemplate<String, String> getRedisTemplate() {
		return redisTemplate;
	}

	public void setRedisTemplate(RedisTemplate<String, String> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

	@Override
	public boolean lock(String key, long expire, int retryTimes, long sleepMillis) {
		return lock(key, expire, retryTimes, sleepMillis, null);
	}

	@Override
	public boolean lock(String key, long expire, int retryTimes, long sleepMillis, IExtendFlag iExtendFlag) {
		boolean result = setRedis(key, expire);
		// 如果获取锁失败，按照传入的重试次数进行重试
		while ((!result) && retryTimes-- > 0) {
			try {
				// 扩展判断，外部优先判断
				if (iExtendFlag != null && iExtendFlag.flag()) {
					return true;
				}
				LOGGER.debug(LogMsg.to("key", key, "lock failed, retrying:", retryTimes));
				Thread.sleep(sleepMillis);
			} catch (InterruptedException e) {
				LOGGER.error(LogMsg.to(e));
				return false;
			}
			result = setRedis(key, expire);
		}
		return result;
	}

	private boolean setRedis(String key, long expire) {
		try {
			String result = redisTemplate.execute(new RedisCallback<String>() {
				@Override
				public String doInRedis(RedisConnection connection) throws DataAccessException {
					String uuid = UUID.randomUUID().toString();
					lockFlag.set(uuid);
					Boolean result = connection.set(key.getBytes(Charset.forName("UTF-8")),
							uuid.getBytes(Charset.forName("UTF-8")), Expiration.from(expire, TimeUnit.MILLISECONDS),
							SetOption.SET_IF_ABSENT);
					if (result != null && result) {
						return uuid;
					}
					return null;
				}
			});
			return !StringUtils.isEmpty(result);
		} catch (Exception e) {
			LOGGER.error(LogMsg.to(e));
		}
		return false;
	}

	@Override
	public boolean releaseLock(String key) {
		if (StringUtils.isBlank(key)) {
			return false;
		}
		String uuid = lockFlag.get();
		// 释放线程数据
		lockFlag.remove();
		if (StringUtils.isBlank(uuid)) {
			return true;
		}
		try {
			byte[][] keysAndArgs = new byte[2][];
			keysAndArgs[0] = key.getBytes(Charset.forName("UTF-8"));
			keysAndArgs[1] = uuid.getBytes(Charset.forName("UTF-8"));
			Long result = redisTemplate.execute(new RedisCallback<Long>() {
				@Override
				public Long doInRedis(RedisConnection connection) throws DataAccessException {
					Long result = (Long) connection.scriptingCommands()
							.eval(UNLOCK_SCRIPT.getBytes(Charset.forName("UTF-8")), ReturnType.INTEGER, 1, keysAndArgs);
					return result;
				}
			});
			return result != null && result > 0;
		} catch (Exception e) {
			LOGGER.error(LogMsg.to(e));
		}
		return false;
	}

}