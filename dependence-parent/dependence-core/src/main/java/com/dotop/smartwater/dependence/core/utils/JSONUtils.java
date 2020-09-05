package com.dotop.smartwater.dependence.core.utils;

import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;

/**
 * json 封装类
 * 

 *
 */
public class JSONUtils {

	private JSONUtils() {
		super();
	}

	public static final String toJSONString() {
		return null;
	}

	public static final String toJSONString(Object object) {
		if (object == null) {
			return null;
		}
		return com.alibaba.fastjson.JSON.toJSONString(object, SerializerFeature.DisableCircularReferenceDetect);
	}

	public static final <T> T parseObject(String text, Class<T> clazz) {
		return com.alibaba.fastjson.JSON.parseObject(text, clazz);
	}

	public static final <T> T parseObject(String text, TypeReference<T> type) {
		return com.alibaba.fastjson.JSON.parseObject(text, type);
	}

	public static final <K, V> Map<K, V> parseObject(String text, Class<K> clazzK, Class<V> clazzV) {
		return com.alibaba.fastjson.JSON.parseObject(text, new TypeReference<Map<K, V>>((Type) clazzK, (Type) clazzV) {
		});
	}

	public static final <T> List<T> parseArray(String text, Class<T> clazz) {
		return com.alibaba.fastjson.JSON.parseArray(text, clazz);
	}

	public static final JSONObjects parseObject(String text) {
		return com.alibaba.fastjson.JSON.parseObject(text, JSONObjects.class);
	}

	public static final byte[] toJSONBytes(Object object) throws FrameworkRuntimeException {
		String jsonString = toJSONString(object);
		if (!StringUtils.isEmpty(jsonString)) {
			return jsonString.getBytes(StandardCharsets.UTF_8);
		}
		return new byte[0];
	}
}
