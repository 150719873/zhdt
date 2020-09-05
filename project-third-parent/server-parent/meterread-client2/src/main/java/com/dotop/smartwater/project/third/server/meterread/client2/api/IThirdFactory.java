package com.dotop.smartwater.project.third.server.meterread.client2.api;

import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;

public interface IThirdFactory {

    /**
     * 刷新设备上行读数
     *
     * @throws FrameworkRuntimeException
     */
    void refreshDeviceUplinks( ) throws FrameworkRuntimeException;

}
