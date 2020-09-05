package com.dotop.smartwater.dependence.cache;

import com.dotop.smartwater.dependence.cache.api.AbstractListCache;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**

 * @date 2019年5月8日
 * @description val值为string的list
 */
public class StringListCache extends AbstractListCache<String> {

    @Override
    public String rightPop(String key) throws FrameworkRuntimeException {
        if (StringUtils.isNotBlank(key)) {
            String str = cacheOpsForList.rightPop(key);
            if (StringUtils.isNotBlank(str)) {
                return str;
            }
        }
        return null;
    }

    @Override
    public String rightPop(String key, long timeout) throws FrameworkRuntimeException {
        if (StringUtils.isNotBlank(key)) {
            String str = cacheOpsForList.rightPop(key, timeout, TimeUnit.SECONDS);
            if (StringUtils.isNotBlank(str)) {
                return str;
            }
        }
        return null;
    }

    @Override
    public List<String> lrange(String key, long start, long end) throws FrameworkRuntimeException {
        if (StringUtils.isNotBlank(key)) {
            return cacheOpsForList.range(key, start, end);
        }
        return new ArrayList<>();
    }
}
