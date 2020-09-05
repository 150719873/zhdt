package com.dotop.smartwater.project.module.core.third.utils.wechat;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 

 * @date 2018年7月19日 上午10:08:29
 * @version 1.0.0 TODO 此类是否还继续使用？
 */
public class GenerateSNUtil {

	private final static Logger logger = LogManager.getLogger(GenerateSNUtil.class);

	private static final String WECHAT_PAY_SN = "WECHAT_SN:PAY";

	private static final String WECHAT_CHARGE_SN = "WECHAT_SN:CHARGE";

	// private StringValueCache svc;

	private static final ThreadLocal<DateFormat> dateTimeFormat = new ThreadLocal<DateFormat>() {
		@Override
		protected DateFormat initialValue() {
			return new SimpleDateFormat("yyyyMMddHHmmss");
		}
	};

	public static String nextPaySN() {
		StringBuilder builder = new StringBuilder("P");
		builder.append(dateTimeFormat.get().format(new Date()));
		// .append(redisUtil.incr(WECHAT_PAY_SN, new Date().getTime()));
		return builder.toString();
	}

	public static String nextChargeSN() {
		StringBuilder builder = new StringBuilder("C");
		builder.append(dateTimeFormat.get().format(new Date()));
		// .append(redisUtil.incr(WECHAT_CHARGE_SN, new Date().getTime()));
		return builder.toString();
	}

}
