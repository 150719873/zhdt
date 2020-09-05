package com.dotop.smartwater.project.third.concentrator.thread.pool;

import com.dotop.smartwater.dependence.core.log.LogMsg;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.DateTime;

import java.util.Random;

public class MyRunnable implements Runnable {

    private final static Logger LOGGER = LogManager.getLogger(MyRunnable.class);

    private String name;

    public MyRunnable(String name) {
        this.name = name;
    }

    @Override
    public void run() {
        try {
            Random r1 = new Random();
            int sleep = (r1.nextInt(10) + 3) * 1000;
            LOGGER.info(LogMsg.to("msg1-" + name + "-" + sleep, new DateTime().toString("yyyy-MM-dd HH:mm:ss")));
            Thread.sleep(sleep);
            LOGGER.info(LogMsg.to("msg2-" + name + "-" + sleep, new DateTime().toString("yyyy-MM-dd HH:mm:ss")));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "MyTask [name=" + name + "]";
    }
}
