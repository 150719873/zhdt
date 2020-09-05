package com.dotop.smartwater.dependence.lock;

public interface IDistributedLock {

	public static final long TIMEOUT_MILLIS = 10000 * 1000L;

	public static final int RETRY_TIMES = Integer.MAX_VALUE;

	public static final long SLEEP_MILLIS = 500;

	public boolean lock(String key);

	public boolean lock(String key, int retryTimes);

	public boolean lock(String key, int retryTimes, long sleepMillis);

	public boolean lock(String key, long expire);

	public boolean lock(String key, long expire, int retryTimes);

	public boolean lock(String key, long expire, int retryTimes, long sleepMillis);

	public boolean releaseLock(String key);

	// 扩展判断
	public boolean lock(String key, long expire, IExtendFlag iExtendFlag);

	public boolean lock(String key, long expire, int retryTimes, long sleepMillis, IExtendFlag iExtendFlag);
}