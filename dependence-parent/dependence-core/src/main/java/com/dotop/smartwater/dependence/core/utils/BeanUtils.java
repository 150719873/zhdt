package com.dotop.smartwater.dependence.core.utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.dotop.smartwater.dependence.core.exception.BaseExceptionConstants;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.log.LogMsg;

/**
 * 

 * @date 2019年5月8日
 * @description 对象复制类
 */
public final class BeanUtils {

	private static final Logger LOGGER = LogManager.getLogger(BeanUtils.class);

	private BeanUtils() {

	}

	public static <S, T> T copy(S source, T to) throws FrameworkRuntimeException {
		org.springframework.beans.BeanUtils.copyProperties(source, to);
		return to;
	}

	public static <S, T> T copy(S source, Class<T> to) throws FrameworkRuntimeException {
		try {
			if (source == null) {
				return null;
			}
			// 暂时使用此复制方法进行对象的嵌套复制
            return JSONUtils.parseObject(JSONUtils.toJSONString(source), to);
		} catch (IllegalArgumentException | SecurityException e) {
			LOGGER.error(LogMsg.to("ex", e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.ILLEGAL_EXCEPTION,
					BaseExceptionConstants.getMessage(BaseExceptionConstants.ILLEGAL_EXCEPTION), e);
		}
	}

	public static <S, T> T copyProperties(S source, Class<T> to) throws FrameworkRuntimeException {
		try {
			if (source == null) {
				return null;
			}
			T t = to.newInstance();
			org.springframework.beans.BeanUtils.copyProperties(source, t);
			return t;
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | SecurityException e) {
			LOGGER.error(LogMsg.to("ex", e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.ILLEGAL_EXCEPTION,
					BaseExceptionConstants.getMessage(BaseExceptionConstants.ILLEGAL_EXCEPTION), e);
		}
	}

	public static <S, T> List<T> copy(List<S> sources, Class<T> to) throws FrameworkRuntimeException {
		try {
			if (sources == null) {
				return new ArrayList<>();
			}
			List<T> ts = new ArrayList<>(sources.size());
			for (S source : sources) {
				T t = BeanUtils.copy(source, to);
				ts.add(t);
			}
			return ts;
		} catch (IllegalArgumentException | SecurityException e) {
			LOGGER.error(LogMsg.to("ex", e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.ILLEGAL_EXCEPTION,
					BaseExceptionConstants.getMessage(BaseExceptionConstants.ILLEGAL_EXCEPTION), e);
		}
	}

	public static <K, S, T> Map<K, T> copy(Map<K, S> maps, Class<T> to) throws FrameworkRuntimeException {
		try {
			if (maps == null) {
				return null;
			}
			Map<K, T> ms = new HashMap<>(maps.size());
			for (Entry<K, S> entry : maps.entrySet()) {
				K k = entry.getKey();
				S source = entry.getValue();
				T t = BeanUtils.copy(source, to);
				ms.put(k, t);
			}
			return ms;
		} catch (IllegalArgumentException | SecurityException e) {
			LOGGER.error(LogMsg.to("ex", e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.ILLEGAL_EXCEPTION,
					BaseExceptionConstants.getMessage(BaseExceptionConstants.ILLEGAL_EXCEPTION), e);
		}
	}

	public static <K, S, T> Map<K, List<T>> copyMapList(Map<K, List<S>> maps, Class<T> to)
			throws FrameworkRuntimeException {
		try {
			if (maps == null) {
				return null;
			}
			Map<K, List<T>> ms = new HashMap<>(maps.size());
			for (Entry<K, List<S>> entry : maps.entrySet()) {
				K k = entry.getKey();
				List<S> sources = entry.getValue();
				List<T> ts = BeanUtils.copy(sources, to);
				ms.put(k, ts);
			}
			return ms;
		} catch (IllegalArgumentException | SecurityException e) {
			LOGGER.error(LogMsg.to("ex", e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.ILLEGAL_EXCEPTION,
					BaseExceptionConstants.getMessage(BaseExceptionConstants.ILLEGAL_EXCEPTION), e);
		}
	}

	private static <T> void supperNews(T t, String enterpriseid)
			throws IllegalArgumentException, IllegalAccessException {
		Class<?> superClass = t.getClass().getSuperclass();
		Field[] superFields = superClass.getDeclaredFields();
		for (int i = 0; i < superFields.length; i++) {
			superFields[i].setAccessible(true);
			if ("enterpriseid".equals(superFields[i].getName())) {
				superFields[i].set(t, enterpriseid);
				break;
			}
		}
	}

	public static <T> T news(Class<T> clazz, String id, String enterpriseid) throws FrameworkRuntimeException {
		try {
			Field[] fields = clazz.getDeclaredFields();
			T t = clazz.newInstance();
			for (int i = 0; i < fields.length; i++) {
				fields[i].setAccessible(true);
				if ("id".equals(fields[i].getName())) {
					fields[i].set(t, id);
					break;
				}
			}
			supperNews(t, enterpriseid);
			return t;
		} catch (InstantiationException | IllegalAccessException e) {
			LOGGER.error(LogMsg.to("ex", e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.ILLEGAL_EXCEPTION,
					BaseExceptionConstants.getMessage(BaseExceptionConstants.ILLEGAL_EXCEPTION), e);
		}
	}

	public static <T> T news(Class<T> clazz, String id) throws FrameworkRuntimeException {
		try {
			Field[] fields = clazz.getDeclaredFields();
			T t = clazz.newInstance();
			for (int i = 0; i < fields.length; i++) {
				fields[i].setAccessible(true);
				if ("id".equals(fields[i].getName())) {
					fields[i].set(t, id);
					break;
				}
			}
			return t;
		} catch (InstantiationException | IllegalAccessException e) {
			LOGGER.error(LogMsg.to("ex", e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.ILLEGAL_EXCEPTION,
					BaseExceptionConstants.getMessage(BaseExceptionConstants.ILLEGAL_EXCEPTION), e);
		}
	}

	public static <T> T news(Class<T> clazz, Map<String, Object> params) throws FrameworkRuntimeException {
		try {
			Field[] fields = clazz.getDeclaredFields();
			T t = clazz.newInstance();
			for (int i = 0; i < fields.length; i++) {
				fields[i].setAccessible(true);

				for (Entry<String, Object> entry : params.entrySet()) {
					String key = entry.getKey();
					if (fields[i].getName().equals(key)) {
						fields[i].set(t, entry.getValue());
					}
				}
			}
			return t;
		} catch (InstantiationException | IllegalAccessException e) {
			LOGGER.error(LogMsg.to("ex", e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.ILLEGAL_EXCEPTION,
					BaseExceptionConstants.getMessage(BaseExceptionConstants.ILLEGAL_EXCEPTION), e);
		}
	}

	public static <T> T news(Class<T> clazz, Object... params) throws FrameworkRuntimeException {
		try {
			Field[] fields = clazz.getDeclaredFields();
			T t = clazz.newInstance();
			for (int i = 0; i < fields.length; i++) {
				fields[i].setAccessible(true);
				for (int j = 0; j < params.length; j = j + 2) {
					Object key = params[j];
					Object val = params[j + 1];
					if (fields[i].getName().equals(key)) {
						fields[i].set(t, val);
					}
				}
			}
			return t;
		} catch (InstantiationException | IllegalAccessException e) {
			LOGGER.error(LogMsg.to("ex", e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.ILLEGAL_EXCEPTION,
					BaseExceptionConstants.getMessage(BaseExceptionConstants.ILLEGAL_EXCEPTION), e);
		}
	}

	@SafeVarargs
	public static <T> List<T> list(T... ts) throws FrameworkRuntimeException {
		// 传递null,所以返回null
		if (ts == null || ts.length == 0) {
			return new ArrayList<>();
		}
		List<T> list = new ArrayList<>(ts.length);
		for (T t : ts) {
			list.add(t);
		}
		return list;
	}

	public static Map<String, Object> objToMap(Object obj) throws FrameworkRuntimeException {
		try {
			Field[] fields = obj.getClass().getDeclaredFields();
			Map<String, Object> map = new HashMap<>(fields.length);
			for (Field field : fields) {
				field.setAccessible(true);
				map.put(field.getName(), field.get(obj));
			}
			return map;
		} catch (IllegalAccessException e) {
			LOGGER.error(LogMsg.to("ex", e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.ILLEGAL_EXCEPTION,
					BaseExceptionConstants.getMessage(BaseExceptionConstants.ILLEGAL_EXCEPTION), e);
		}
	}

	public static Map<String, String> objToMapStr(Object obj) throws FrameworkRuntimeException {
		try {
			Field[] fields = obj.getClass().getDeclaredFields();
			Map<String, String> map = new HashMap<>(fields.length);
			for (Field field : fields) {
				field.setAccessible(true);
				map.put(field.getName(), String.valueOf(field.get(obj)));
			}
			return map;
		} catch (IllegalAccessException e) {
			LOGGER.error(LogMsg.to("ex", e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.ILLEGAL_EXCEPTION,
					BaseExceptionConstants.getMessage(BaseExceptionConstants.ILLEGAL_EXCEPTION), e);
		}
	}

}
