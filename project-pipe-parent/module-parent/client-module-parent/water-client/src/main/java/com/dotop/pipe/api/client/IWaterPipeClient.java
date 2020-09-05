package com.dotop.pipe.api.client;

import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;

/**
 * 对接水务平台管漏对接数据信息
 *
 *
 */
public interface IWaterPipeClient {

    /**
     *  管漏订阅绑定
     */
    String deviceSubscribeBind(String enterpriseid,String devno) throws FrameworkRuntimeException;

    /**
     * 管漏订阅删除
     */
    String deviceSubscribeDel(String enterpriseid,String devno) throws FrameworkRuntimeException;


}
