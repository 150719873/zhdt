package com.dotop.pipe.server.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import io.shardingsphere.api.config.ShardingRuleConfiguration;
import io.shardingsphere.api.config.TableRuleConfiguration;
import io.shardingsphere.api.config.strategy.InlineShardingStrategyConfiguration;
import io.shardingsphere.shardingjdbc.api.ShardingDataSourceFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

// https://blog.gzj.me/2017/11/01/%E5%88%86%E5%B8%83%E5%BC%8F%E6%95%B0%E6%8D%AE%E5%BA%93%E4%B8%AD%E9%97%B4%E4%BB%B6Sharding-JDBC/
//https://www.cnblogs.com/mr-yang-localhost/p/8313360.html#_label1_2
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

    @Bean("druidDataSource")
    public DataSource getDruidDataSource() throws SQLException {

        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setDriverClassName(driverClassName);
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);

        dataSource.setName(name);
        dataSource.setDbType(type);
        dataSource.setFilters("stat");
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
    public DataSourceTransactionManager getPlatformTransactionManager(@Qualifier("shardingDataSource") DataSource shardingDataSource) throws SQLException {
        return new DataSourceTransactionManager(shardingDataSource);
    }

    @Bean
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

    @Bean
    public FilterRegistrationBean<WebStatFilter> druidStatFilter() {

        FilterRegistrationBean<WebStatFilter> filterRegistrationBean = new FilterRegistrationBean<>(
                new WebStatFilter());
        // 过滤规则
        filterRegistrationBean.addUrlPatterns("/*");
        // 忽略的信息
        filterRegistrationBean.addInitParameter("exclusions", "/druid/*");
        return filterRegistrationBean;
    }

    @Bean("shardingDataSource")
    @Primary
    public DataSource getDataSource(@Qualifier("druidDataSource") DataSource druidDataSource) throws SQLException {
        return buildDataSource(druidDataSource);
    }

    private DataSource buildDataSource(DataSource druidDataSource) throws SQLException {


        // 设置分库映射
        Map<String, DataSource> dataSourceMap = new HashMap<>();
        // 添加两个数据库ds_0,ds_1到map里
        dataSourceMap.put("db0", druidDataSource);
        // dataSourceMap.put("ds_0", createDataSource("pipe-ww"));
        // 设置默认db为ds_0，也就是为那些没有配置分库分表策略的指定的默认库
        // 如果只有一个库，也就是不需要分库的话，map里只放一个映射就行了，只有一个库时不需要指定默认库，但2个及以上时必须指定默认库，否则那些没有配置策略的表将无法操作数据

//        TableRuleConfiguration dogTableRuleConfiguration = new TableRuleConfiguration();
//        dogTableRuleConfiguration.setLogicTable("pls_device_property_log"); // 逻辑表
//        dogTableRuleConfiguration.setActualDataNodes(
//                "db0.pls_device_property_log_${201701..201712},db0.pls_device_property_log_${201801..201812},db0.pls_device_property_log_${201901..201912}"); // 真实的数据节点
//        dogTableRuleConfiguration.setTableShardingStrategyConfig(
//                new InlineShardingStrategyConfiguration("ctime", "pls_device_property_log_${ctime}"));


        TableRuleConfiguration plsDeviceDataLog = new TableRuleConfiguration();
        plsDeviceDataLog.setLogicTable("pls_device_data_log"); // 逻辑表
        plsDeviceDataLog.setActualDataNodes("db0.pls_device_data_log_${201801..201812},db0.pls_device_data_log_${201901..201912},db0.pls_device_data_log_${202001..202012},db0.pls_device_data_log_${202101..202112}"); // 真实的数据节点
        plsDeviceDataLog.setTableShardingStrategyConfig(
                new InlineShardingStrategyConfiguration("ctime", "pls_device_data_log_${ctime}"));


        ShardingRuleConfiguration shardingRuleConfiguration = new ShardingRuleConfiguration();
//        shardingRuleConfiguration.getTableRuleConfigs().add(dogTableRuleConfiguration);
        shardingRuleConfiguration.getTableRuleConfigs().add(plsDeviceDataLog);

        return ShardingDataSourceFactory.createDataSource(dataSourceMap, shardingRuleConfiguration, new HashMap<>(),
                new Properties());
    }

}
