package com.dotop.smartwater.project.third.meterread.webservice.api;

import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;

/**
 * 水务系统业务逻辑处理层
 *
 *
 */
public interface IWaterFactory {

    /**
     * 检查业主信息
     *
     * @throws FrameworkRuntimeException
     */
    void checkOwners(String enterpriseid, Integer factoryId, String ownerid, Integer page, Integer pageCount) throws FrameworkRuntimeException;

    /**
     * 刷新设备上行读数
     *
     * @throws FrameworkRuntimeException
     */
    void refreshDeviceUplinks(String enterpriseid, Integer factoryId, String deveui, String devno, Integer page, Integer pageCount) throws FrameworkRuntimeException;
}
