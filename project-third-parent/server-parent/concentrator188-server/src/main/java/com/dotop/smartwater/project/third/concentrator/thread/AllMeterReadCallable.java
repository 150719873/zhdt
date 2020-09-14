package com.dotop.smartwater.project.third.concentrator.thread;

import com.dotop.smartwater.project.third.concentrator.core.bo.ConcentratorBo;
import com.dotop.smartwater.project.third.concentrator.core.bo.DownLinkTaskLogBo;
import com.dotop.smartwater.project.third.concentrator.core.vo.ConcentratorDeviceVo;
import com.dotop.smartwater.project.third.concentrator.core.vo.ConcentratorVo;
import com.dotop.smartwater.project.third.concentrator.thread.pool.ConcentratorCallable;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.utils.JSONUtils;
import com.dotop.smartwater.project.third.concentrator.client.netty.dc.model.form.DownLoadFileForm;
import com.dotop.smartwater.project.third.concentrator.core.constants.ConcentratorConstants;
import com.dotop.smartwater.project.third.concentrator.core.form.ConcentratorDeviceForm;
import com.dotop.smartwater.project.third.concentrator.core.form.ConcentratorForm;
import com.dotop.smartwater.project.third.concentrator.core.model.CallableParam;
import com.dotop.smartwater.project.third.concentrator.core.model.FutureResult;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

/**
 * 全部抄表
 *
 *
 */
public class AllMeterReadCallable extends ConcentratorCallable {

    private final static Logger LOGGER = LogManager.getLogger(AllMeterReadCallable.class);

    public AllMeterReadCallable(String enterpriseid, String taskLogId, String taskType, String concentratorCode, CallableParam callableParam) {
        super(enterpriseid, taskLogId, taskType, concentratorCode, callableParam);
    }

    @Override
    public FutureResult call() {
        LOGGER.info(LogMsg.to("concentratorCode", concentratorCode, "callableParam", callableParam));
        FutureResult futureResult = new FutureResult();
        List<String> nos = new ArrayList<>();
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
            // 判断是否需要更新序号的
            Integer reordering = concentratorVo.getReordering();
            if (ConcentratorConstants.NEED_REORDERING.equals(reordering)) {
                futureResult.setResult(ConcentratorConstants.RESULT_FAIL);
                futureResult.setDesc("集中器对应的水表序号需要重新下载档案");
                return futureResult;
            }
            // 集中器的所有序号、采集器号、水表号的集合
            ConcentratorDeviceForm concentratorDeviceForm = new ConcentratorDeviceForm();
            ConcentratorForm concentratorForm = new ConcentratorForm();
            concentratorForm.setId(concentratorVo.getId());
            concentratorDeviceForm.setConcentrator(concentratorForm);
            concentratorDeviceForm.setEnterpriseid(enterpriseid);
            List<ConcentratorDeviceVo> devices = iConcentratorDeviceFactory.reordering(concentratorDeviceForm);
            if (devices == null || devices.isEmpty()) {
                LOGGER.info(LogMsg.to("concentratorCode", concentratorCode, "callableParam", callableParam));
                futureResult.setResult(ConcentratorConstants.RESULT_FAIL);
                return futureResult;
            }

            for (ConcentratorDeviceVo device : devices) {
                nos.add(String.valueOf(device.getNo()));
            }
            Collections.sort(nos, new Comparator<String>() {
                @Override
                public int compare(String o1, String o2) {
                    int i1 = Integer.valueOf(o1);
                    int i2 = Integer.valueOf(o2);
                    if (i1 > i2) {
                        return 1;
                    } else if (i1 < i2) {
                        return -1;
                    } else {
                        return 0;
                    }
                }
            });
            DownLoadFileForm downLoadFileForm = new DownLoadFileForm();
            downLoadFileForm.setEnterpriseid(enterpriseid);
            downLoadFileForm.setTaskLogId(taskLogId);
            downLoadFileForm.setNum(concentratorCode);
            downLoadFileForm.setNos(nos);
            operationService.readAll(downLoadFileForm);
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
                downLinkTaskLogBo.setFailNum(String.valueOf(nos.size()));
                iDownLinkTaskLogService.edit(downLinkTaskLogBo);
            }
        }
    }
}