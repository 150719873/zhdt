package com.dotop.smartwater.project.third.concentrator.thread;

import com.dotop.smartwater.project.third.concentrator.thread.pool.ConcentratorCallable;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.project.third.concentrator.client.netty.dc.model.form.UploadTimeForm;
import com.dotop.smartwater.project.third.concentrator.core.constants.ConcentratorConstants;
import com.dotop.smartwater.project.third.concentrator.core.model.CallableParam;
import com.dotop.smartwater.project.third.concentrator.core.model.FutureResult;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 读取-是否允许数据上报
 *
 *
 */
public class ReadIsAllowUplinkDataCallable extends ConcentratorCallable {

    private final static Logger LOGGER = LogManager.getLogger(ReadIsAllowUplinkDataCallable.class);

    public ReadIsAllowUplinkDataCallable(String enterpriseid, String taskLogId, String taskType, String concentratorCode, CallableParam callableParam) {
        super(enterpriseid, taskLogId, taskType, concentratorCode, callableParam);
    }

    @Override
    public FutureResult call() {
        LOGGER.info(LogMsg.to("concentratorCode", concentratorCode, "callableParam", callableParam));
        FutureResult futureResult = new FutureResult();
        try {
            UploadTimeForm uploadTimeForm = new UploadTimeForm();
            uploadTimeForm.setEnterpriseid(enterpriseid);
            uploadTimeForm.setTaskLogId(taskLogId);
            uploadTimeForm.setNum(concentratorCode);

            String uploadStatus = operationService.getUploadStatus(uploadTimeForm);
            futureResult.setResult(ConcentratorConstants.RESULT_SUCCESS);
            futureResult.setReadIsAllowUplinkData(uploadStatus);
        } catch (InterruptedException e) {
            LOGGER.info(LogMsg.to("ex", e, "concentratorCode", concentratorCode, "callableParam", callableParam));
            LOGGER.error(LogMsg.to(e));
            futureResult.setResult(ConcentratorConstants.RESULT_FAIL);
        } catch (FrameworkRuntimeException e) {
            LOGGER.info(LogMsg.to("ex", e, "concentratorCode", concentratorCode, "callableParam", callableParam));
            LOGGER.error(LogMsg.to(e));
            futureResult.setDesc(e.getMsg());
            futureResult.setResult(ConcentratorConstants.RESULT_FAIL);
        } catch (Exception e) {
            LOGGER.info(LogMsg.to("ex", e, "concentratorCode", concentratorCode, "callableParam", callableParam));
            LOGGER.error(LogMsg.to(e));
            futureResult.setResult(ConcentratorConstants.RESULT_FAIL);
        } finally {
            LOGGER.info(LogMsg.to("concentratorCode", concentratorCode, "callableParam", callableParam, "futureResult", futureResult));
        }
        return futureResult;
    }
}