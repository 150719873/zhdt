package com.dotop.smartwater.project.module.core.water.utils.sequence;

import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;

/**
 * 分布式高效有序ID生产黑科技(sequence)
 */
public class Sequence {

	private static final long twepoch = 1288834974657L;
	private static final long workerIdBits = 5L;
	private static final long datacenterIdBits = 5L;
	private static final long maxWorkerId = -1L ^ (-1L << workerIdBits);
	private static final long maxDatacenterId = -1L ^ (-1L << datacenterIdBits);
	private static final long sequenceBits = 12L;
	private static final long workerIdShift = sequenceBits;
	private static final long datacenterIdShift = sequenceBits + workerIdBits;
	private static final long timestampLeftShift = sequenceBits + workerIdBits + datacenterIdBits;
	private static final long sequenceMask = -1L ^ (-1L << sequenceBits);

	private long workerId;
	private long datacenterId;
	private long sequence;
	private long lastTimestamp = -1L;

	/**
	 * @param workerId     工作机器ID
	 * @param datacenterId 序列号
	 */
	public Sequence(long workerId, long datacenterId) {
		if (workerId > maxWorkerId || workerId < 0) {
			throw new IllegalArgumentException(
					String.format("worker Id can't be greater than %d or less than 0", maxWorkerId));
		}
		if (datacenterId > maxDatacenterId || datacenterId < 0) {
			throw new IllegalArgumentException(
					String.format("datacenter Id can't be greater than %d or less than 0", maxDatacenterId));
		}
		this.workerId = workerId;
		this.datacenterId = datacenterId;
	}

	/**
	 * 获取下一个ID
	 *
	 * @return
	 */
	public synchronized long nextId() {
		long timestamp = timeGen();
		if (timestamp < lastTimestamp) {
			throw new FrameworkRuntimeException(String.format(
					"Clock moved backwards. Refusing to generate id for %d milliseconds", lastTimestamp - timestamp));
		}
		if (lastTimestamp == timestamp) {
			sequence = (sequence + 1) & sequenceMask;
			if (sequence == 0) {
				timestamp = tilNextMillis(lastTimestamp);
			}
		} else {
			sequence = 0L;
		}

		lastTimestamp = timestamp;

		return ((timestamp - twepoch) << timestampLeftShift) | (datacenterId << datacenterIdShift)
				| (workerId << workerIdShift) | sequence;
	}

	private static long tilNextMillis(long lastTimestamp) {
		long timestamp = timeGen();
		while (timestamp <= lastTimestamp) {
			timestamp = timeGen();
		}
		return timestamp;
	}

	private static long timeGen() {
		return SystemClock.now();
	}

}