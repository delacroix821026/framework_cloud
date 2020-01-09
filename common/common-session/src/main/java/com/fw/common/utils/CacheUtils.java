/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.fw.common.utils;

import com.fw.common.service.CacheService;

/**
 * Cache工具类
 *
 * @author ThinkGem
 * @version 2013-5-29
 */
public class CacheUtils {

    private static CacheService cacheService = null;//(CacheService) SpringContextHolder.getBean("");

    /**
     * 获取SYS_CACHE缓存
     *
     * @param key String
     * @return Object
     */
    public static Object get(String key) {
        return cacheService.get(key);
    }

    /**
     * 写入SYS_CACHE缓存
     *
     * @param key   String
     * @param value Object
     */
    public static void put(String key, Object value) {
        cacheService.set(key, value);
    }

    /**
     * 从SYS_CACHE缓存中移除
     *
     * @param key String
     */
    public static void remove(String key) {
        cacheService.delete(key);
    }

}
