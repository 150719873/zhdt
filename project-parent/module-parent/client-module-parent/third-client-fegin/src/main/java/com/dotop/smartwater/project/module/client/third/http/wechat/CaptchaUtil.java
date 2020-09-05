/**
 * jelibuy-server
 * com.jelibuy.service.base.util
 * CaptchaUtil.java
 */
package com.dotop.smartwater.project.module.client.third.http.wechat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

/**
 * 验证码工具类，用来生成验证码字符串、验证码图片以及发送验证码短信
 *

 * @createTime 2014年8月7日 下午4:50:37
 * @since 1.0
 */
public class CaptchaUtil {

	private static final Logger LOGGER = LoggerFactory.getLogger(CaptchaUtil.class);

	/**
	 * private String smsAPI = "http://api.duanxin.cm/?action=send";
	 * 从数据库配置而来
	 */
	private String SMS_API = "http://119.145.253.67:8080/edeeserver/sendSMS.do";

	/**
	 * 从数据库配置而来
	 */
	private String TERM_TO_REPLACE = "<captcha>";

	/**
	 * 从数据库配置而来
	 */
	private int expireTimeInMinute = 2;

	/**
	 * 从数据库配置而来
	 * private String smsContent = "欢迎注册快途用户，您本次的验证码是<captcha>，有效期" +
	 * expireTimeInMinute + "分钟"+"【快途科技】";
	 */
	private String smsContent = "您本次的验证码是<captcha>，有效期" + expireTimeInMinute + "分钟" + "【快途科技】";

	/**
	 * private String userName = "70200330";// 从数据库配置而来
	 */
	private String userName = "900655";

	/**
	 * private String password = "b3fd451584b7e0363f261bc8f67dd187";// 从数据库配置而来
	 */
	private String password = "kwikto123456";

	/**
	 * 从数据库配置而来
	 */
	private int minLength = 4;

	private static final char[] CHARS = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O',
			'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
	private static final char[] DIGITS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};

	private static CaptchaUtil instance = new CaptchaUtil();

	private CaptchaUtil() {

	}

	/**
	 * @return
	 */
	public static CaptchaUtil instance() {
		return instance;
	}


	/**
	 * 向指定手机号码发送验证码短信
	 *
	 * @param phoneNumber 手机号码
	 * @param captcha     验证码
	 * @return
	 */
	public int sendCaptchaSMS(String phoneNumber, String captcha) {
		LOGGER.debug("Start excute sendCaptchaSMS");
		LOGGER.debug("sendSms param:phoneNumber={},captcha={}", phoneNumber, captcha);
		Map<String, String> paramsMap = new HashMap<>();
		String result = "";
		paramsMap.put("UserName", userName);
		paramsMap.put("Password", password);
		paramsMap.put("MobileNumber", phoneNumber);
		paramsMap.put("MsgContent", smsContent.replaceFirst(TERM_TO_REPLACE, captcha));
		try {
			result = WechatHttpClientUtils.sendPostRequest(SMS_API, paramsMap);
			LOGGER.debug("发送短信返回码为:{}", result);
		} catch (UnsupportedEncodingException e) {
			LOGGER.debug("sendCaptchaSMS excute have Exception", e);
			return -1;
		}
		if ("0".equals(result.trim())) {
			return 0;
		} else {
			LOGGER.debug("短信失败信息为:{}", result);
			return -1;
		}
	}

	/**
	 * 获取指定长度的纯数字验证码
	 *
	 * @param length 验证码长度
	 * @return 纯数字验证码
	 */
	public static String getDigitalCaptcha(int length) {
		if (length < instance.minLength) {
			throw new IllegalArgumentException(length + "");
		}
		SecureRandom random = new SecureRandom(long2Bytes(System.currentTimeMillis()));
		StringBuilder builder = new StringBuilder();
		int charsLength = DIGITS.length;
		for (int i = 0; i < length; i++) {
			builder.append(DIGITS[random.nextInt(charsLength)]);
		}
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Generate Digital Captcha: {}", builder);
		}
		return builder.toString();
	}

	private static byte[] long2Bytes(long num) {
		byte[] byteNum = new byte[8];
		for (int ix = 0; ix < 8; ++ix) {
			int offset = 64 - (ix + 1) * 8;
			byteNum[ix] = (byte) ((num >> offset) & 0xff);
		}
		return byteNum;
	}

	/**
	 * 获取字母、数字验证码
	 *
	 * @param length 验证码长度
	 * @return 验证码
	 */
	public static String getAlphabetCaptcha(int length) {
		if (length < instance.minLength) {
			throw new IllegalArgumentException(length + "");
		}
		SecureRandom random = new SecureRandom(long2Bytes(System.currentTimeMillis()));
		StringBuilder builder = new StringBuilder();
		int charsLength = CHARS.length;
		for (int i = 0; i < length; i++) {
			builder.append(CHARS[random.nextInt(charsLength)]);
		}
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Generate Alphabet Captcha: {}", builder);
		}
		return builder.toString();
	}

}
