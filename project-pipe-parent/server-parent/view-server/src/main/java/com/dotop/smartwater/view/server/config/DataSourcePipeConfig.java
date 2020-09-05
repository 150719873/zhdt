package com.dotop.smartwater.view.server.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//数据库配置
@Configuration
@MapperScan(basePackages = {"com.dotop.smartwater.view.server.dao.pipe", "com.dotop.pipe.api.dao",
        "com.dotop.pipe.auth.api.dao", "com.dotop.pipe.data.report.api.dao","com.dotop.pipe.data.receiver.api.dao"},
        sqlSessionTemplateRef = "pipeSqlSessionTemplate")
public class DataSourcePipeConfig {

    @Value("${druid.datasource-pipe.name}")
    private String name;
    @Value("${druid.datasource-pipe.url}")
    private String url;
    @Value("${druid.datasource-pipe.username}")
    private String username;
    @Value("${druid.datasource-pipe.password}")
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

    @Bean(name = "pipeData")
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

    @Bean(name = "pipeSqlSessionFactory")
    public SqlSessionFactory pipeSqlSessionFactory(@Qualifier("pipeData") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        List<Resource> resources = new ArrayList<>();
        resources.addAll(Arrays.asList(resolver.getResources("classpath*:com/dotop/smartwater/view/server/dao/pipe/**/*.xml")));
        resources.addAll(Arrays.asList(resolver.getResources("classpath*:com/dotop/pipe/dao/**/*.xml")));
        resources.addAll(Arrays.asList(resolver.getResources("classpath*:com/dotop/pipe/auth/dao/**/*.xml")));
        resources.addAll(Arrays.asList(resolver.getResources("classpath*:com/dotop/pipe/data/report/dao/*.xml")));
        resources.addAll(Arrays.asList(resolver.getResources("classpath*:com/dotop/pipe/data/receiver/dao/*.xml")));
        bean.setMapperLocations(resources.toArray(new Resource[resources.size()]));
        bean.setConfigLocation(resolver.getResource("classpath:mybatis-config.xml"));
        return bean.getObject();
    }

    @Bean(name = "pipeTransactionManager")
    public DataSourceTransactionManager pipeTransactionManager(@Qualifier("pipeData") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean(name = "pipeSqlSessionTemplate")
    public SqlSessionTemplate pipeSqlSessionTemplate(@Qualifier("pipeSqlSessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception {
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}
