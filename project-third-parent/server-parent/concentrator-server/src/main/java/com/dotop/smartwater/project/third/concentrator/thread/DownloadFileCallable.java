package com.dotop.smartwater.project.third.concentrator.thread;

import com.dotop.smartwater.project.third.concentrator.core.bo.ConcentratorBo;
import com.dotop.smartwater.project.third.concentrator.core.bo.DownLinkTaskLogBo;
import com.dotop.smartwater.project.third.concentrator.core.vo.CollectorVo;
import com.dotop.smartwater.project.third.concentrator.core.vo.ConcentratorDeviceVo;
import com.dotop.smartwater.project.third.concentrator.core.vo.ConcentratorVo;
import com.dotop.smartwater.project.third.concentrator.thread.pool.ConcentratorCallable;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.utils.JSONUtils;
import com.dotop.smartwater.project.third.concentrator.client.netty.dc.model.form.DeviceForm;
import com.dotop.smartwater.project.third.concentrator.client.netty.dc.model.form.DownLoadFileForm;
import com.dotop.smartwater.project.third.concentrator.core.constants.ConcentratorConstants;
import com.dotop.smartwater.project.third.concentrator.core.form.ConcentratorDeviceForm;
import com.dotop.smartwater.project.third.concentrator.core.form.ConcentratorForm;
import com.dotop.smartwater.project.third.concentrator.core.model.CallableParam;
import com.dotop.smartwater.project.third.concentrator.core.model.FutureResult;
import com.dotop.smartwater.project.third.concentrator.core.utils.ConcentratorUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 下载档案
 *
 *
 */
public class DownloadFileCallable extends ConcentratorCallable {

    private final static Logger LOGGER = LogManager.getLogger(DownloadFileCallable.class);

    public DownloadFileCallable(String enterpriseid, String taskLogId, String taskType, String concentratorCode, CallableParam callableParam) {
        super(enterpriseid, taskLogId, taskType, concentratorCode, callableParam);
    }

    @Override
    public FutureResult call() {
        LOGGER.info(LogMsg.to("concentratorCode", concentratorCode, "callableParam", callableParam));
        FutureResult futureResult = new FutureResult();
        try {
            ConcentratorBo concentratorBo = new ConcentratorBo();
            concentratorBo.setCode(concentratorCode);
            concentratorBo.setEnterpriseid(enterpriseid);
            ConcentratorVo concentratorVo = iConcentratorService.getByCode(concentratorBo);
            if (concentratorVo == null) {
                futureResult.setResult(ConcentratorConstants.RESULT_FAIL);
                futureResult.setDesc("集中器不存在");
                return futureResult;
            }
            // 集中器的所有序号、采集器号、水表号的集合
            ConcentratorDeviceForm concentratorDeviceForm = new ConcentratorDeviceForm();
            ConcentratorForm concentratorForm = new ConcentratorForm();
            concentratorForm.setId(concentratorVo.getId());
            concentratorDeviceForm.setConcentrator(concentratorForm);
            concentratorDeviceForm.setEnterpriseid(enterpriseid);
            List<ConcentratorDeviceVo> devices = iConcentratorDeviceFactory.reordering(concentratorDeviceForm);
            LOGGER.info(LogMsg.to("concentratorCode", concentratorCode, "callableParam", callableParam, "devices", devices));
            if (devices == null || devices.isEmpty()) {
                LOGGER.info(LogMsg.to("concentratorCode", concentratorCode, "callableParam", callableParam));
                futureResult.setResult(ConcentratorConstants.RESULT_FAIL);
                futureResult.setDesc("集中器不存在设备");
                return futureResult;
            }
            // 转换下发档案
            List<DeviceForm> list = new ArrayList<>();
            for (ConcentratorDeviceVo device : devices) {
                DeviceForm deviceForm = new DeviceForm();
                list.add(deviceForm);
                deviceForm.setDevnum(device.getNo());
                CollectorVo collectorVo = device.getCollector();
                // 组装采集器编号
//                String repeaterno = ConcentratorUtils.getCollectorCode(collectorVo.getChannel(), collectorVo.getCode(), device.getChannel());
                String repeaterno = ConcentratorUtils.getCollectorCode(collectorVo.getCode(), device.getChannel());
                if (StringUtils.isBlank(repeaterno)) {
                    LOGGER.info(LogMsg.to("concentratorCode", concentratorCode, "callableParam", callableParam, "collectorVo", collectorVo, "device", device));
                    futureResult.setResult(ConcentratorConstants.RESULT_FAIL);
                    futureResult.setDesc("集中器的设备序号异常，请联系管理员");
                    return futureResult;
                }
                deviceForm.setRepeaterno(repeaterno);
                LOGGER.info(LogMsg.to("concentratorCode", concentratorCode, "callableParam", callableParam, "repeaterno", deviceForm.getRepeaterno()));
                deviceForm.setDevno(device.getDevno());
            }
            LOGGER.info(LogMsg.to("concentratorCode", concentratorCode, "callableParam", callableParam, "list", list));
            DownLoadFileForm downLoadFileForm = new DownLoadFileForm();
            downLoadFileForm.setEnterpriseid(enterpriseid);
            downLoadFileForm.setTaskLogId(taskLogId);
            downLoadFileForm.setNum(concentratorCode);
            downLoadFileForm.setList(list);

            operationService.downloadFile(downLoadFileForm);
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
