package com.dotop.smartwater.project.server.schedule.schedule;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.project.module.api.revenue.ICouponFactory;

/**
 * 检查优惠券任务
 * 

 * @date 2019年4月22日
 * @description
 */
// @Component
public class CheckoutCouponScheduled {

	private static final Logger Logger = LogManager.getLogger(CheckoutCouponScheduled.class);

	@Autowired
	private ICouponFactory iCouponFactory;

	@Scheduled(initialDelay = 10000, fixedRate = 3600000)
	public void init() {
		Logger.info("执行优惠券检查任务");
		try {
			iCouponFactory.checkTask();
		} catch (FrameworkRuntimeException e) {
			Logger.error("AsyncEveryDayWaterRecord", e);
		} finally {
			Logger.info("完成优惠券检查任务");
		}

	}

}
