package com.dotop.smartwater.project.third.concentrator.thread;

import com.dotop.smartwater.project.third.concentrator.core.bo.ConcentratorBo;
import com.dotop.smartwater.project.third.concentrator.core.bo.ConcentratorDeviceBo;
import com.dotop.smartwater.project.third.concentrator.core.bo.DownLinkTaskLogBo;
import com.dotop.smartwater.project.third.concentrator.core.vo.ConcentratorDeviceVo;
import com.dotop.smartwater.project.third.concentrator.core.vo.ConcentratorVo;
import com.dotop.smartwater.project.third.concentrator.thread.pool.ConcentratorCallable;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.utils.JSONUtils;
import com.dotop.smartwater.project.third.concentrator.client.netty.dc.model.form.DeviceForm;
import com.dotop.smartwater.project.third.concentrator.core.constants.ConcentratorConstants;
import com.dotop.smartwater.project.third.concentrator.core.model.CallableParam;
import com.dotop.smartwater.project.third.concentrator.core.model.FutureResult;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Date;

/**
 * 单表抄表
 *
 *
 */
public class MeterReadCallable extends ConcentratorCallable {

    private final static Logger LOGGER = LogManager.getLogger(MeterReadCallable.class);

    public MeterReadCallable(String enterpriseid, String taskLogId, String taskType, String concentratorCode, CallableParam callableParam) {
        super(enterpriseid, taskLogId, taskType, concentratorCode, callableParam);
    }

    @Override
    public FutureResult call() {
        LOGGER.info(LogMsg.to("concentratorCode", concentratorCode, "callableParam", callableParam));
        FutureResult futureResult = new FutureResult();
        try {
            // 校验集中器是否存在
            ConcentratorBo concentratorBo = new ConcentratorBo();
            concentratorBo.setCode(concentratorCode);
            concentratorBo.setEnterpriseid(enterpriseid);
            ConcentratorVo concentratorVo = iConcentratorService.getByCode(concentratorBo);
            if (concentratorVo == null) {
                futureResult.setResult(ConcentratorConstants.RESULT_FAIL);
                futureResult.setDesc("集中器不存在");
                return futureResult;
            }
            // 判断是否需要更新序号的
            Integer reordering = concentratorVo.getReordering();
            if (ConcentratorConstants.NEED_REORDERING.equals(reordering)) {
                futureResult.setResult(ConcentratorConstants.RESULT_FAIL);
                futureResult.setDesc("集中器对应的水表序号需要重新下载档案");
                return futureResult;
            }
            // 校验水表是否存在
            ConcentratorDeviceBo concentratorDeviceBo = new ConcentratorDeviceBo();
            concentratorDeviceBo.setDevno(callableParam.getDevno());
            concentratorDeviceBo.setEnterpriseid(enterpriseid);
            // 集中器的拥有水表号转换为序号
            ConcentratorDeviceVo concentratorDeviceVo = iConcentratorDeviceService.get(concentratorDeviceBo);
            if (concentratorDeviceVo == null) {
                futureResult.setResult(ConcentratorConstants.RESULT_FAIL);
                futureResult.setDesc("水表不存在");
                return futureResult;
            }
            Integer devnum = concentratorDeviceVo.getNo();
            DeviceForm deviceForm = new DeviceForm();
            deviceForm.setEnterpriseid(enterpriseid);
            deviceForm.setTaskLogId(taskLogId);
            deviceForm.setNum(concentratorCode);
            deviceForm.setDevnum(devnum);
            operationService.readOne(deviceForm);
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
                downLinkTaskLogBo.setSuccessNum("0");
                downLinkTaskLogBo.setFailNum("1");
                iDownLinkTaskLogService.edit(downLinkTaskLogBo);
            }
        }
    }
}