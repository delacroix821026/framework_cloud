/*
 * Copyright (C), 2014-2015, 上海澍勋电子商务有限公司
 * FileName: RedisServiceImpl.java
 * Author:   long.yu
 * Date:     2015年6月5日 上午10:51:04
 * Description: //模块目的、功能描述      
 * History: //修改记录
 * <author>      <time>      <version>    <desc>
 * 修改人姓名             修改时间            版本号                  描述
 */
package com.fw.common.service.impl;

import com.fw.common.service.CacheService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;


/**
 * Redis实现
 * 
 * @author long.yu
 * @since [产品/模块版本] （可选）
 */
@Service("cacheService")
@Slf4j
public class RedisServiceImpl implements CacheService {
    @Autowired
    private RedisTemplate redisTemplate;

    //common area
    @Override
    public boolean setExpireDate(String key, Date expireDate) {
        Date now = new Date();
        long expireDateTime = expireDate.getTime();
        long nowTime = now.getTime();

        return setExpireSec(key, (int) ((expireDateTime - nowTime) / 1000));
    }

    @Override
    public boolean setExpireSec(String key, Integer expire) {
        if(expire == null || expire.intValue() == 0) {
            return true;
        }
        redisTemplate.expire(key, expire, TimeUnit.SECONDS);
        return true;
    }

    @Override
    public boolean delete(String key) {
        redisTemplate.delete(key);
        return true;
    }

    @Override
    public boolean exists(String key) {
        return redisTemplate.hasKey(key);
    }

    //set area
    @Override
    public Object get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    @Override
    public boolean set(String key, Object obj) {
        if (obj == null) {
            return false;
        }

        redisTemplate.opsForValue().set(key, obj);
        return true;
    }

    @Override
    public boolean set(String key, Object obj, Integer expire) {
        if (set(key, obj)) {
            return setExpireSec(key, expire);
        }

        return false;
    }

    @Override
    public boolean set(String key, Object obj, Date expireDate) {
        if (set(key, obj)) {
            return setExpireDate(key, expireDate);
        }

        return false;
    }


    //map area
    @Override
    public Object getMap(String key, String mapKey) {
        return redisTemplate.opsForHash().get(key, mapKey);
    }

    @Override
    public boolean setMap(String key, String mapKey, Object obj) {
        if(obj == null) {
            return false;
        }
        return redisTemplate.opsForHash().putIfAbsent(key, mapKey, obj);
    }

    @Override
    public boolean setMap(String key, String mapKey, Object obj, Integer expire) {
        if (setMap(key, mapKey, obj)) {
            return setExpireSec(key, expire);
        }

        return false;
    }

    @Override
    public boolean setMap(String key, String mapKey, Object obj, Date expireDate) {
        if (setMap(key, mapKey, obj)) {
            return setExpireDate(key, expireDate);
        }

        return false;
    }

    @Override
    public boolean setMap(String key, Map<String, Object> map) {
        if(map == null || map.size() == 0) {
            return false;
        }
        redisTemplate.opsForHash().putAll(key, map);
        return true;
    }

    @Override
    public boolean setMap(String key, Map<String, Object> map, Integer expire) {
        if (setMap(key, map)) {
            return setExpireSec(key, expire);
        }

        return false;
    }

    @Override
    public boolean setMap(String key, Map<String, Object> map, Date expireDate) {
        if (setMap(key, map)) {
            return setExpireDate(key, expireDate);
        }

        return false;
    }

    @Override
    public boolean deleteMap(String key, String mapKey) {
        redisTemplate.opsForHash().delete(key, mapKey);
        return true;
    }

    //list area
    @Override
    public List getList(String key) {
        return redisTemplate.opsForList().range(key, 0, redisTemplate.opsForList().size(key));
    }

    @Override
    public boolean setList(String key, Object obj) {
        if(obj == null) {
            return false;
        }
        redisTemplate.opsForList().rightPush(key, obj);
        return true;
    }

    @Override
    public boolean setList(String key, Object obj, Integer expire) {
        if (setList(key, obj)) {
            return setExpireSec(key, expire);
        }

        return false;
    }

    @Override
    public boolean setList(String key, Object obj, Date expireDate) {
        if (setList(key, obj)) {
            return setExpireDate(key, expireDate);
        }

        return false;
    }

    @Override
    public boolean setList(String key, List list) {
        if(list == null || list.size() == 0) {
            return false;
        }
        redisTemplate.opsForList().rightPushAll(key, list);
        return true;
    }

    @Override
    public boolean setList(String key, List list, Integer expire) {
        if (setList(key, list)) {
            return setExpireSec(key, expire);
        }

        return false;
    }

    @Override
    public boolean setList(String key, List list, Date expireDate) {
        if (setList(key, list)) {
            return setExpireDate(key, expireDate);
        }

        return false;
    }

    //set area
    //list area
    @Override
    public Set getSet(String key) {
        return redisTemplate.opsForSet().members(key);
    }

    @Override
    public boolean setSet(String key, Object obj) {
        if(obj == null) {
            return false;
        }
        redisTemplate.opsForSet().add(key, obj);
        return true;
    }

    @Override
    public boolean setSet(String key, Object obj, Integer expire) {
        if (setSet(key, obj)) {
            return setExpireSec(key, expire);
        }

        return false;
    }

    @Override
    public boolean setSet(String key, Object obj, Date expireDate) {
        if (setSet(key, obj)) {
            return setExpireDate(key, expireDate);
        }

        return false;
    }

    @Override
    public boolean setSet(String key, Set set) {
        if(set == null || set.size() == 0) {
            return false;
        }
        redisTemplate.opsForSet().intersect(key, set);
        return true;
    }

    @Override
    public boolean setList(String key, Set set, Integer expire) {
        if (setSet(key, set)) {
            return setExpireSec(key, expire);
        }

        return false;
    }

    @Override
    public boolean setSet(String key, Set set, Date expireDate) {
        if (setList(key, set)) {
            return setExpireDate(key, expireDate);
        }

        return false;
    }
}
