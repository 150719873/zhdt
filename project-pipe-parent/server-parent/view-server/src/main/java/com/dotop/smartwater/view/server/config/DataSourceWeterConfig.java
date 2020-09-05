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
import org.springframework.context.annotation.Primary;
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
@MapperScan(basePackages = {"com.dotop.smartwater.view.server.dao.water"}, sqlSessionTemplateRef = "waterSqlSessionTemplate")
public class DataSourceWeterConfig {

    @Value("${druid.datasource-water.name}")
    private String name;
    @Value("${druid.datasource-water.url}")
    private String url;
    @Value("${druid.datasource-water.username}")
    private String username;
    @Value("${druid.datasource-water.password}")
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

    @Bean(name = "waterData")
    @Primary
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

    @Bean(name = "waterSqlSessionFactory")
    @Primary
    public SqlSessionFactory waterSqlSessionFactory(@Qualifier("waterData") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        List<Resource> resources = new ArrayList<>();
        resources.addAll(Arrays.asList(resolver.getResources("classpath*:com/dotop/smartwater/view/server/dao/water/**/*.xml")));
        bean.setMapperLocations(resources.toArray(new Resource[resources.size()]));
        return bean.getObject();
    }

    @Bean(name = "waterTransactionManager")
    @Primary
    public DataSourceTransactionManager waterTransactionManager(@Qualifier("waterData") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean(name = "waterSqlSessionTemplate")
    @Primary
    public SqlSessionTemplate waterSqlSessionTemplate(@Qualifier("waterSqlSessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception {
        return new SqlSessionTemplate(sqlSessionFactory);
    }


}
