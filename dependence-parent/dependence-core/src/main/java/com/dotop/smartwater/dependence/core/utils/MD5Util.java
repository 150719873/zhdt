package com.dotop.smartwater.dependence.core.utils;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * 

 * @date 2019年5月8日
 * @description md5工具类
 */
public class MD5Util {

	private MD5Util() {
		super();
	}

	public static final String encode(String str) {
		if (StringUtils.isBlank(str)) {
			return null;
		}
		return DigestUtils.md5Hex(str);
	}

}
