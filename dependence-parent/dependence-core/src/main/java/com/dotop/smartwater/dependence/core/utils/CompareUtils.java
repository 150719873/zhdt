package com.dotop.smartwater.dependence.core.utils;

import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.vo.CompareVo;
import com.dotop.smartwater.dependence.core.exception.BaseExceptionConstants;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class CompareUtils {

    private final static Logger logger = LogManager.getLogger(CompareUtils.class);

    public final static <T> Map<String, CompareVo> compare(T obj1, T obj2, Set<String> filters)
            throws FrameworkRuntimeException {
        try {
            Map<String, CompareVo> result = new HashMap<String, CompareVo>(2);
            Map<String, Object> map1 = objectToMap(obj1);
            Map<String, Object> map2 = objectToMap(obj2);

            Iterator<Entry<String, Object>> iter1 = map1.entrySet().iterator();
            while (iter1.hasNext()) {
                Entry<String, Object> entry1 = (Entry<String, Object>) iter1.next();
                String key = entry1.getKey();
                if (filters != null && filters.contains(key)) {
                    continue;
                }
                // 修改后值
                Object m1value = entry1.getValue();
                // 修改前值
                Object m2value = map2.get(entry1.getKey());
                boolean flag = equals(m1value, m2value);
                if (!flag) {
                    CompareVo compareVo = new CompareVo(key, m2value, m1value);
                    result.put(key, compareVo);
                }
            }

            return result;
        } catch (SecurityException | IllegalArgumentException | IllegalAccessException e) {
            logger.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.ILLEGAL_EXCEPTION);
        }
    }

    public final static boolean equals(Object obj1, Object obj2) {
        if (obj1 == obj2) {
            return true;
        }
        if (obj1 == null || obj2 == null) {
            return false;
        }
        return obj1.equals(obj2);
    }

    public final static Map<String, Object> objectToMap(Object obj) throws IllegalAccessException {
        Map<String, Object> map = new HashMap<>(2);
        Class<?> clazz = obj.getClass();
        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            String fieldName = field.getName();
            Object value = field.get(obj);
            map.put(fieldName, value);
        }
        return map;
    }
}
