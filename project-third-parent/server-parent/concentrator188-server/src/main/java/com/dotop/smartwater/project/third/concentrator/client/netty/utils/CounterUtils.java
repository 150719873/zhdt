package com.dotop.smartwater.project.third.concentrator.client.netty.utils;

import com.dotop.smartwater.dependence.cache.StringValueCache;
import com.dotop.smartwater.project.third.concentrator.core.constants.CacheKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @program: project-parent
 * @description: 计数器
 *
 * @create: 2019-06-21 17:10
 **/
@Service
public class CounterUtils {

    @Autowired
    private StringValueCache redisDao;


    private final int hex = 16;

    /**
     * num 是短地址
     **/

    public void reSetCounter(String num) {
        redisDao.set(CacheKey.COUNTER_KEY + num, String.valueOf(0));
        redisDao.set(CacheKey.COUNTER_VALUE + num, String.valueOf(0));
    }

    public String getCounter(String num) {
        String counter = redisDao.get(CacheKey.COUNTER_KEY + num);
        if (StrUtil.isBlank(counter)) {
            reSetCounter(num);
            return "0";
        }
        return counter;
    }

    public String getCounterValue(String num) {
        String value = redisDao.get(CacheKey.COUNTER_VALUE + num);
        if (StrUtil.isBlank(value)) {
            reSetCounter(num);
            return "0";
        }
        return value;
    }

    public void autoCounterAdd(String num) {
        Long counter = 0L;
        if (StrUtil.isNotBlank(redisDao.get(CacheKey.COUNTER_VALUE + num))) {
            counter = Long.parseLong(redisDao.get(CacheKey.COUNTER_VALUE + num));
        }

        counter = counter + 1;
        redisDao.set(CacheKey.COUNTER_KEY + num, String.valueOf(Long.toHexString(counter % hex).toUpperCase()));
        redisDao.set(CacheKey.COUNTER_VALUE + num, String.valueOf(counter));
    }

}
