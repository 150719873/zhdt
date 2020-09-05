package com.dotop.smartwater.project.third.concentrator.thread;

import com.dotop.smartwater.project.third.concentrator.core.bo.ConcentratorBo;
import com.dotop.smartwater.project.third.concentrator.core.bo.ConcentratorDeviceBo;
import com.dotop.smartwater.project.third.concentrator.core.vo.ConcentratorDeviceVo;
import com.dotop.smartwater.project.third.concentrator.core.vo.ConcentratorVo;
import com.dotop.smartwater.project.third.concentrator.thread.pool.ConcentratorCallable;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.project.module.core.water.constants.WaterConstants;
import com.dotop.smartwater.project.third.concentrator.client.netty.dc.model.form.DeviceForm;
import com.dotop.smartwater.project.third.concentrator.core.constants.ConcentratorConstants;
import com.dotop.smartwater.project.third.concentrator.core.model.CallableParam;
import com.dotop.smartwater.project.third.concentrator.core.model.FutureResult;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 开关阀操作
 *
 *
 */
public class ValveOperCallable extends ConcentratorCallable {

    private final static Logger LOGGER = LogManager.getLogger(ValveOperCallable.class);

    public ValveOperCallable(String enterpriseid, String taskLogId, String taskType, String concentratorCode, CallableParam callableParam) {
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
            ConcentratorDeviceVo concentratorDeviceVo = iConcentratorDeviceService.get(concentratorDeviceBo);
            if (concentratorDeviceVo == null) {
                futureResult.setResult(ConcentratorConstants.RESULT_FAIL);
                futureResult.setDesc("水表不存在");
                return futureResult;
            }
            DeviceForm deviceForm = new DeviceForm();
            deviceForm.setEnterpriseid(enterpriseid);
            deviceForm.setTaskLogId(taskLogId);
            deviceForm.setNum(concentratorCode);
            deviceForm.setDevno(callableParam.getDevno());
            deviceForm.setDevnum(concentratorDeviceVo.getNo());
            //  开关阀操作
            String result = null;
            Integer tapstatus = null;
            if (ConcentratorConstants.TASK_TYPE_DEVICE_OPEN.equals(taskType)) {
                result = operationService.open(deviceForm);
                tapstatus = WaterConstants.DEVICE_TAP_STATUS_ON;
            } else if (ConcentratorConstants.TASK_TYPE_DEVICE_CLOSE.equals(taskType)) {
                result = operationService.close(deviceForm);
                tapstatus = WaterConstants.DEVICE_TAP_STATUS_OFF;
            }
            if (ConcentratorConstants.RESULT_SUCCESS.equals(result)) {
                futureResult.setResult(ConcentratorConstants.RESULT_SUCCESS);
                //  更新水表为开阀或关阀状态
                concentratorDeviceBo = new ConcentratorDeviceBo();
                concentratorDeviceBo.setDevid(concentratorDeviceVo.getDevid());
                concentratorDeviceBo.setEnterpriseid(enterpriseid);
                concentratorDeviceBo.setTapstatus(tapstatus);
                iConcentratorDeviceService.setTapstatus(concentratorDeviceBo);
            } else {
                futureResult.setResult(ConcentratorConstants.RESULT_FAIL);
                futureResult.setDesc("开关阀操作失败");
            }
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
