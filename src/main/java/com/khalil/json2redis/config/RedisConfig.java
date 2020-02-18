package com.khalil.json2redis.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import redis.clients.jedis.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;


@Configuration
@PropertySource("classpath:redis.properties")
@Slf4j
public class RedisConfig {

    @Value("${spring.redis.host}")
    private String host;

    @Value("${spring.redis.port}")
    private int port;

    @Value("${spring.redis.timeout}")
    private int timeout;

    @Value("${spring.redis.jedis.pool.max-idle}")
    private int maxIdle;

    @Value("${spring.redis.jedis.pool.max-wait}")
    private long maxWaitMillis;

    @Value("${spring.redis.password}")
    private String password;

    @Value("${spring.redis.block-when-exhausted}")
    private boolean  blockWhenExhausted;

    @Bean
    public JedisPool redisPoolFactory()  throws Exception{
        log.info("JedisPool注入成功！！");
        log.info("redis地址：" + host + ":" + port);
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxIdle(maxIdle);
        jedisPoolConfig.setMaxWaitMillis(maxWaitMillis);
        // 连接耗尽时是否阻塞, false报异常,ture阻塞直到超时, 默认true
        jedisPoolConfig.setBlockWhenExhausted(blockWhenExhausted);
        // 是否启用pool的jmx管理功能, 默认true
        jedisPoolConfig.setJmxEnabled(true);
        JedisPool jedisPool = new JedisPool(jedisPoolConfig, host, port, timeout);
        return jedisPool;
    }

//    @Bean
//    public JedisSentinelPool redisSentinelPoolFactory()throws Exception{
//        Set<String> sentinels = new HashSet<>(
//                Arrays.asList("192.168.226.201:26379", "192.168.226.201:26380", "192.168.226.201:26381"));
//        JedisPoolConfig config = new JedisPoolConfig();
//        config.setMaxTotal(10);
//        config.setMaxWaitMillis(1000);
//        //此处对poolConfig进行设置
//        JedisSentinelPool pool = new JedisSentinelPool("mymaster", sentinels, config);
//        return pool;
//    }

    @Bean
    public JedisCluster redisClusterFactory()throws Exception{
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        // 最大连接数
        poolConfig.setMaxTotal(1);
        // 最大空闲数
        poolConfig.setMaxIdle(1);
        // 最大允许等待时间，如果超过这个时间还未获取到连接，则会报JedisException异常：
        // Could not get a resource from the pool
        poolConfig.setMaxWaitMillis(1000);
        Set<HostAndPort> nodes = new LinkedHashSet<HostAndPort>();
        nodes.add(new HostAndPort("192.168.226.201", 7001));
        nodes.add(new HostAndPort("192.168.226.201", 7002));
        nodes.add(new HostAndPort("192.168.226.201", 7003));
        nodes.add(new HostAndPort("192.168.226.201", 7004));
        nodes.add(new HostAndPort("192.168.226.201", 7005));
        nodes.add(new HostAndPort("192.168.226.201", 7006));
        JedisCluster cluster = new JedisCluster(nodes, poolConfig);
        return cluster;
    }

}
