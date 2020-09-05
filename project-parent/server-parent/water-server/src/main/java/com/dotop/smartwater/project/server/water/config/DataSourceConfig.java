package com.dotop.smartwater.project.server.water.config;

import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;

//数据库配置
@Configuration
public class DataSourceConfig {

	@Value("${druid.datasource.name}")
	private String name;
	@Value("${druid.datasource.url}")
	private String url;
	@Value("${druid.datasource.username}")
	private String username;
	@Value("${druid.datasource.password}")
	private String password;
	@Value("${druid.datasource.type}")
	private String type;
	@Value("${druid.datasource.driver-class-name}")
	private String driverClassName;
	@Value("${druid.datasource.filters}")
	private String filters;
	@Value("${druid.datasource.maxActive}")
	private int maxActive;
	@Value("${druid.datasource.initialSize}")
	private int initialSize;
	@Value("${druid.datasource.maxWait}")
	private long maxWait;
	@Value("${druid.datasource.minIdle}")
	private int minIdle;
	@Value("${druid.datasource.timeBetweenEvictionRunsMillis}")
	private long timeBetweenEvictionRunsMillis;
	@Value("${druid.datasource.minEvictableIdleTimeMillis}")
	private long minEvictableIdleTimeMillis;
	@Value("${druid.datasource.validationQuery}")
	private String validationQuery;
	@Value("${druid.datasource.testWhileIdle}")
	private boolean testWhileIdle;
	@Value("${druid.datasource.testOnBorrow}")
	private boolean testOnBorrow;
	@Value("${druid.datasource.testOnReturn}")
	private boolean testOnReturn;
	@Value("${druid.datasource.poolPreparedStatements}")
	private boolean poolPreparedStatements;
	@Value("${druid.datasource.maxOpenPreparedStatements}")
	private int maxOpenPreparedStatements;

	@Bean
	public DataSource getDruidDataSource() throws SQLException {

		DruidDataSource dataSource = new DruidDataSource();
		dataSource.setDriverClassName(driverClassName);
		dataSource.setUrl(url);
		dataSource.setUsername(username);
		dataSource.setPassword(password);

		dataSource.setName(name);
		dataSource.setDbType(type);
		// dataSource.setFilters("stat");
		dataSource.setFilters(null);
		dataSource.setMaxActive(maxActive);
		dataSource.setInitialSize(initialSize);
		dataSource.setMaxWait(maxWait);
		dataSource.setMinIdle(minIdle);
		dataSource.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
		dataSource.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
		dataSource.setValidationQuery(validationQuery);
		dataSource.setTestWhileIdle(testWhileIdle);
		dataSource.setTestOnBorrow(testOnBorrow);
		dataSource.setTestOnReturn(testOnReturn);
		dataSource.setPoolPreparedStatements(poolPreparedStatements);
		dataSource.setMaxOpenPreparedStatements(maxOpenPreparedStatements);

		return dataSource;
	}

	@Bean
	public DataSourceTransactionManager getPlatformTransactionManager() throws SQLException {
		DataSource dataSource = getDruidDataSource();
		return new DataSourceTransactionManager(dataSource);
	}

	// @Bean
	public ServletRegistrationBean<StatViewServlet> druidStatViewServle() {
		ServletRegistrationBean<StatViewServlet> servletRegistrationBean = new ServletRegistrationBean<>(
				new StatViewServlet(), "/druid/*");
		// 白名单
		servletRegistrationBean.addInitParameter("allow", "127.0.0.1");
		// IP黑名单
		servletRegistrationBean.addInitParameter("deny", "192.168.1.1");
		// 登录查看信息的账号密码.
		servletRegistrationBean.addInitParameter("loginUsername", "admin");
		servletRegistrationBean.addInitParameter("loginPassword", "123456");
		// 是否能够重置数据.
		servletRegistrationBean.addInitParameter("resetEnable", "false");
		return servletRegistrationBean;
	}

	// @Bean
	public FilterRegistrationBean<WebStatFilter> druidStatFilter() {

		FilterRegistrationBean<WebStatFilter> filterRegistrationBean = new FilterRegistrationBean<>(
				new WebStatFilter());
		// 过滤规则
		filterRegistrationBean.addUrlPatterns("/*");
		// 忽略的信息
		filterRegistrationBean.addInitParameter("exclusions", "/druid/*");
		return filterRegistrationBean;
	}

}
