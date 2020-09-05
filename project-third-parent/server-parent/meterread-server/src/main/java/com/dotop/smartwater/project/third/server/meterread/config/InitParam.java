package com.dotop.smartwater.project.third.server.meterread.config;

import com.dotop.smartwater.project.third.server.meterread.core.third.config.Config;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 *
 */
@Configuration
public class InitParam {

    private static final Logger LOGGER = LogManager.getLogger(InitParam.class);

    @Value("${param.config.version:2.5}")
    private String version;
    @Value("${param.config.findPageSize}")
    private Integer findPageSize;
    @Value("${param.config.times}")
    private Integer times;
    @Value("${param.config.expire}")
    private Integer expire;

    @PostConstruct
    public void init() {
        Config.VERSION = version;
        Config.FIND_PAGESIZE = findPageSize;
        Config.TIMES = times;
        Config.EXPIRE = expire;
        LOGGER.info(LogMsg.to("VERSION", version));
        LOGGER.info(LogMsg.to("TIMES", times));
        LOGGER.info(LogMsg.to("EXPIRE", expire));
    }

}
