package com.dotop.smartwater.dependence.core.utils;

import java.util.Random;

/**
 * 

 * @date 2019年5月8日
 * @description 随机数工具类
 */
public final class RandomUtils {

	private static final int STEP = 8;

	private RandomUtils() {
		super();
	}

	/**
	 * 生成随机数
	 */
	public static String get(int length) {
		Random r = new Random();
		StringBuilder sb = new StringBuilder();
		sb.append((r.nextInt(STEP) + 1));
		for (int i = 1; i < length; i++) {
			sb.append((r.nextInt(STEP + 1)));
		}
		return sb.toString();
	}
}
