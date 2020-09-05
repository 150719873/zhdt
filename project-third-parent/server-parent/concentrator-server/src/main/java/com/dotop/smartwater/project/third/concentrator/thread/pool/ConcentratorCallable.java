package com.dotop.smartwater.project.third.concentrator.thread.pool;

import com.dotop.smartwater.project.third.concentrator.service.IConcentratorDeviceService;
import com.dotop.smartwater.project.third.concentrator.service.IDownLinkTaskLogService;
import com.dotop.smartwater.project.third.concentrator.api.IConcentratorDeviceFactory;
import com.dotop.smartwater.project.third.concentrator.client.netty.dc.service.OperationService;
import com.dotop.smartwater.project.third.concentrator.core.model.CallableParam;
import com.dotop.smartwater.project.third.concentrator.core.model.FutureResult;
import com.dotop.smartwater.project.third.concentrator.core.utils.SpringContextUtils;
import com.dotop.smartwater.project.third.concentrator.service.IConcentratorService;

import java.util.concurrent.Callable;

/**
 *
 */
public abstract class ConcentratorCallable implements Callable<FutureResult> {

    protected OperationService operationService = (OperationService) SpringContextUtils.getBean("operationService");

    protected IConcentratorDeviceFactory iConcentratorDeviceFactory = (IConcentratorDeviceFactory) SpringContextUtils.getBean("IConcentratorDeviceFactory");

    protected IConcentratorService iConcentratorService = (IConcentratorService) SpringContextUtils.getBean("IConcentratorService");

    protected IConcentratorDeviceService iConcentratorDeviceService = (IConcentratorDeviceService) SpringContextUtils.getBean("IConcentratorDeviceService");

    protected IDownLinkTaskLogService iDownLinkTaskLogService = (IDownLinkTaskLogService) SpringContextUtils.getBean("IDownLinkTaskLogService");

    protected String enterpriseid;

    protected String taskLogId;

    protected String taskType;

    protected String concentratorCode;

    protected CallableParam callableParam;

    public ConcentratorCallable(String enterpriseid, String taskLogId, String taskType, String concentratorCode, CallableParam callableParam) {
        this.enterpriseid = enterpriseid;
        this.taskLogId = taskLogId;
        this.taskType = taskType;
        this.concentratorCode = concentratorCode;
        this.callableParam = callableParam;
    }
}
