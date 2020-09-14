package com.dotop.smartwater.project.third.concentrator.thread;

import com.dotop.smartwater.project.third.concentrator.thread.pool.ConcentratorCallable;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.project.third.concentrator.client.netty.dc.model.form.ConcentratorForm;
import com.dotop.smartwater.project.third.concentrator.client.netty.dc.model.vo.UploadTimeVo;
import com.dotop.smartwater.project.third.concentrator.core.constants.ConcentratorConstants;
import com.dotop.smartwater.project.third.concentrator.core.model.CallableParam;
import com.dotop.smartwater.project.third.concentrator.core.model.FutureResult;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 读取-上报时间
 *
 */
public class ReadUplinkDateCallable extends ConcentratorCallable {

    private final static Logger LOGGER = LogManager.getLogger(ReadDateCallable.class);

    public ReadUplinkDateCallable(String enterpriseid, String taskLogId, String taskType, String concentratorCode, CallableParam callableParam) {
        super(enterpriseid, taskLogId, taskType, concentratorCode, callableParam);
    }

    @Override
    public FutureResult call() {
        LOGGER.info(LogMsg.to("concentratorCode", concentratorCode, "callableParam", callableParam));
        FutureResult futureResult = new FutureResult();
        try {
            ConcentratorForm concentratorForm = new ConcentratorForm();
            concentratorForm.setEnterpriseid(enterpriseid);
            concentratorForm.setTaskLogId(taskLogId);
            concentratorForm.setNum(concentratorCode);

            UploadTimeVo uploadTimeVo = operationService.readUploadTime(concentratorForm);
            futureResult.setResult(ConcentratorConstants.RESULT_SUCCESS);
            // 时间格式：19-06-11 10:37:00
            futureResult.setReadUplinkDate(uploadTimeVo.getTime());
            futureResult.setReadUplinkDateType(uploadTimeVo.getType());
            futureResult.setReadUplinkDateTypeName(uploadTimeVo.getTypeName());
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