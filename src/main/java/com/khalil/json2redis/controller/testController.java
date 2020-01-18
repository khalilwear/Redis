package com.khalil.json2redis.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.khalil.json2redis.entity.User;
import com.khalil.json2redis.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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


}
