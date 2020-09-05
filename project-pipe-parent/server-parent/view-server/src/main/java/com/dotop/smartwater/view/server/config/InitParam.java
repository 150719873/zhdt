package com.dotop.smartwater.view.server.config;

import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.view.server.core.config.Config;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 */
@Configuration
public class InitParam {

    private static final Logger LOGGER = LogManager.getLogger(InitParam.class);

    @Value("${param.config.enterpriseids}")
    private String enterpriseids;

    @PostConstruct
    public void init() {
        Config.ENTERPRISE_IDS = enterpriseids;
        LOGGER.info(LogMsg.to("ENTERPRISE_IDS", enterpriseids));
    }

}
