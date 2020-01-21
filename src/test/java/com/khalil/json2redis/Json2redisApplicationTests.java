package com.khalil.json2redis;

import com.khalil.json2redis.utils.RedisUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import redis.clients.jedis.BinaryClient;
import redis.clients.jedis.SortingParams;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.*;

@SpringBootTest
class Json2redisApplicationTests {

    @Autowired
    private RedisUtil redisUtil;

    @Test
    void get3set() {
        byte[] key="name".getBytes();
        byte[] value="khalil".getBytes();
        redisUtil.set(key,value,0);
        System.out.println(new String(redisUtil.get(key,0), StandardCharsets.UTF_8));
    }

    @Test
    void del(){
        redisUtil.del(1,"name");
    }

    @Test
    void append(){
        redisUtil.append("name","s",1);
    }

    @Test
    void isExist(){
        System.out.println(redisUtil.exists("name"));
    }

    @Test
    void flushDB(){
        redisUtil.flushDB();
    }

    @Test
    void expire(){
        redisUtil.expire("name",30,0);
    }

    @Test
    void ttl(){
        System.out.println(redisUtil.ttl("name",0));
    }

    @Test
    void persist(){
        redisUtil.persist("name");
    }

    @Test
    void hset(){
        for (int i = 0; i < 10; i++) {
            redisUtil.hset("user","name"+i,"zhaokai"+i);
        }
    }

    @Test
    void hmset(){
        Map<String,String> user=new HashMap();
        user.put("id","1");
        user.put("name","caojing");
        user.put("age","25");
        redisUtil.hmset("user1",user,0);
    }

    @Test
    void hmget(){
        String[] fields={"id","name","age"};
        System.out.println(redisUtil.hmget("user1",0,fields));
    }

    @Test
    void hkeys(){
        System.out.println(redisUtil.hkeys("user1"));
    }

    @Test
    void hvals(){
        System.out.println(redisUtil.hvals("user1"));
    }

    @Test
    void hgetAll(){
        System.out.println(redisUtil.hgetall("user1",0));
    }

    @Test
    void lpush(){
        List<String> strings=new ArrayList<>();
        Random r=new Random();
        for (int i = 0; i < 10; i++) {
            strings.add(r.nextInt(10)+"");
        }
        //list转数组
        String[] al= strings.toArray(new String[strings.size()]);
        System.out.println(al);
        redisUtil.lpush(0,"list3", al);
        //redisUtil.lpush(0,"list2","age");
    }

    @Test
    void linsert(){
        redisUtil.linsert("list2", BinaryClient.LIST_POSITION.AFTER,"name6","khalil");
    }

    @Test
    void ltrim(){
        redisUtil.ltrim("list2",2,5);
    }

    @Test
    void sort(){
        List list=redisUtil.sort("list3", new SortingParams().desc());
        System.out.println(list);
    }

    @Test
    void sadd(){
        List<String> strings=new ArrayList<>();
        Random r=new Random();
        for (int i = 0; i < 10; i++) {
            strings.add(r.nextInt(10)+"");
        }
        //list转数组
        String[] al= strings.toArray(new String[strings.size()]);
        System.out.println(al);
        redisUtil.sadd("set1",al);
    }

    @Test
    void spop(){
        String s=redisUtil.spop("set1");
        System.out.println(s);
    }

    @Test
    void scard(){
        System.out.println(redisUtil.scard("set1"));
    }

    @Test
    void zadd(){
        redisUtil.zadd("zset",102,"jessie");
        redisUtil.zadd("zset",99,"Bob");
    }

    @Test
    void zincrBy(){
        redisUtil.zincrby("zset",12,"Bob");
    }

    @Test
    void zrank(){
        System.out.println(redisUtil.zrevrank("zset", "khalil"));
    }

    @Test
    void setBit(){
        redisUtil.setBit("bit1",0,true);
        redisUtil.setBit("bit1",1,true);
        redisUtil.setBit("bit1",2,false);
        redisUtil.setBit("bit1",3,true);
        redisUtil.setBit("bit1",4,true);
        redisUtil.setBit("bit1",5,true);
        redisUtil.setBit("bit1",6,true);
        redisUtil.setBit("bit1",7,true);
        redisUtil.setBit("bit1",8,false);
        redisUtil.setBit("bit1",9,true);
    }

    @Test
    void getBit(){
        System.out.println(redisUtil.getBit("bit1", 0));
        System.out.println(redisUtil.getBit("bit1", 1));
        System.out.println(redisUtil.getBit("bit1", 2));
        System.out.println(redisUtil.getBit("bit1", 3));
        System.out.println(redisUtil.getBit("bit1", 4));
        System.out.println(redisUtil.getBit("bit1", 5));
        System.out.println(redisUtil.getBit("bit1", 6));
        System.out.println(redisUtil.getBit("bit1", 7));
        System.out.println(redisUtil.getBit("bit1", 8));
        System.out.println(redisUtil.getBit("bit1", 9));
    }

    @Test
    void bitCount(){
        System.out.println(redisUtil.bitCount("bit1"));
    }

    @Test
    void bitCountByRange(){
        System.out.println(redisUtil.bitCountByRange("bit1", 0l, 8l));
    }

    @Test
    void hyperAdd(){
        for (int i = 0; i < 100000; i++) {
            redisUtil.pfadd("hyper","name"+i);
        }
        System.out.println("总数为："+redisUtil.pfcount("hyper"));
    }

    @Test
    void test(){
        System.out.println("git upload test");
    }

}
