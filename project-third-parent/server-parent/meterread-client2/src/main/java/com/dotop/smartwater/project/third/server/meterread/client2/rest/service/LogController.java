package com.dotop.smartwater.project.third.server.meterread.client2.rest.service;

import com.dotop.smartwater.dependence.core.common.BaseController;
import com.dotop.smartwater.dependence.core.common.BaseForm;
import com.dotop.smartwater.dependence.core.global.GlobalContext;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.project.module.core.water.constants.ResultCode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * 日志读取
 *
 *
 */
@Deprecated
//@RestController()
//@RequestMapping("/log")
public class LogController implements BaseController<BaseForm> {

    private final static Logger LOGGER = LogManager.getLogger(LogController.class);

    @Value("${param.config.logLine:100}")
    private int LOG_LINE;

    @PostMapping(value = "/show", produces = GlobalContext.PRODUCES)
    public String show() {
        try {
            FileInputStream inputStream = new FileInputStream("logs/sysout.log");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            Stream<String> lines = bufferedReader.lines();
            List<String> logs = new ArrayList<>();
            String str = null;
            while ((str = bufferedReader.readLine()) != null) {
                logs.add(str);
            }
            inputStream.close();
            bufferedReader.close();
            // 获取最后100条
            if (logs.size() > LOG_LINE) {
                logs.subList(0, logs.size() - LOG_LINE).clear();
            }
            return resp(ResultCode.Success, ResultCode.SUCCESS, logs);
        } catch (IOException e) {
            LOGGER.error(LogMsg.to(e));
            return resp(ResultCode.Fail, "日志读取失败，请稍后再试", null);
        }
    }

}
