package com.dotop.pipe.api.cache.point;


import com.dotop.pipe.core.vo.point.PointVo;
import com.dotop.smartwater.dependence.cache.api.AbstractBaseCache;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;

import java.util.List;

public abstract class IPointCache extends AbstractBaseCache<String, PointVo, String> {

    /**
     * 查询缓存
     *
     * @return
     * @throws FrameworkRuntimeException
     */
    public abstract List<PointVo> getByDeviceCode(String deviceId) throws FrameworkRuntimeException;


    /**
     * 把点放入缓存中
     *
     * @param deviceId
     * @param points
     * @throws FrameworkRuntimeException
     */
    public abstract void setPointCache(String deviceId, List<PointVo> points) throws FrameworkRuntimeException;

}