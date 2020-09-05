package com.dotop.smartwater.project.server.pay.config;

import com.dotop.smartwater.project.module.core.auth.config.WaterClientConfig;
import com.dotop.smartwater.project.module.core.pay.constants.PayConstants;
import com.dotop.smartwater.project.module.core.water.config.Config;
import com.dotop.smartwater.project.module.core.water.utils.sequence.Sequence;
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

	@Value("${param.config.version:2.6}")
	private String version;

	@Value("${param.config.userinvalidtime}")
	private String userinvalidtime;

	@Value("${param.config.serverhost}")
	private String serverhost;

	@Value("${param.config.weixindebug}")
	private Boolean weixindebug;

	@Value("${param.config.authserverurl}")
	private String authserverurl;

	@Value("${wechat.notify_url}")
	private String wechat_notify_url;

	@PostConstruct
	public void init() {
		Config.VERSION = version;

		Config.ServerHost = serverhost;
		Config.UserInvalidTime = userinvalidtime;
		Config.weixinDebug = weixindebug;

		PayConstants.WeChatNotifyUrl = wechat_notify_url;

		WaterClientConfig.WaterCasUrl = authserverurl;

		Config.Generator = new Sequence(0L, 0L);

		LOGGER.info("支付系统接收微信回调地址：" + PayConstants.WeChatNotifyUrl);

	}

}
