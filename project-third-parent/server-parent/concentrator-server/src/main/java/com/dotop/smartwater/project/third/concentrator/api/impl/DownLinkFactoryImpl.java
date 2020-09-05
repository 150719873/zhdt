package com.dotop.smartwater.project.third.concentrator.api.impl;

import com.dotop.smartwater.project.third.concentrator.api.IDownLinkLogic;
import com.dotop.smartwater.dependence.cache.StringValueCache;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.utils.JSONUtils;
import com.dotop.smartwater.project.module.core.auth.local.IAuthCasClient;
import com.dotop.smartwater.project.third.concentrator.api.IDownLinkFactory;
import com.dotop.smartwater.project.third.concentrator.core.constants.CacheKey;
import com.dotop.smartwater.project.third.concentrator.core.constants.ConcentratorConstants;
import com.dotop.smartwater.project.third.concentrator.core.form.DownLinkForm;
import com.dotop.smartwater.project.third.concentrator.core.model.CallableParam;
import com.dotop.smartwater.project.third.concentrator.core.model.FutureResult;
import com.dotop.smartwater.project.third.concentrator.core.model.Heartbeat;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DownLinkFactoryImpl implements IDownLinkFactory, IAuthCasClient {

    private final static Logger LOGGER = LogManager.getLogger(DownLinkFactoryImpl.class);

    @Autowired
    private StringValueCache svc;

    @Autowired
    private IDownLinkLogic iDownLinkLogic;


    @Override
    public Heartbeat heartbeat(DownLinkForm downLinkForm) throws FrameworkRuntimeException {
        String concentratorCode = downLinkForm.getConcentrator().getCode();
        String val = svc.get(CacheKey.HEARTBEAT + concentratorCode);
        if (StringUtils.isBlank(val)) {
            Heartbeat heartbeat = new Heartbeat();
            heartbeat.setConcentratorCode(concentratorCode);
            heartbeat.setIsOnline(ConcentratorConstants.ONLINE_OFFLINE);
            return heartbeat;
        }
        return JSONUtils.parseObject(val, Heartbeat.class);
    }

    @Override
    public FutureResult downloadFiles(DownLinkForm downLinkForm) throws FrameworkRuntimeException {
        String concentratorCode = downLinkForm.getConcentrator().getCode();
        String taskType = downLinkForm.getTaskType();
        FutureResult result = iDownLinkLogic.send(getEnterpriseid(), taskType, concentratorCode, false, new CallableParam(getEnterpriseid(), getAccount()));
        return result;
    }

    @Override
    public FutureResult readFiles(DownLinkForm downLinkForm) throws FrameworkRuntimeException {
        String concentratorCode = downLinkForm.getConcentrator().getCode();
        String taskType = downLinkForm.getTaskType();
        FutureResult result = iDownLinkLogic.send(getEnterpriseid(), taskType, concentratorCode, false, new CallableParam(getEnterpriseid(), getAccount()));
        return result;
    }

    @Override
    public FutureResult allMeterRead(DownLinkForm downLinkForm) throws FrameworkRuntimeException {
        String concentratorCode = downLinkForm.getConcentrator().getCode();
        String taskType = downLinkForm.getTaskType();
        FutureResult result = iDownLinkLogic.send(getEnterpriseid(), taskType, concentratorCode, false, new CallableParam(getEnterpriseid(), getAccount()));
        return result;
    }

    @Override
    public FutureResult readIsAllowUplinkData(DownLinkForm downLinkForm) throws FrameworkRuntimeException {
        String concentratorCode = downLinkForm.getConcentrator().getCode();
        String taskType = downLinkForm.getTaskType();
        FutureResult result = iDownLinkLogic.send(getEnterpriseid(), taskType, concentratorCode, true, new CallableParam(getEnterpriseid(), getAccount()));
        return result;
    }

    @Override
    public FutureResult setIsAllowUplinkData(DownLinkForm downLinkForm) throws FrameworkRuntimeException {
        String concentratorCode = downLinkForm.getConcentrator().getCode();
        String taskType = downLinkForm.getTaskType();
        CallableParam callableParam = new CallableParam(getEnterpriseid(), getAccount());
        callableParam.setSetIsAllowUplinkData(downLinkForm.getSetIsAllowUplinkData());
        FutureResult result = iDownLinkLogic.send(getEnterpriseid(), taskType, concentratorCode, true, callableParam);
        return result;
    }

    @Override
    public FutureResult readUplinkDate(DownLinkForm downLinkForm) throws FrameworkRuntimeException {
        String concentratorCode = downLinkForm.getConcentrator().getCode();
        String taskType = downLinkForm.getTaskType();
        FutureResult result = iDownLinkLogic.send(getEnterpriseid(), taskType, concentratorCode, true, new CallableParam(getEnterpriseid(), getAccount()));
        return result;
    }

    @Override
    public FutureResult setUplinkDate(DownLinkForm downLinkForm) throws FrameworkRuntimeException {
        String concentratorCode = downLinkForm.getConcentrator().getCode();
        String taskType = downLinkForm.getTaskType();
        CallableParam callableParam = new CallableParam(getEnterpriseid(), getAccount());
        callableParam.setSetUplinkDate(downLinkForm.getSetUplinkDate());
        callableParam.setSetUplinkDateType(downLinkForm.getSetUplinkDateType());
        FutureResult result = iDownLinkLogic.send(getEnterpriseid(), taskType, concentratorCode, true, callableParam);
        return result;
    }

    @Override
    public FutureResult readGprs(DownLinkForm downLinkForm) throws FrameworkRuntimeException {
        String concentratorCode = downLinkForm.getConcentrator().getCode();
        String taskType = downLinkForm.getTaskType();
        FutureResult result = iDownLinkLogic.send(getEnterpriseid(), taskType, concentratorCode, true, new CallableParam(getEnterpriseid(), getAccount()));
        return result;
    }

    @Override
    public FutureResult setGprs(DownLinkForm downLinkForm) throws FrameworkRuntimeException {
        String concentratorCode = downLinkForm.getConcentrator().getCode();
        String taskType = downLinkForm.getTaskType();
        CallableParam callableParam = new CallableParam(getEnterpriseid(), getAccount());
        callableParam.setSetGprsIp(downLinkForm.getSetGprsIp());
        callableParam.setSetGprsPort(downLinkForm.getSetGprsPort());
        FutureResult result = iDownLinkLogic.send(getEnterpriseid(), taskType, concentratorCode, true, callableParam);
        return result;
    }


    @Override
    public FutureResult readDate(DownLinkForm downLinkForm) throws FrameworkRuntimeException {
        String concentratorCode = downLinkForm.getConcentrator().getCode();
        String taskType = downLinkForm.getTaskType();
        FutureResult result = iDownLinkLogic.send(getEnterpriseid(), taskType, concentratorCode, true, new CallableParam(getEnterpriseid(), getAccount()));
        return result;
    }

    @Override
    public FutureResult setDate(DownLinkForm downLinkForm) throws FrameworkRuntimeException {
        String concentratorCode = downLinkForm.getConcentrator().getCode();
        String taskType = downLinkForm.getTaskType();
        CallableParam callableParam = new CallableParam(getEnterpriseid(), getAccount());
        callableParam.setSetDate(downLinkForm.getSetDate());
        FutureResult result = iDownLinkLogic.send(getEnterpriseid(), taskType, concentratorCode, true, callableParam);
        return result;
    }

    @Override
    public FutureResult meterRead(DownLinkForm downLinkForm) throws FrameworkRuntimeException {
        String concentratorCode = downLinkForm.getConcentrator().getCode();
        String taskType = downLinkForm.getTaskType();
        CallableParam callableParam = new CallableParam(getEnterpriseid(), getAccount());
        callableParam.setDevno(downLinkForm.getDevno());
        FutureResult result = iDownLinkLogic.send(getEnterpriseid(), taskType, concentratorCode, false, callableParam);
        return result;
    }

    @Override
    public FutureResult valveOper(DownLinkForm downLinkForm) throws FrameworkRuntimeException {
        String concentratorCode = downLinkForm.getConcentrator().getCode();
        String taskType = downLinkForm.getTaskType();
        CallableParam callableParam = new CallableParam(getEnterpriseid(), getAccount());
        callableParam.setDevno(downLinkForm.getDevno());
        callableParam.setValveOper(downLinkForm.getValveOper());
        FutureResult result = iDownLinkLogic.send(getEnterpriseid(), taskType, concentratorCode, true, callableParam);
        return result;
    }
}
