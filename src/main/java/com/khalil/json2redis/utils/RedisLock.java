package com.khalil.json2redis.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import redis.clients.jedis.JedisCluster;

import java.util.Collections;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

@Component
@Slf4j
public class RedisLock implements Lock {
    @Autowired
    private JedisCluster jedisCluster;

    private static final String KEY = "LOCK_KEY";
    private static final String LOCK_SUCCESS = "OK";
    private static final String SET_IF_NOT_EXIST = "NX";
    private static final String SET_WITH_EXPIRE_TIME = "PX";
    ThreadLocal<String> local=new ThreadLocal<>();

    @Override
    public void lock() {
        if(tryLock()){
            return;
        }
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        lock();
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {

    }

    @Override
    public boolean tryLock() {
        String uuid= UUID.randomUUID().toString();
        String result = jedisCluster.set(KEY, uuid, SET_IF_NOT_EXIST, SET_WITH_EXPIRE_TIME, 1000);
        if (LOCK_SUCCESS.equals(result)) {
            local.set(uuid);
            return true;
        }
        return false;
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        String uuid= UUID.randomUUID().toString();
        String result = jedisCluster.set(KEY, uuid, SET_IF_NOT_EXIST, SET_WITH_EXPIRE_TIME, time);
        if (LOCK_SUCCESS.equals(result)) {
            return true;
        }
        return false;
    }

    @Override
    public void unlock() {
        String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
        jedisCluster.eval(script, Collections.singletonList(KEY), Collections.singletonList(local.get()));
    }

    @Override
    public Condition newCondition() {
        return null;
    }
}
