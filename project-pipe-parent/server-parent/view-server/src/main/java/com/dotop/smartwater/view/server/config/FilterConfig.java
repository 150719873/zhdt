package com.dotop.smartwater.view.server.config;

import com.dotop.smartwater.view.server.filter.AuthFilter;
import com.dotop.smartwater.view.server.filter.OutFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

    @Autowired
    private AuthFilter authFilter;

    @Autowired
    private OutFilter outFilter;

    @Bean
    public FilterRegistrationBean<AuthFilter> authFilterRegistration() {
        FilterRegistrationBean<AuthFilter> registration = new FilterRegistrationBean<AuthFilter>();
        registration.setFilter(authFilter);
        // pipe-server 后面
        registration.addUrlPatterns("/*");
        registration.setName("authFilter");
        registration.setOrder(1);
        return registration;
    }

    @Bean
    public FilterRegistrationBean<OutFilter> outFilterRegistration() {
        FilterRegistrationBean<OutFilter> registration = new FilterRegistrationBean<OutFilter>();
        registration.setFilter(outFilter);
        // pipe-server 后面
        registration.addUrlPatterns("/*");
        registration.setName("outFilter");
        registration.setOrder(2);
        return registration;
    }
}
