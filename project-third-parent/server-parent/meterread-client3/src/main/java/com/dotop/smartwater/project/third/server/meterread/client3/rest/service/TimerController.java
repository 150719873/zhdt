package com.dotop.smartwater.project.third.server.meterread.client3.rest.service;

import com.dotop.smartwater.project.third.server.meterread.client3.config.TimerConfig;
import com.dotop.smartwater.dependence.core.common.BaseController;
import com.dotop.smartwater.dependence.core.common.BaseForm;
import com.dotop.smartwater.dependence.core.global.GlobalContext;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.project.module.core.water.constants.ResultCode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 定时器执行操作
 *
 *
 */
@RestController()
@RequestMapping("/timer")
public class TimerController implements BaseController<BaseForm> {

    private static final Logger LOGGER_SYSOUT = LogManager.getLogger("SYSOUT");

    private final static Logger LOGGER = LogManager.getLogger(TimerController.class);

    @Autowired
    private TimerConfig timerConfig;

    /**
     * 启动任务
     **/
    @RequestMapping(value = "/startCron", produces = GlobalContext.PRODUCES)
    public String startCron() {
        LOGGER.info(LogMsg.to("msg", "startCron启动任务"));
        LOGGER_SYSOUT.info("startCron启动任务");
        timerConfig.stopCron();
        timerConfig.startCron();
        return resp(ResultCode.Success, ResultCode.SUCCESS, null);
    }

    /**
     * 停止任务
     **/
    @RequestMapping(value = "/stopCron", produces = GlobalContext.PRODUCES)
    public String stopCron() {
        LOGGER.info(LogMsg.to("msg", "stopCron停止任务"));
        LOGGER_SYSOUT.info("stopCron停止任务");
        timerConfig.stopCron();
        return resp(ResultCode.Success, ResultCode.SUCCESS, null);
    }

    /**
     * 变更任务间隔，再次启动
     **/
    @RequestMapping(value = "/changeCron", produces = GlobalContext.PRODUCES)
    public String changeCron() {
        LOGGER.info(LogMsg.to("msg", "changeCron变更任务间隔，再次启动"));
        LOGGER_SYSOUT.info("changeCron变更任务间隔，再次启动");
        timerConfig.stopCron();
        timerConfig.startCron();
        return resp(ResultCode.Success, ResultCode.SUCCESS, null);
    }


}
