package com.example.springbootcache.service;

import com.example.springbootcache.bean.Employee;
import com.example.springbootcache.mapper.EmployeeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.*;
import org.springframework.stereotype.Service;
//@CacheConfig 抽取缓存的公共配置信息
//如果在@CacheConfig中指定了一些公共的配置信息那么在@Cacheable、
//@CachePut @CacheEvict等这些注解中就不需要重复的指定了
@CacheConfig(cacheNames = {"emp"})
@Service
public class EmployeeService {

    @Autowired
    EmployeeMapper employeeMapper;

    /**
     *  CacheManager管理多个Cache组件，对缓存的真正操作再Cache组件中，每一个缓存组件有自己的一个名字
     *  可用的属性
     *      cacheNames/value:指定缓存组件的名字。
     *      key：缓存数据使用的key：可以用来它指定。默认是使用方法参数的值 如：1:方法的返回值
     *              可以编写SpEL表达式：#id:参数的id值 === #a0  ===#root.args[0]
     *      keyGenerator：key的生成器：可以自己指定key的生成器组件
     *              使用时：key和keyGenerator二选一
     *      cacheManager:指定缓存管理器，或者cacheResolver指定获取解析器
     *      condition：指定缓存的条件，当指定的条件为true时才进行缓存
     *                  可以使用SpEL表达式
     *      unless：否定缓存，当指定的条件为true时则不保存缓存,可以获取到结果进行判断
     *                  unless = "#result==null"
     *                  可以使用SpEL表达式
     *      sync:是否是异步的，默认为false
     *
     * 原理
     *  1.自动配置类 CacheAutoConfiguration
     *  2.缓存的配置类
     *      org.springframework.boot.autoconfigure.cache.GenericCacheConfiguration
     *      org.springframework.boot.autoconfigure.cache.JCacheCacheConfiguration
     *      org.springframework.boot.autoconfigure.cache.EhCacheCacheConfiguration
     *      org.springframework.boot.autoconfigure.cache.HazelcastCacheConfiguration
     *      org.springframework.boot.autoconfigure.cache.InfinispanCacheConfiguration
     *      org.springframework.boot.autoconfigure.cache.CouchbaseCacheConfiguration
     *      org.springframework.boot.autoconfigure.cache.RedisCacheConfiguration
     *      org.springframework.boot.autoconfigure.cache.CaffeineCacheConfiguration
     *      org.springframework.boot.autoconfigure.cache.GuavaCacheConfiguration
     *      org.springframework.boot.autoconfigure.cache.SimpleCacheConfiguration [默认生效]
     *      org.springframework.boot.autoconfigure.cache.NoOpCacheConfiguration
     *
     * 3.哪个缓存配置类默认生效？
     *
     * 4.给容器中注册了一个CacheManger ConcurrentMapCacheManager
     * 5.可用获取和创建ConcurrentMapCache类型的缓存组件：他的作用是将数据保存的ConcurrentMap中
     *
     * 运行流程：
     *  1.方法执行之前，先去查询Cache(缓存组件)，按照cacheNames指定名字获取
     *  2.取Cache中查找缓存的内容，使用一个key，默认就是方法的参数
     *      key是按照某种策略生成的，默认是使用keyGenerator生成的，默认使用SimpleKeyGenerator生成key
     *          SimpleKeyGenerator生成key的默认策略
     *              如果没用参数：key=new SimpleKey();
     *              如果有一个参数：key=参数值的
     *              如果有多个参数：key=new SimpleKey(params)
     *  3.没用查询到缓存就调用目标方法
     *  4.将目标方法返回的结果，放到缓存中
     *
     * 核心：
     *  1.使用CacheManager【ConcurrentMapCacheManager】按照名字得到Cache【ConcurrentMapCache】组件
     *  2.key使用keyGenerator生成的，默认是SimpleKeyGenerator
     */
    //将方法的运行结果缓存，以后直接从缓存中取，不在调用方法
    //@Cacheable标注的方法执行之前先来检查缓存中有没有这个数据，默认按照参数的值作为key去查询缓存
    //如果没有就允许方法并将结果放入缓存，以后再来调用就直接使用缓存中的数据
    @Cacheable(value = {"emp"}
            ,key = "#id"
            /*,key = "#root.methodName+'['+#root.args[0]+']'"*/
            /*,keyGenerator = "myKeyGenerator"*//*使用自定的key生成策略*/
            ,cacheManager = "empCacheManager"/*这里可以不用些，因为empCacheManager
            使用@Primary注解标记为默认的了*/)
    public Employee getEmp(Integer id){
        System.out.println("查询" + id + "员工");
        Employee employee = employeeMapper.getEmployee(id);
        return employee;
    }

    //@CachePut既调用方法，又缓存数据
    //运行时机：
    //  1.先调用目标方法
    //  2.将目标方法的返回值缓存起来
    //需要注意：更新缓存用的key值要和存入缓存中用的key值必须一样才能达到更新缓存的效果
    @CachePut(value = "emp",key = "#result.id")
    public int updateEmp(Employee employee){
        int num = employeeMapper.updateEmp(employee);
        return num;
    }

    //@CacheEvict  缓存清除
    //,allEntries = true 一次清除所有的缓存默认是fasle
    // beforeInvocation = true 是否在目标方法执行之前删除缓存，默认是false ,如果方法出错了就不会清除缓存
    @CacheEvict(value = "emp",key = "#id",beforeInvocation = true)
    public int delEmp(Integer id){
        return employeeMapper.delEmp(id);
    }


    //@Caching 定义复杂的缓存规则
    @Caching(
            cacheable = {@Cacheable(value = "emp",key = "#lastName")},
            put = {@CachePut(value = "emp",key = "#result.id"),
                    @CachePut(value = "emp",key = "#result.email")}
    )
    public Employee getEmpByLastName(String lastName){
        Employee employee = employeeMapper.getEmployeeByLastName(lastName);
        return employee;
    }
}
