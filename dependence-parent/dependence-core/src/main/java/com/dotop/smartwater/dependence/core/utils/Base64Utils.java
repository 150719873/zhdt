package com.dotop.smartwater.dependence.core.utils;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;

/**
 * 

 * @date 2019年5月8日
 * @description base64工具类
 */
public final class Base64Utils {

	private static final Base64.Decoder DECODER = Base64.getDecoder();
	private static final Base64.Encoder ENCODER = Base64.getEncoder();

	private Base64Utils() {

	}

	public static String encoder(String text) throws FrameworkRuntimeException {
		byte[] textByte = text.getBytes(StandardCharsets.UTF_8);
		return ENCODER.encodeToString(textByte);
	}

	public static String decoder(String text) throws FrameworkRuntimeException {
		return new String(DECODER.decode(text), StandardCharsets.UTF_8);
	}
}
