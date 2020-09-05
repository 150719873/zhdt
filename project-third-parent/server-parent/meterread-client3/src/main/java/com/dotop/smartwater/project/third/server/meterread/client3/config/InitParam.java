package com.dotop.smartwater.project.third.server.meterread.client3.config;

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
    @Value("${param.config.code}")
    private String code;
    @Value("${param.config.userName}")
    private String userName;
    @Value("${param.config.password}")
    private String password;
    @Value("${param.config.findPageSize}")
    private Integer findPageSize;
    @Value("${param.config.times}")
    private Integer times;
    @Value("${param.config.expire}")
    private Integer expire;
    @Value("${param.config.serverHost}")
    private String serverHost;
    @Value("${param.config.timeOut}")
    private Integer timeOut;

    @PostConstruct
    public void init() {
        Config.VERSION = version;
        Config.FIND_PAGESIZE = findPageSize;
        Config.USER_NAME = userName;
        Config.PASSWORD = password;
        Config.TICKET = null;
        Config.CODE = code;
        Config.TIMES = times;
        Config.EXPIRE = expire;
        Config.SERVER_HOST = serverHost;
        Config.TIME_OUT = timeOut;
        LOGGER.info(LogMsg.to("VERSION", version));
        LOGGER.info(LogMsg.to("TIMES", times));
        LOGGER.info(LogMsg.to("EXPIRE", expire));
        LOGGER.info(LogMsg.to("SERVER_HOST", serverHost));
        LOGGER.info(LogMsg.to("TIME_OUT", timeOut));
    }

}
