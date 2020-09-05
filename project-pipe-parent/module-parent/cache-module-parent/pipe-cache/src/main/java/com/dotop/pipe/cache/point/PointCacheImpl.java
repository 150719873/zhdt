package com.dotop.pipe.cache.point;

import com.dotop.pipe.api.cache.point.IPointCache;
import com.dotop.pipe.core.vo.point.PointVo;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.utils.JSONUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class PointCacheImpl extends IPointCache {

    @Override
    public List<PointVo> getByDeviceCode(String deviceId) throws FrameworkRuntimeException {
        List<PointVo> list = new ArrayList<>();
        return list;
    }

    @Override
    public void setPointCache(String deviceId, List<PointVo> points) throws FrameworkRuntimeException {
        // 封装成list
        String str = JSONUtils.toJSONString(points);
        cacheOpsForList.leftPushAll(deviceId, str);
        // cacheOpsForHash.put(deviceId, "aaa", str);
        //  cacheRedisTemplate.expire(CacheKey.deviceProperty(deviceId), devicePropertyExpire, TimeUnit.SECONDS);
    }
}
