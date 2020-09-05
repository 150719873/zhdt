package com.dotop.smartwater.project.module.core.water.config;

import com.dotop.smartwater.project.module.core.water.utils.sequence.Sequence;

/**

 */
public class Config {

	public static String VERSION;
	/**
	 * 一周
	 */
	public static final int TOKEN_TIME = 3600 * 24 * 7;

	/**
	 * 10天
	 */
	public static final int APP_LIVE_TIME = 3600 * 24 * 35;

	public static String ServerHost;
	public static String UserInvalidTime;
	public static Sequence Generator;
	public static Boolean weixinDebug;
	public static String PayCallBackUrl;

}
