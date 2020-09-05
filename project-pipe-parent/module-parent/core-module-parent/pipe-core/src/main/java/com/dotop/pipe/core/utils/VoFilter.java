package com.dotop.pipe.core.utils;

import com.alibaba.fastjson.serializer.PropertyFilter;

public class VoFilter {

	private static PropertyFilter filter;

	private static VoFilter instance;

	private VoFilter() {
		filter = new PropertyFilter() {
			public boolean apply(Object object, String name, Object value) {
				for (String str : ARRS) {
					if (name.equalsIgnoreCase(str)) {
						return false;
					}
				}
				return true;
			}
		};
	}

	public static synchronized VoFilter getInstance() {
		if (instance == null) {
			instance = new VoFilter();
		}
		return instance;
	}

	private final static String[] ARRS = new String[] { "des", "lastBy", "productId", "factoryId", "factory",
			"enterpriseId", "devProId", "devSendDate", "deviceCode", "serReceDate", "tag" };

	public final static PropertyFilter rf() {
		getInstance();
		return VoFilter.filter;
	}
}
