package com.dotop.smartwater.project.auth.config;

import com.dotop.smartwater.project.auth.filter.OutFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

import com.dotop.smartwater.project.auth.filter.AuthFilter;

//@Configuration
public class FilterConfig {

	@Autowired
	private AuthFilter authFilter;

	@Autowired
	private OutFilter outFilter;

	@Bean
	public FilterRegistrationBean<AuthFilter> authFilterRegistration() {
		FilterRegistrationBean<AuthFilter> registration = new FilterRegistrationBean<>();
		registration.setFilter(authFilter);
		// context-path 后面
		registration.addUrlPatterns("/*");
		registration.setName("authFilter");
		registration.setOrder(1);
		return registration;
	}

	@Bean
	public FilterRegistrationBean<OutFilter> outFilterRegistration() {
		FilterRegistrationBean<OutFilter> registration = new FilterRegistrationBean<>();
		registration.setFilter(outFilter);
		// context-path 后面
		registration.addUrlPatterns("/*");
		registration.setName("outFilter");
		registration.setOrder(2);
		return registration;
	}
}
