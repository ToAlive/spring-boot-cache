package com.example.springbootcache;

import com.example.springbootcache.bean.Employee;
import com.example.springbootcache.mapper.EmployeeMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringBootCacheApplicationTests {

    @Autowired
    EmployeeMapper employeeMapper;

    @Test
    public void contextLoads() {
        System.out.println(employeeMapper.getEmployee(1));
    }



    @Autowired
    StringRedisTemplate stringRedisTemplate; //k-v都是字符串的

    @Autowired
    RedisTemplate redisTemplate; //k-v都是Object的

    @Autowired
    RedisTemplate empRedisTemplate; //以json方式序列化的RedisTemplate

    /**
     * Redis常见的五大数据类型
     * String(字符串) List(列表) Set(集合) Hash(散列) ZSet(有序集合)
     * stringRedisTemplate.opsForValue() 操作字符串的
     * stringRedisTemplate.opsForHash() 操作哈希的
     * stringRedisTemplate.opsForList() 操作列表的
     * stringRedisTemplate.opsForSet() 操作集合的
     * stringRedisTemplate.opsForZSet() 操作有序集合的
     *  等等...
     *
     */
    @Test
    public void test01(){
        //给redis中保存数据
        //string
        //stringRedisTemplate.opsForValue().append("msg","hello");
        //list
        stringRedisTemplate.opsForList().leftPush("mylsit","1");
        stringRedisTemplate.opsForList().leftPush("mylsit","2");
    }

    @Test
    public void test02(){
        //从redis中读取数据
        String msg = stringRedisTemplate.opsForValue().get("msg");
        System.out.println(msg);
    }

    @Test
    public void test03(){
        //将对象保存到redis中
        /**
         * 异常信息：需要一个可序列化的类
         * Caused by: java.lang.IllegalArgumentException: DefaultSerializer
         * requires a Serializable payload but received an object of type
         * [com.example.springbootcache.bean.Employee]
         *  将Employee类实现序列号接口
         *
         * 改变redis默认的序列化机制
         * (1)自己将对象转换为json
         * (2)改变redis默认的序列化规则
         */

        Employee emp = employeeMapper.getEmployee(1);

        //默认的jdk序列化机制
        //redis默认使用jdk的序列化机制，将对象序列化后保存到redis中，缺点：数据看不懂
        //redisTemplate.opsForValue().set("emp01",emp);


        //json序列化机制的
        empRedisTemplate.opsForValue().set("emp01",emp);
    }
}
