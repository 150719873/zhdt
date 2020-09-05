package com.dotop.smartwater.project.third.meterread.webservice.config;

import com.dotop.smartwater.project.third.meterread.webservice.core.third.config.Config;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.project.third.meterread.webservice.core.third.config.ConfigEnterprise;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.ArrayList;

/**
 *
 */
@Configuration
public class InitParam {

    private static final Logger LOGGER = LogManager.getLogger(InitParam.class);

    @Value("${param.config.version:2.5}")
    private String version;
    @Value("${param.config.enterprises}")
    private String enterprises;
    @Value("${param.config.enterpriseName}")
    private String enterpriseName;
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

        Config.ENTERPRISES = new ArrayList<>();
        String[] group = enterprises.split(";");
        for (int i = 0; i < group.length; i++) {
            String[] items = group[i].split(",");
            Config.ENTERPRISES.add(new ConfigEnterprise(items[0], Integer.valueOf(items[1])));
        }
        Config.ENTERPRISE_NAME = enterpriseName;
        Config.TIMES = times;
        Config.EXPIRE = expire;
        LOGGER.info(LogMsg.to("VERSION", version));
        LOGGER.info(LogMsg.to("ENTERPRISE_ID", enterprises));
        LOGGER.info(LogMsg.to("ENTERPRISE_NAME", enterpriseName));
        LOGGER.info(LogMsg.to("TIMES", times));
        LOGGER.info(LogMsg.to("EXPIRE", expire));
    }

}
