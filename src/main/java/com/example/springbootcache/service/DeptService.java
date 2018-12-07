package com.example.springbootcache.service;

import com.example.springbootcache.bean.Department;
import com.example.springbootcache.mapper.DepartmentMpper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.Cache;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.stereotype.Service;

@CacheConfig(cacheManager = "deptCacheManager")
@Service
public class DeptService {

    @Autowired
    DepartmentMpper deptMapper;

    /*如果只配置了一个emp的empRedisTemplate和RedisCacheManager那么在将数据存入redis库的时候是可以存进去的
    * 但是从redis库中将序列化的数据反序列化回来的时候会出错，因为使用的是emp的empRedisTemplate
    * 和RedisCacheManager
    * 因此需要根据不同的情况定制不同对象的RedisTemplate和和RedisCacheManager*/
    /*@Cacheable(value = {"dept"},key = "#id")
    public Department getDept(Integer id){
        Department department = deptMapper.getDeptById(id);
        return department;
    }*/

    //不使用注解的方式操作缓存，用编码的方式操作缓存
    @Qualifier("deptCacheManager") //指明要注入的bean
    @Autowired
    RedisCacheManager deptCacheManager;

    //但这个方法不是可以缓存的
    public Department getDept(Integer id){
        Department department = deptMapper.getDeptById(id);
        //通过缓存管理器获取缓存
        Cache deptCache = this.deptCacheManager.getCache("deptCacheManager");
        deptCache.put("dept2",department);
        return department;
    }
}
