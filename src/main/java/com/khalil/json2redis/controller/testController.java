package com.khalil.json2redis.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.khalil.json2redis.entity.User;
import com.khalil.json2redis.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.Response;

import java.io.IOException;

@RestController
public class testController {

    @Autowired
    private RedisUtil redisUtil;

    @RequestMapping("/set")
    public String set(){
        User user=new User();
        user.setId(1);
        user.setName("Khalil");
        user.setAge(27);
        String json= JSON.toJSONString(user);
        redisUtil.set("user",json,0);
        return user.toString();
    }

    @RequestMapping("/get")
    public String get(){
        String json=redisUtil.get("user",0);
        User user=JSON.parseObject(json,User.class);
        return user.toString();
    }

    @RequestMapping("/rate")
    public void rate() throws Exception {
        for(int i=0;i<20;i++){
            Thread.sleep(100);
            System.out.println(isActionAllowed("khalil","reply",10,5,i));
        }
    }
//    适用于在一个时间段内限制用户操作次数（限流）
    public boolean isActionAllowed(String userId,String actionKey,int period,int maxCount,int i) throws Exception {
        String key=String.format("hist:%s:%s",userId,actionKey);
        long nowTs=System.currentTimeMillis();
        Pipeline pipeline=redisUtil.getJedis().pipelined();
        pipeline.multi();
        pipeline.zadd(key,nowTs,""+nowTs+"-"+i);
        pipeline.zremrangeByScore(key,0,nowTs-(period*1000));
        //0 10 5-> 5-10
        //0 11 5-> 6-11
        Response<Long> count=pipeline.zcard(key);
        pipeline.expire(key,period+1);
        pipeline.exec();
        pipeline.close();
        return count.get()<=maxCount;
    }


}
