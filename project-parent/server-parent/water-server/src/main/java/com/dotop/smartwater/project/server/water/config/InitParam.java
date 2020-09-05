package com.dotop.smartwater.project.server.water.config;

import javax.annotation.PostConstruct;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import com.dotop.smartwater.project.module.core.auth.config.WaterClientConfig;
import com.dotop.smartwater.project.module.core.water.config.Config;
import com.dotop.smartwater.project.module.core.water.utils.sequence.Sequence;

/**

 *
 */
@Configuration
public class InitParam {

	private static final Logger LOGGER = LogManager.getLogger(InitParam.class);

	@Value("${param.config.version:2.5}")
	private String version;

	@Value("${param.config.userinvalidtime}")
	private String userinvalidtime;

	@Value("${param.config.serverhost}")
	private String serverhost;

	@Value("${param.config.weixindebug}")
	private Boolean weixindebug;

	@Value("${param.config.authserverurl}")
	private String authserverurl;

	@Value("${param.config.paycallbackurl}")
	private String paycallbackurl;

	@PostConstruct
	public void init() {
		Config.VERSION = version;

		Config.ServerHost = serverhost;
		Config.UserInvalidTime = userinvalidtime;
		Config.weixinDebug = weixindebug;

		WaterClientConfig.WaterCasUrl = authserverurl;
		Config.PayCallBackUrl = paycallbackurl;

		Config.Generator = new Sequence(0L, 0L);

		LOGGER.info("WaterCasUrl: " + WaterClientConfig.WaterCasUrl);
		LOGGER.info("IOT Host: " + Config.ServerHost);
		LOGGER.info("PayCallBackUrl: " + Config.PayCallBackUrl);
	}

}
