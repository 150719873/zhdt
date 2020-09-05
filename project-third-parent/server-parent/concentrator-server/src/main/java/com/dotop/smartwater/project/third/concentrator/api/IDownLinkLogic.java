package com.dotop.smartwater.project.third.concentrator.api;

import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.project.third.concentrator.core.model.CallableParam;
import com.dotop.smartwater.project.third.concentrator.core.model.FutureResult;

/**
 * 下发命令逻辑
 *
 *
 */
public interface IDownLinkLogic {

    /**
     * 下发命令
     */
    FutureResult send(String enterpriseid, String taskType, String concentratorCode, boolean ifWait, CallableParam callableParam) throws FrameworkRuntimeException;

}
