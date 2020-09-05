package com.dotop.smartwater.project.server.wechat;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = { RabbitAutoConfiguration.class,
		DataSourceAutoConfiguration.class }, scanBasePackages = "com.dotop.smartwater")
@MapperScan(basePackages = { "com.dotop.smartwater.project.module.dao" })
public class WaterWechatApplication {

	public static void main(String[] args) {
		SpringApplication.run(WaterWechatApplication.class, args);
	}
	
	 /**
     * 设置匹配*.action后缀请求
     * @param dispatcherServlet
     * @return
     */
//    @Bean
//    public ServletRegistrationBean servletRegistrationBean(DispatcherServlet dispatcherServlet) {
//        ServletRegistrationBean<DispatcherServlet> servletServletRegistrationBean = new ServletRegistrationBean<>(dispatcherServlet);
//        servletServletRegistrationBean.addUrlMappings("*.do");
//        return servletServletRegistrationBean;
//    }


}
