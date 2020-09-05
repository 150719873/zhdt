package com.dotop.smartwater.project.third.server.meterread.client2.utils;

import org.springframework.context.ApplicationContext;

public class SpringContextUtils {

    private static ApplicationContext applicationContext;

    public static void setApplicationContext(ApplicationContext context) {
        applicationContext = context;
    }

    public static Object getBean(String beanId) {
        return applicationContext.getBean(beanId);
    }
}
