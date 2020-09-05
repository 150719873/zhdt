package com.dotop.smartwater.project.server.schedule.schedule;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.scheduling.annotation.Scheduled;

//@Component
public class TestScheduled {

	private static final Logger LOGGER = LogManager.getLogger(TestScheduled.class);

	@Scheduled(initialDelay = 10000, fixedRate = 3600000)
	public void init() {
		LOGGER.info("定时任务----");
		//System.out.println("定时任务开始执行");
	}
}
