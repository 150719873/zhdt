package com.dotop.smartwater.project.third.server.meter.rest.service.third;


import com.dotop.smartwater.dependence.core.common.BaseController;
import com.dotop.smartwater.dependence.core.common.BaseForm;
import com.dotop.smartwater.dependence.core.global.GlobalContext;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.project.module.core.water.constants.ResultCode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 *
 */
@RestController
@RequestMapping("/third")
public class ThirdController implements BaseController<BaseForm> {

    private static final Logger LOGGER = LogManager.getLogger(ThirdController.class);

    /**
     * 下发命令
     */
    @PostMapping(value = "/downlink", produces = GlobalContext.PRODUCES, consumes = GlobalContext.PRODUCES)
    public String downlink(@RequestBody Map<String, String> params) {
        LOGGER.info(LogMsg.to("params", params));
        Map<String, String> data = new HashMap<>(2);
        return resp(ResultCode.Success, ResultCode.SUCCESS, data);
    }


    /**
     * 下发查询结果
     */
    @PostMapping(value = "/downlink/result", produces = GlobalContext.PRODUCES)
    public String downlinkResult(@RequestBody Map<String, String> params) {
        LOGGER.info(LogMsg.to("params", params));
        Map<String, String> data = new HashMap<>(3);
        return resp(ResultCode.Success, ResultCode.SUCCESS, data);
    }

}
