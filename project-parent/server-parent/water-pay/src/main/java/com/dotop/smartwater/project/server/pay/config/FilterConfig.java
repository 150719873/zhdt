package com.dotop.smartwater.project.server.pay.config;


import com.dotop.smartwater.project.server.pay.filter.AuthFilter;
import com.dotop.smartwater.project.server.pay.filter.AuthFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**

 */
@Configuration
public class FilterConfig {

	@Autowired
	private AuthFilter authFilter;

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

}
