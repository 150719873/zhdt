package com.dotop.smartwater.project.third.concentrator.core.utils;

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
