package com.dotop.smartwater.project.third.concentrator.thread.pool;

import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.project.third.concentrator.api.ICollectorFactory;
import com.dotop.smartwater.project.third.concentrator.core.utils.SpringContextUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.DateTime;

import java.util.Random;
import java.util.concurrent.Callable;


public class MyCallable implements Callable<String> {

    private final static Logger LOGGER = LogManager.getLogger(MyCallable.class);

    private ICollectorFactory iCollectorFactory = (ICollectorFactory) SpringContextUtils.getBean("collectorFactoryImpl");

    private String name;

    public MyCallable(String name) {
        this.name = name;
    }

    @Override
    public String call() {
        try {
            LOGGER.info(LogMsg.to("msg0-" + name, iCollectorFactory));
            Random r1 = new Random();
            int sleep = (r1.nextInt(10) + 3) * 1000;
            LOGGER.info(LogMsg.to("msg1-" + name + "-" + sleep, new DateTime().toString("yyyy-MM-dd HH:mm:ss")));
            Thread.sleep(sleep);
            LOGGER.info(LogMsg.to("msg2-" + name + "-" + sleep, new DateTime().toString("yyyy-MM-dd HH:mm:ss")));
            return name;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "error";
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "MyTask [name=" + name + "]";
    }
}
