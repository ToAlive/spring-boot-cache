package com.example.springbootcache.config;

import com.example.springbootcache.bean.Department;
import com.example.springbootcache.bean.Employee;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.JacksonJsonRedisSerializer;

import java.net.UnknownHostException;
import java.util.List;

/**
 * 改变redis默认的对象序列化机制
 */
@Configuration
public class MyRedisConfig {
    @Bean
    public RedisTemplate<Object, Employee> empRedisTemplate(RedisConnectionFactory redisConnectionFactory) throws UnknownHostException {
        RedisTemplate<Object, Employee> template = new RedisTemplate();
        template.setConnectionFactory(redisConnectionFactory);
        //改变redis默认的序列化机制，以json字符串的方式序列化对象
        Jackson2JsonRedisSerializer<Employee> jackson2JsonRedisSerializer =
                new Jackson2JsonRedisSerializer<>(Employee.class);
        template.setDefaultSerializer(jackson2JsonRedisSerializer);
        return template;
    }

    /*
     导入redis的stater以后RedisCacheManager缓存管理器就会生效它使用的RedisTemplatem是redis
     默认创建的，序列化机制
     是使用jdk的，现在想要使用json格式的序列化机制，所有需要自己来定义一个RedisCacheManager
     【RedisCacheConfiguration源码中的...】
    @Bean
    public RedisCacheManager cacheManager(RedisTemplate<Object, Object> redisTemplate) {
        RedisCacheManager cacheManager = new RedisCacheManager(redisTemplate);
        cacheManager.setUsePrefix(true);
        List<String> cacheNames = this.cacheProperties.getCacheNames();
        if (!cacheNames.isEmpty()) {
            cacheManager.setCacheNames(cacheNames);
        }
        //CacheManagerCustomizers ---> customizerInvoker 定制一些缓存的规则
        return (RedisCacheManager)this.customizerInvoker.customize(cacheManager);
    }*/



    @Primary //如果有多个同类型的bean的时候必须标记一个首选的来作为默认值，但是使用redis自己的CacheManager
    // 作为默认的比较好
    @Bean
    public RedisCacheManager empCacheManager(RedisTemplate<Object, Employee> empRedisTemplate) {
        RedisCacheManager cacheManager = new RedisCacheManager(empRedisTemplate);
        //这句代码的作用是使用前缀 在empService层里面设置的key是emp的id值---1:{结果集}，
        // 但是下面这句代码设置为true以后在redis数据库中的key就变成了--->这样：emp:1
        // 意思是 缓存的名字(cacheName):key的名字
        cacheManager.setUsePrefix(true);
        return cacheManager;
    }

    /*dept的缓存定制*/
    @Bean
    public RedisTemplate<Object, Department> deptRedisTemplate(RedisConnectionFactory redisConnectionFactory) throws UnknownHostException {
        RedisTemplate<Object, Department> template = new RedisTemplate();
        template.setConnectionFactory(redisConnectionFactory);
        //改变redis默认的序列化机制，以json字符串的方式序列化对象
        Jackson2JsonRedisSerializer<Department> jackson2JsonRedisSerializer =
                new Jackson2JsonRedisSerializer<>(Department.class);
        template.setDefaultSerializer(jackson2JsonRedisSerializer);
        return template;
    }

    @Bean
    public RedisCacheManager deptCacheManager(RedisTemplate<Object,Department> deptRedisTemplate) {
        RedisCacheManager cacheManager = new RedisCacheManager(deptRedisTemplate);
        //这句代码的作用是使用前缀 在empService层里面设置的key是emp的id值---1:{结果集}，
        // 但是下面这句代码设置为true以后在redis数据库中的key就变成了--->这样：emp:1
        // 意思是 缓存的名字(cacheName):key的名字
        cacheManager.setUsePrefix(true);
        return cacheManager;
    }
}
