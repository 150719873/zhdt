package com.dotop.smartwater.project.third.server.meterread.client2.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;
import java.sql.SQLException;

//数据库配置
@Configuration
@MapperScan(basePackages = {"com.dotop.smartwater.project.third.server.meterread.client2.dao.third"}, sqlSessionTemplateRef = "thirdSqlSessionTemplate")
public class DataSourceThirdConfig {

    @Value("${druid.datasource-third.name}")
    private String name;
    @Value("${druid.datasource-third.url}")
    private String url;
    @Value("${druid.datasource-third.username}")
    private String username;
    @Value("${druid.datasource-third.password}")
    private String password;
    @Value("${druid.datasource.type}")
    private String type;
    @Value("${druid.datasource-third.driver-class-name}")
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

    @Bean(name = "thirdData")
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

    @Bean(name = "thirdSqlSessionFactory")
    public SqlSessionFactory thirdSqlSessionFactory(@Qualifier("thirdData") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath*:com/dotop/smartwater/project/third/server/meterread/client2/dao/third/**/*.xml"));
        return bean.getObject();
    }

    @Bean(name = "thirdTransactionManager")
    public DataSourceTransactionManager thirdTransactionManager(@Qualifier("thirdData") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean(name = "thirdSqlSessionTemplate")
    public SqlSessionTemplate thirdSqlSessionTemplate(@Qualifier("thirdSqlSessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception {
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}
