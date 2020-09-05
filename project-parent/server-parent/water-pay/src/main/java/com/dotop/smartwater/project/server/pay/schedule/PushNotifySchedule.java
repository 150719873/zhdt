package com.dotop.smartwater.project.server.pay.schedule;

import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.project.module.api.pay.IPushFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**

 * @date 2019/7/31.
 */
@Component
public class PushNotifySchedule {

	private static final Logger LOGGER = LogManager.getLogger(PushNotifySchedule.class);

	@Autowired
	private IPushFactory iPushFactory;

	@Scheduled(initialDelay = 20000, fixedRate = 1800000)
	public void pushNotify() {
		Long start = System.currentTimeMillis();
		try {
			iPushFactory.push();
		} catch (FrameworkRuntimeException e) {
			LOGGER.error("pushNotify error", e);
		} finally {
			LOGGER.info("定时任务推送支付状态完成, 耗时: {} ms", (System.currentTimeMillis() - start));
		}
	}
}
