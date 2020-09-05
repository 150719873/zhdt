package com.dotop.deyang.config;


import com.dotop.deyang.filter.AccessLogFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean registFilter() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new AccessLogFilter());
        registration.addUrlPatterns("/auth/*");
        registration.setName("AccessLogFilter");
        registration.setOrder(1);
        return registration;
    }

}