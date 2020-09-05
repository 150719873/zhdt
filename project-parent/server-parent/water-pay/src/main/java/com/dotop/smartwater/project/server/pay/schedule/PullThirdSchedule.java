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
public class PullThirdSchedule {

	private static final Logger LOGGER = LogManager.getLogger(PullThirdSchedule.class);

	@Autowired
	private IPushFactory iPushFactory;

	/**
	 * 5分钟一次
	 * **/
	@Scheduled(initialDelay = 20000, fixedRate = 300000)
	public void getNotify() {
		Long start = System.currentTimeMillis();
		try {
			iPushFactory.getNotify();
		} catch (FrameworkRuntimeException e) {
			LOGGER.error("pushNotify error", e);
		} catch (Exception e) {
			LOGGER.error("pushNotify Exception", e);
		} finally {
			LOGGER.info("定时任务拉取支付状态完成, 耗时: {} ms", (System.currentTimeMillis() - start));
		}
	}
}
