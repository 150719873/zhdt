package com.dotop.smartwater.dependence.cache;

import org.apache.commons.lang3.StringUtils;

import com.dotop.smartwater.dependence.cache.api.AbstractValueCache;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;

/**
 * 

 * @date 2019年5月8日
 * @description val值为string的keyval
 */
public class StringValueCache extends AbstractValueCache<String> {

	@Override
	public String get(String key) throws FrameworkRuntimeException {
		if (StringUtils.isNotBlank(key)) {
			String str = cacheOpsForValue.get(key);
			if (StringUtils.isNotBlank(str)) {
				return str;
			}
		}
		return null;
	}

}
