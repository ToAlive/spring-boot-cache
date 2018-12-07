package com.example.springbootcache;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

/**
 * 搭建基本环境
 *  1.导入数据库文件，创建出department和employee表
 *  2.创建javabean封装数据
 *  3.这个和MyBatis操作数据库
 *      1.配置数据源信息
 *      2.使用注解版本的mybatis
 *          1.@MapperScan注解需要扫描的mapper接口
 * --------------------------------------------------
 * 默认使用的是ConcurrentMapCacheManager==ConcurrentMapCache
 * 将数据保存在ConcurrentMap<Object,Object>
 * 开发中使用缓存中间件：redis、memcache、ehcache
 * 整合redis作为缓存
 *  1.安装redis
 *  2.引入redis的starter
 *      RedisAutoConfiguration这个自动配置就起作用了
 *  3.配置redis
 *  4.测试缓存
 *      原理：CacheManager==Cache 缓存组件来实际给缓存中存取数据
 *          1.引入redis的stater后，容器中保存的是RedisCacheConfiguration
 *              默认的SimpleCacheConfiguration就不会起作用了
 *              RedisCacheConfiguration会为我们创建默认的RedisCacheManager
 *          2.RedisCacheManager帮我们创建RedisCache来作为缓存组件
 *          RedisCache通过redis来操作缓存
 *          3.默认保存数据是k-v都是object：利用序列化保存（默认的jdk），如果保存为json？
 *               1.引入redis的stater，cacheManager变为默认创建的RedisCacheManager
 *               2.默认创建的RedisCacheManager 在操作数据库的时候使用的是：
 *                      RedisTemplate<Object, Object> redisTemplate
 *                      他默认用的序列化机制是jdk的：
 *                      this.defaultSerializer = new JdkSerializationRedisSerializer()
 *              3.要改变他的默认序列化机制，就需要重写一个RedisCacheManager，将自己定义的序列化机制
 *              RedisTemplate传递给他
 */
@MapperScan(value = {"com.example.springbootcache.mapper"})
@SpringBootApplication
@EnableCaching //要使用缓存必须要开启缓存
public class SpringBootCacheApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootCacheApplication.class, args);
    }
}
