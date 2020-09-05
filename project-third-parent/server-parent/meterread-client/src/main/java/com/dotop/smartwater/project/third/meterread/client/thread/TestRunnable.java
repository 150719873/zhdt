package com.dotop.smartwater.project.third.meterread.client.thread;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 测试
 *
 *
 */
public class TestRunnable implements Runnable {

    private static final Logger LOGGER = LogManager.getLogger(TestRunnable.class);

    @Override
    public void run() {
        LOGGER.info("心跳定时任务");
    }
}
