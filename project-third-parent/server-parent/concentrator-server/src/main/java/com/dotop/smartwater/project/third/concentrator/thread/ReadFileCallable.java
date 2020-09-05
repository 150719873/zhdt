package com.dotop.smartwater.project.third.concentrator.thread;

import com.dotop.smartwater.project.third.concentrator.core.bo.ConcentratorBo;
import com.dotop.smartwater.project.third.concentrator.core.bo.DownLinkTaskLogBo;
import com.dotop.smartwater.project.third.concentrator.core.vo.ConcentratorVo;
import com.dotop.smartwater.project.third.concentrator.thread.pool.ConcentratorCallable;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.utils.JSONUtils;
import com.dotop.smartwater.project.third.concentrator.client.netty.dc.model.form.DownLoadFileForm;
import com.dotop.smartwater.project.third.concentrator.core.constants.ConcentratorConstants;
import com.dotop.smartwater.project.third.concentrator.core.model.CallableParam;
import com.dotop.smartwater.project.third.concentrator.core.model.FutureResult;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Date;

/**
 * 读取档案
 *
 *
 */
public class ReadFileCallable extends ConcentratorCallable {

    private final static Logger LOGGER = LogManager.getLogger(ReadFileCallable.class);

    public ReadFileCallable(String enterpriseid, String taskLogId, String taskType, String concentratorCode, CallableParam callableParam) {
        super(enterpriseid, taskLogId, taskType, concentratorCode, callableParam);
    }

    @Override
    public FutureResult call() {
        LOGGER.info(LogMsg.to("concentratorCode", concentratorCode, "callableParam", callableParam));
        FutureResult futureResult = new FutureResult();
        try {
            // 集中器的拥有水表总数量，前端需要提示【抄表期间，不要重新下载档案】
            ConcentratorBo concentratorBo = new ConcentratorBo();
            concentratorBo.setCode(concentratorCode);
            concentratorBo.setEnterpriseid(enterpriseid);
            ConcentratorVo concentratorVo = iConcentratorService.getByCode(concentratorBo);
            if (concentratorVo == null) {
                futureResult.setResult(ConcentratorConstants.RESULT_FAIL);
                return futureResult;
            }
            DownLoadFileForm downLoadFileForm = new DownLoadFileForm();
            downLoadFileForm.setEnterpriseid(enterpriseid);
            downLoadFileForm.setTaskLogId(taskLogId);
            downLoadFileForm.setNum(concentratorCode);
            LOGGER.info(LogMsg.to("concentratorCode", concentratorCode, "callableParam", callableParam, "downLoadFileForm", downLoadFileForm));
            LOGGER.info(LogMsg.to("downLoadFileForm", downLoadFileForm));
            operationService.readFile(downLoadFileForm);
            futureResult.setResult(ConcentratorConstants.RESULT_SUCCESS);
            return futureResult;
        } catch (InterruptedException e) {
            LOGGER.info(LogMsg.to("ex", e, "concentratorCode", concentratorCode, "callableParam", callableParam));
            LOGGER.error(LogMsg.to(e));
            futureResult.setResult(ConcentratorConstants.RESULT_FAIL);
            return futureResult;
        } catch (FrameworkRuntimeException e) {
            LOGGER.info(LogMsg.to("ex", e, "concentratorCode", concentratorCode, "callableParam", callableParam));
            LOGGER.error(LogMsg.to(e));
            futureResult.setDesc(e.getMsg());
            futureResult.setResult(ConcentratorConstants.RESULT_FAIL);
            return futureResult;
        } catch (Exception e) {
            LOGGER.info(LogMsg.to("ex", e, "concentratorCode", concentratorCode, "callableParam", callableParam));
            LOGGER.error(LogMsg.to(e));
            futureResult.setResult(ConcentratorConstants.RESULT_FAIL);
            return futureResult;
        } finally {
            LOGGER.info(LogMsg.to("concentratorCode", concentratorCode, "callableParam", callableParam, "futureResult", futureResult));
            if (ConcentratorConstants.RESULT_FAIL.equals(futureResult.getResult())) {
                // 如果调用异常，应该需要修改为任务失败
                Date resultDate = new Date();
                DownLinkTaskLogBo downLinkTaskLogBo = new DownLinkTaskLogBo();
                downLinkTaskLogBo.setId(taskLogId);
                downLinkTaskLogBo.setResult(futureResult.getResult());
                downLinkTaskLogBo.setCompleteDate(resultDate);
                downLinkTaskLogBo.setResultData(JSONUtils.toJSONString(futureResult));
                downLinkTaskLogBo.setEnterpriseid(callableParam.getEnterpriseid());
                downLinkTaskLogBo.setUserBy(callableParam.getUserBy());
                downLinkTaskLogBo.setCurr(resultDate);
                downLinkTaskLogBo.setDesc(futureResult.getDesc());
                iDownLinkTaskLogService.edit(downLinkTaskLogBo);
            }
        }
    }
}
