package com.dotop.smartwater.project.server.schedule.schedule;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.project.module.api.revenue.IEverydayWaterRecordFactory;

/**
 * 每天用水量计算
 * 

 */
// @Component
public class EveryDayWaterSchedule {

	private static final Logger LOGGER = LogManager.getLogger(EveryDayWaterSchedule.class);

	@Autowired
	private IEverydayWaterRecordFactory iEverydayWaterRecordFactory;

	@Scheduled(initialDelay = 10000, fixedRate = 3600000)
	public void init() {
		// insertEveryDayWater
		Long start = System.currentTimeMillis();
		try {
			// 先插入今天的用水总量,再计算更新今天的用水量
			iEverydayWaterRecordFactory.addEveryDayWaterRecord();
			iEverydayWaterRecordFactory.updateEveryDayWaterRecord();
		} catch (FrameworkRuntimeException e) {
			LOGGER.error("AsyncEveryDayWaterRecord", e);
		} finally {
			LOGGER.info("统计每个小区用水量, 耗时: {} ms", (System.currentTimeMillis() - start));
		}
	}

}
