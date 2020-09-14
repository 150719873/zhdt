package com.dotop.smartwater.project.third.concentrator.rest.service;

import com.dotop.smartwater.project.third.concentrator.api.IDownLinkFactory;
import com.dotop.smartwater.dependence.core.common.BaseController;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.global.GlobalContext;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.utils.VerificationUtils;
import com.dotop.smartwater.project.module.core.water.constants.ResultCode;
import com.dotop.smartwater.project.third.concentrator.core.constants.ConcentratorConstants;
import com.dotop.smartwater.project.third.concentrator.core.form.DownLinkForm;
import com.dotop.smartwater.project.third.concentrator.core.model.FutureResult;
import com.dotop.smartwater.project.third.concentrator.core.model.Heartbeat;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * 下发命令入口层辑接口
 *
 *
 */
@RestController
@RequestMapping("/downLink")
public class DownLinkController implements BaseController<DownLinkForm> {

    private final static Logger LOGGER = LogManager.getLogger(DownLinkController.class);

    @Autowired
    private IDownLinkFactory iDownLinkFactory;

    /**
     * 在线状态检查
     */
    @PostMapping(value = "/heartbeat", produces = GlobalContext.PRODUCES)
    public String heartbeat(@RequestBody DownLinkForm downLinkForm) throws FrameworkRuntimeException {
        LOGGER.info(LogMsg.to("downLinkForm", downLinkForm));
        String concentratorCode = downLinkForm.getConcentrator().getCode();
        VerificationUtils.string("concentratorCode", concentratorCode);
        downLinkForm.setTaskType(ConcentratorConstants.TASK_TYPE_HEARTBEAT);
        Heartbeat heartbeat = iDownLinkFactory.heartbeat(downLinkForm);
        return resp(ResultCode.Success, ResultCode.SUCCESS, heartbeat);
    }

    /**
     * 下载档案
     */
    @PostMapping(value = "/downloadFiles", produces = GlobalContext.PRODUCES)
    public String downloadFiles(@RequestBody DownLinkForm downLinkForm) throws FrameworkRuntimeException {
        LOGGER.info(LogMsg.to("downLinkForm", downLinkForm));
        String concentratorCode = downLinkForm.getConcentrator().getCode();
        VerificationUtils.string("concentratorCode", concentratorCode);
        downLinkForm.setTaskType(ConcentratorConstants.TASK_TYPE_DOWNLOAD_FILE);
        try {
            FutureResult result = iDownLinkFactory.downloadFiles(downLinkForm);
            return resp(ResultCode.Success, ResultCode.SUCCESS, result);
        } catch (FrameworkRuntimeException e) {
            LOGGER.error(LogMsg.to("code", e.getCode(), "msg", e.getMsg()));
            return resp(e.getCode(), e.getMsg(), null);
        }
    }

    /**
     * 读取档案
     */
    @PostMapping(value = "/readFiles", produces = GlobalContext.PRODUCES)
    public String readFiles(@RequestBody DownLinkForm downLinkForm) throws FrameworkRuntimeException {
        LOGGER.info(LogMsg.to("downLinkForm", downLinkForm));
        String concentratorCode = downLinkForm.getConcentrator().getCode();
        VerificationUtils.string("concentratorCode", concentratorCode);
        downLinkForm.setTaskType(ConcentratorConstants.TASK_TYPE_READ_FILE);
        try {
            FutureResult result = iDownLinkFactory.readFiles(downLinkForm);
            return resp(ResultCode.Success, ResultCode.SUCCESS, result);
        } catch (FrameworkRuntimeException e) {
            LOGGER.error(LogMsg.to("code", e.getCode(), "msg", e.getMsg()));
            return resp(e.getCode(), e.getMsg(), null);
        }
    }

    /**
     * 全部抄表
     */
    @PostMapping(value = "/allMeterRead", produces = GlobalContext.PRODUCES)
    public String allMeterRead(@RequestBody DownLinkForm downLinkForm) {
        LOGGER.info(LogMsg.to("downLinkForm", downLinkForm));
        String concentratorCode = downLinkForm.getConcentrator().getCode();
        VerificationUtils.string("concentratorCode", concentratorCode);
        downLinkForm.setTaskType(ConcentratorConstants.TASK_TYPE_ALL_METER_READ);
        try {
            FutureResult result = iDownLinkFactory.allMeterRead(downLinkForm);
            return resp(ResultCode.Success, ResultCode.SUCCESS, result);
        } catch (FrameworkRuntimeException e) {
            LOGGER.error(LogMsg.to("code", e.getCode(), "msg", e.getMsg()));
            return resp(e.getCode(), e.getMsg(), null);
        }
    }

    /**
     * 读取-是否允许数据上报
     */
    @PostMapping(value = "/readIsAllowUplinkData", produces = GlobalContext.PRODUCES)
    public String readIsAllowUplinkData(@RequestBody DownLinkForm downLinkForm) throws FrameworkRuntimeException {
        LOGGER.info(LogMsg.to("downLinkForm", downLinkForm));
        String concentratorCode = downLinkForm.getConcentrator().getCode();
        VerificationUtils.string("concentratorCode", concentratorCode);
        downLinkForm.setTaskType(ConcentratorConstants.TASK_TYPE_READ_IS_ALLOW_UPLINK_DATA);
        try {
            FutureResult result = iDownLinkFactory.readIsAllowUplinkData(downLinkForm);
            return resp(ResultCode.Success, ResultCode.SUCCESS, result);
        } catch (FrameworkRuntimeException e) {
            LOGGER.error(LogMsg.to("code", e.getCode(), "msg", e.getMsg()));
            return resp(e.getCode(), e.getMsg(), null);
        }
    }

    /**
     * 设置-是否允许数据上报
     */
    @PostMapping(value = "/setIsAllowUplinkData", produces = GlobalContext.PRODUCES)
    public String setIsAllowUplinkData(@RequestBody DownLinkForm downLinkForm) throws FrameworkRuntimeException {
        LOGGER.info(LogMsg.to("downLinkForm", downLinkForm));
        String concentratorCode = downLinkForm.getConcentrator().getCode();
        String setIsAllowUplinkData = downLinkForm.getSetIsAllowUplinkData();
        VerificationUtils.string("concentratorCode", concentratorCode);
        VerificationUtils.string("setIsAllowUplinkData", setIsAllowUplinkData);
        downLinkForm.setTaskType(ConcentratorConstants.TASK_TYPE_SET_IS_ALLOW_UPLINK_DATA);
        try {
            FutureResult result = iDownLinkFactory.setIsAllowUplinkData(downLinkForm);
            return resp(ResultCode.Success, ResultCode.SUCCESS, result);
        } catch (FrameworkRuntimeException e) {
            LOGGER.error(LogMsg.to("code", e.getCode(), "msg", e.getMsg()));
            return resp(e.getCode(), e.getMsg(), null);
        }
    }

    /**
     * 读取-上报时间
     */
    @PostMapping(value = "/readUplinkDate", produces = GlobalContext.PRODUCES)
    public String readUplinkDate(@RequestBody DownLinkForm downLinkForm) throws FrameworkRuntimeException {
        LOGGER.info(LogMsg.to("downLinkForm", downLinkForm));
        String concentratorCode = downLinkForm.getConcentrator().getCode();
        VerificationUtils.string("concentratorCode", concentratorCode);
        downLinkForm.setTaskType(ConcentratorConstants.TASK_TYPE_READ_UPLINK_DATE);
        try {
            FutureResult result = iDownLinkFactory.readUplinkDate(downLinkForm);
            return resp(ResultCode.Success, ResultCode.SUCCESS, result);
        } catch (FrameworkRuntimeException e) {
            LOGGER.error(LogMsg.to("code", e.getCode(), "msg", e.getMsg()));
            return resp(e.getCode(), e.getMsg(), null);
        }
    }

    /**
     * 设置-上报时间
     */
    @PostMapping(value = "/setUplinkDate", produces = GlobalContext.PRODUCES)
    public String setUplinkDate(@RequestBody DownLinkForm downLinkForm) throws FrameworkRuntimeException {
        LOGGER.info(LogMsg.to("downLinkForm", downLinkForm));
        String concentratorCode = downLinkForm.getConcentrator().getCode();
        Date setUplinkDate = downLinkForm.getSetUplinkDate();
        String setUplinkDateType = downLinkForm.getSetUplinkDateType();
        VerificationUtils.string("concentratorCode", concentratorCode);
        VerificationUtils.date("setUplinkDate", setUplinkDate);
        VerificationUtils.string("setUplinkDateType", setUplinkDateType);
        downLinkForm.setTaskType(ConcentratorConstants.TASK_TYPE_SET_UPLINK_DATE);
        try {
            FutureResult result = iDownLinkFactory.setUplinkDate(downLinkForm);
            return resp(ResultCode.Success, ResultCode.SUCCESS, result);
        } catch (FrameworkRuntimeException e) {
            LOGGER.error(LogMsg.to("code", e.getCode(), "msg", e.getMsg()));
            return resp(e.getCode(), e.getMsg(), null);
        }
    }

    /**
     * 读取-网络gprs设置
     */
    @PostMapping(value = "/readGprs", produces = GlobalContext.PRODUCES)
    public String readGprs(@RequestBody DownLinkForm downLinkForm) throws FrameworkRuntimeException {
        LOGGER.info(LogMsg.to("downLinkForm", downLinkForm));
        String concentratorCode = downLinkForm.getConcentrator().getCode();
        VerificationUtils.string("concentratorCode", concentratorCode);
        downLinkForm.setTaskType(ConcentratorConstants.TASK_TYPE_READ_GPRS);
        try {
            FutureResult result = iDownLinkFactory.readGprs(downLinkForm);
            return resp(ResultCode.Success, ResultCode.SUCCESS, result);
        } catch (FrameworkRuntimeException e) {
            LOGGER.error(LogMsg.to("code", e.getCode(), "msg", e.getMsg()));
            return resp(e.getCode(), e.getMsg(), null);
        }
    }

    /**
     * 设置-网络gprs设置
     */
    @PostMapping(value = "/setGprs", produces = GlobalContext.PRODUCES)
    public String setGprs(@RequestBody DownLinkForm downLinkForm) throws FrameworkRuntimeException {
        LOGGER.info(LogMsg.to("downLinkForm", downLinkForm));
        String concentratorCode = downLinkForm.getConcentrator().getCode();
        String setGprsIp = downLinkForm.getSetGprsIp();
        String setGprsPort = downLinkForm.getSetGprsPort();
        VerificationUtils.string("concentratorCode", concentratorCode);
        VerificationUtils.string("setGprsIp", setGprsIp);
        VerificationUtils.string("setGprsPort", setGprsPort);
        downLinkForm.setTaskType(ConcentratorConstants.TASK_TYPE_SET_GPRS);
        try {
            FutureResult result = iDownLinkFactory.setGprs(downLinkForm);
            return resp(ResultCode.Success, ResultCode.SUCCESS, result);
        } catch (FrameworkRuntimeException e) {
            LOGGER.error(LogMsg.to("code", e.getCode(), "msg", e.getMsg()));
            return resp(e.getCode(), e.getMsg(), null);
        }
    }

    /**
     * 读取-集中器时间
     */
    @PostMapping(value = "/readDate", produces = GlobalContext.PRODUCES)
    public String readDate(@RequestBody DownLinkForm downLinkForm) throws FrameworkRuntimeException {
        LOGGER.info(LogMsg.to("downLinkForm", downLinkForm));
        String concentratorCode = downLinkForm.getConcentrator().getCode();
        VerificationUtils.string("concentratorCode", concentratorCode);
        downLinkForm.setTaskType(ConcentratorConstants.TASK_TYPE_READ_DATE);
        try {
            FutureResult result = iDownLinkFactory.readDate(downLinkForm);
            return resp(ResultCode.Success, ResultCode.SUCCESS, result);
        } catch (FrameworkRuntimeException e) {
            LOGGER.error(LogMsg.to("code", e.getCode(), "msg", e.getMsg()));
            return resp(e.getCode(), e.getMsg(), null);
        }
    }

    /**
     * 设置-集中器时间
     */
    @PostMapping(value = "/setDate", produces = GlobalContext.PRODUCES)
    public String setDate(@RequestBody DownLinkForm downLinkForm) throws FrameworkRuntimeException {
        LOGGER.info(LogMsg.to("downLinkForm", downLinkForm));
        String concentratorCode = downLinkForm.getConcentrator().getCode();
        Date setDate = downLinkForm.getSetDate();
        VerificationUtils.string("concentratorCode", concentratorCode);
        VerificationUtils.date("setDate", setDate);
        downLinkForm.setTaskType(ConcentratorConstants.TASK_TYPE_SET_DATE);
        try {
            FutureResult result = iDownLinkFactory.setDate(downLinkForm);
            return resp(ResultCode.Success, ResultCode.SUCCESS, result);
        } catch (FrameworkRuntimeException e) {
            LOGGER.error(LogMsg.to("code", e.getCode(), "msg", e.getMsg()));
            return resp(e.getCode(), e.getMsg(), null);
        }
    }

    /**
     * 单表抄表
     */
    @PostMapping(value = "/meterRead", produces = GlobalContext.PRODUCES)
    public String meterRead(@RequestBody DownLinkForm downLinkForm) {
        LOGGER.info(LogMsg.to("downLinkForm", downLinkForm));
        String concentratorCode = downLinkForm.getConcentrator().getCode();
        String devno = downLinkForm.getDevno();
        VerificationUtils.string("concentratorCode", concentratorCode);
        VerificationUtils.string("devno", devno);
        downLinkForm.setTaskType(ConcentratorConstants.TASK_TYPE_METER_READ);
        try {
            FutureResult result = iDownLinkFactory.meterRead(downLinkForm);
            return resp(ResultCode.Success, ResultCode.SUCCESS, result);
        } catch (FrameworkRuntimeException e) {
            LOGGER.error(LogMsg.to("code", e.getCode(), "msg", e.getMsg()));
            return resp(e.getCode(), e.getMsg(), null);
        }
    }

    /**
     * 开关阀操作
     */
    @PostMapping(value = "/valveOper", produces = GlobalContext.PRODUCES)
    public String valveOper(@RequestBody DownLinkForm downLinkForm) throws FrameworkRuntimeException {
        LOGGER.info(LogMsg.to("downLinkForm", downLinkForm));
        String concentratorCode = downLinkForm.getConcentrator().getCode();
        String devno = downLinkForm.getDevno();
        String valveOper = downLinkForm.getValveOper();
        VerificationUtils.string("concentratorCode", concentratorCode);
        VerificationUtils.string("devno", devno);
        VerificationUtils.string("valveOper", valveOper);
        if (ConcentratorConstants.TASK_TYPE_DEVICE_OPEN.equals(valveOper)) {
            downLinkForm.setTaskType(ConcentratorConstants.TASK_TYPE_DEVICE_OPEN);
            downLinkForm.setValveOper(ConcentratorConstants.VALVE_OPER_OPEN);
        } else if (ConcentratorConstants.TASK_TYPE_DEVICE_CLOSE.equals(valveOper)) {
            downLinkForm.setTaskType(ConcentratorConstants.TASK_TYPE_DEVICE_CLOSE);
            downLinkForm.setValveOper(ConcentratorConstants.VALVE_OPER_CLOSE);
        } else {
            return resp(ResultCode.Fail, "任务类型不存在", null);
        }
        try {
            FutureResult result = iDownLinkFactory.valveOper(downLinkForm);
            return resp(ResultCode.Success, ResultCode.SUCCESS, result);
        } catch (FrameworkRuntimeException e) {
            LOGGER.error(LogMsg.to("code", e.getCode(), "msg", e.getMsg()));
            return resp(e.getCode(), e.getMsg(), null);
        }
    }

}
