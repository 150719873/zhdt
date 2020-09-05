package com.dotop.smartwater.project.third.server.meter.config;

import com.alibaba.fastjson.support.spring.GenericFastJsonRedisSerializer;
import com.dotop.smartwater.dependence.cache.HashCache;
import com.dotop.smartwater.dependence.cache.StringListCache;
import com.dotop.smartwater.dependence.cache.StringValueCache;
import com.dotop.smartwater.dependence.cache.ValueCache;
import com.dotop.smartwater.dependence.cache.api.AbstractHashCache;
import com.dotop.smartwater.dependence.cache.api.AbstractValueCache;
import com.dotop.smartwater.dependence.lock.RedisDistributedLock;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.CacheKeyPrefix;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.*;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration.JedisClientConfigurationBuilder;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration.JedisPoolingClientConfigurationBuilder;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.serializer.RedisSerializationContext.SerializationPair;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.clients.jedis.JedisPoolConfig;

import java.lang.reflect.Method;
import java.time.Duration;

/**
 * redis配置
 *
 *
 */
@Configuration
public class RedisConfig extends CachingConfigurerSupport {

    @Value("${spring.cache.redis.time-to-live:3600}")
    private long timeToLive;

    @Bean
    @Override
    public KeyGenerator keyGenerator() {
        return new KeyGenerator() {
            @Override
            public Object generate(Object target, Method method, Object... params) {
                StringBuilder sb = new StringBuilder();
                if (params != null && params.length > 0) {
                    for (Object o : params) {
                        sb.append('_').append(String.valueOf(o));
                    }
                }
                return sb.toString();
            }
        };
    }

    // 自定前缀
    @FunctionalInterface
    interface MyCacheKeyPrefix extends CacheKeyPrefix {
        static CacheKeyPrefix mySimple() {
            return name -> name + ":";
        }
    }

    private RedisConnectionFactory connectionFactorys(String hostnames, int ports, String password, int maxIdle,
                                                      int minIdle, int maxTotal, int index, long maxWaitMillis, boolean testOnBorrow) {
        RedisSentinelConfiguration configuration = new RedisSentinelConfiguration().master("mymaster");
        String[] hosts = hostnames.split(",");
        for (String host : hosts) {
            configuration.addSentinel(new RedisNode(host, ports));
        }
        configuration.setPassword(RedisPassword.of(password));
        configuration.setDatabase(index);
        JedisPoolConfig config = poolCofig(maxIdle, minIdle, maxTotal, maxWaitMillis, testOnBorrow);

        // 连接池配置
        JedisClientConfigurationBuilder builder = JedisClientConfiguration.builder();
        JedisPoolingClientConfigurationBuilder usePooling = builder.usePooling();
        usePooling.poolConfig(config);
        JedisClientConfiguration build = builder.build();

        // 创建连接
        JedisConnectionFactory jedis = new JedisConnectionFactory(configuration, build);
        // 初始化连接pool
        jedis.afterPropertiesSet();
        RedisConnectionFactory factory = jedis;

        return factory;
    }

    private RedisConnectionFactory connectionFactory(String hostName, int port, String password, int maxIdle,
                                                     int minIdle, int maxTotal, int index, long maxWaitMillis, boolean testOnBorrow) {
        // 使用单例的redis
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration();
        configuration.setHostName(hostName);
        configuration.setPort(port);
        configuration.setPassword(RedisPassword.of(password));
        configuration.setDatabase(index);

        // 连接池配置
        JedisClientConfigurationBuilder builder = JedisClientConfiguration.builder();
        JedisPoolingClientConfigurationBuilder usePooling = builder.usePooling();
        usePooling.poolConfig(poolCofig(maxIdle, minIdle, maxTotal, maxWaitMillis, testOnBorrow));
        JedisClientConfiguration build = builder.build();
        // 创建连接
        JedisConnectionFactory jedis = new JedisConnectionFactory(configuration, build);
        // 初始化连接pool
        jedis.afterPropertiesSet();
        RedisConnectionFactory factory = jedis;
        return factory;
    }

    private JedisPoolConfig poolCofig(int maxIdle, int minIdle, int maxTotal, long maxWaitMillis,
                                      boolean testOnBorrow) {
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxIdle(maxIdle);
        poolConfig.setMinIdle(minIdle);
        poolConfig.setMaxTotal(maxTotal);
        poolConfig.setMaxWaitMillis(maxWaitMillis);
        poolConfig.setTestOnBorrow(testOnBorrow);
        return poolConfig;
    }

    // 缓存使用
    @Bean
    public RedisCacheManager cacheManager(@Qualifier("cacheConnectionFactory") RedisConnectionFactory factory) {
        RedisCacheWriter cacheWriter = RedisCacheWriter.lockingRedisCacheWriter(factory);
        StringRedisSerializer stringSerializer = new StringRedisSerializer();
        SerializationPair<String> spair = SerializationPair.fromSerializer(stringSerializer);
        GenericFastJsonRedisSerializer fastJSON = new GenericFastJsonRedisSerializer();
        SerializationPair<Object> fpair = SerializationPair.fromSerializer(fastJSON);
        Duration ttl = Duration.ofSeconds(timeToLive);// 3600秒
        RedisCacheConfiguration cacheConfig = RedisCacheConfiguration.defaultCacheConfig().serializeKeysWith(spair)
                .serializeValuesWith(fpair).entryTtl(ttl).computePrefixWith(MyCacheKeyPrefix.mySimple());
        RedisCacheManager cacheManager = new RedisCacheManager(cacheWriter, cacheConfig);
        return cacheManager;
    }

    @Bean("cacheConnectionFactory")
    public RedisConnectionFactory cacheConnectionFactory(@Value("${redis.cache.is-sentinel:false}") boolean isSentinel,
                                                         @Value("${redis.cache.host:0}") String hostName, @Value("${redis.cache.port:0}") int port,
                                                         @Value("${redis.cache.hosts:0}") String hostNames, @Value("${redis.cache.ports:0}") int ports,
                                                         @Value("${redis.cache.password}") String password,
                                                         @Value("${redis.cache.test-on-borrow:true}") boolean testOnBorrow,
                                                         @Value("${redis.cache.max-idle:1024}") int maxIdle, @Value("${redis.cache.min-idle:8}") int minIdle,
                                                         @Value("${redis.cache.max-active:1024}") int maxTotal, @Value("${redis.cache.database}") int index,
                                                         @Value("${redis.cache.max-wait:3000}") long maxWaitMillis) {
        RedisConnectionFactory factory = null;
        if (isSentinel) {
            factory = connectionFactorys(hostNames, ports, password, maxIdle, minIdle, maxTotal, index, maxWaitMillis,
                    testOnBorrow);
        } else {
            factory = connectionFactory(hostName, port, password, maxIdle, minIdle, maxTotal, index, maxWaitMillis,
                    testOnBorrow);
        }
        return factory;
    }

    @Bean(name = "cacheRedisTemplate")
    public RedisTemplate<String, String> cacheRedisTemplate(
            @Qualifier("cacheConnectionFactory") RedisConnectionFactory redisConnectionFactory) {
        StringRedisTemplate template = new StringRedisTemplate(redisConnectionFactory);
        StringRedisSerializer redisSerializer = new StringRedisSerializer();
        template.setKeySerializer(redisSerializer);
        template.setValueSerializer(redisSerializer);
        template.setHashValueSerializer(redisSerializer);
        template.afterPropertiesSet();
        return template;
    }

    @Bean(name = "cacheOpsForValue")
    public ValueOperations<String, String> cacheOpsForValue(
            @Qualifier("cacheRedisTemplate") RedisTemplate<String, String> cacheRedisTemplate) {
        ValueOperations<String, String> opsForValue = cacheRedisTemplate.opsForValue();
        return opsForValue;
    }

    @Bean(name = "cacheOpsForHash")
    public HashOperations<String, String, String> cacheOpsForHash(
            @Qualifier("cacheRedisTemplate") RedisTemplate<String, String> cacheRedisTemplate) {
        HashOperations<String, String, String> opsForHash = cacheRedisTemplate.opsForHash();
        return opsForHash;
    }

    @Bean(name = "cacheOpsForList")
    public ListOperations<String, String> cacheOpsForList(
            @Qualifier("cacheRedisTemplate") RedisTemplate<String, String> cacheRedisTemplate) {
        ListOperations<String, String> opsForList = cacheRedisTemplate.opsForList();
        return opsForList;
    }

    @Bean
    public RedisDistributedLock redisDistributedLock(
            @Qualifier("cacheRedisTemplate") RedisTemplate<String, String> redisTemplate) {
        RedisDistributedLock rdl = new RedisDistributedLock(redisTemplate);
        return rdl;
    }

    @Bean
    public AbstractValueCache<?> getValueCache() {
        ValueCache<?> valueCache = new ValueCache<>();
        return valueCache;
    }

    @Bean
    public AbstractHashCache<?> getHashCache() {
        HashCache<?> hashCache = new HashCache<>();
        return hashCache;
    }

    @Bean
    public StringValueCache getStringValueCache() {
        StringValueCache svc = new StringValueCache();
        return svc;
    }

    @Bean
    public StringListCache getStringListCache() {
        StringListCache svc = new StringListCache();
        return svc;
    }
}
