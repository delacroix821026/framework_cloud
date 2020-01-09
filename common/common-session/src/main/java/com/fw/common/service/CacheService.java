/*
 * Copyright (C), 2014-2015, 上海澍勋电子商务有限公司
 * FileName: CacheService.java
 * Author:   long.yu
 * Date:     2015年6月5日 上午10:47:08
 * Description: //模块目的、功能描述      
 * History: //修改记录
 * <author>      <time>      <version>    <desc>
 * 修改人姓名             修改时间            版本号                  描述
 */
package com.fw.common.service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 缓存接口
 *
 * @author long.yu
 * @since [产品/模块版本] （可选）
 */
public interface CacheService {

    /**
     * 设置缓存
     *
     * @param key        缓存key
     * @param expireDate 缓存有效期
     * @return boolean
     * @since [产品/模块版本](可选)
     */
    boolean setExpireDate(String key, Date expireDate);

    /**
     * 删除缓存
     *
     * @param key\ 缓存key
     * @return boolean
     * @since [产品/模块版本](可选)
     */
    boolean delete(String key);

    /**
     * 是否存在缓存
     *
     * @param key 缓存key
     * @return boolean
     * @since [产品/模块版本](可选)
     */
    boolean exists(String key);

    /**
     * 设置缓存
     *
     * @param key    String
     *               缓存key
     * @param expire Integer
     *               缓存有效期：单位秒
     * @return boolean
     * @since [产品/模块版本](可选)
     */
    boolean setExpireSec(String key, Integer expire);

    /**
     * 获取缓存
     *
     * @param key 缓存key
     * @return Object
     * @since [产品/模块版本](可选)
     */
    Object get(String key);

    /**
     * 设置缓存
     *
     * @param key 缓存key
     * @param obj 缓存值
     * @return boolean
     * @since [产品/模块版本](可选)
     */
    boolean set(String key, Object obj);

    public boolean set(String key, Object obj, Integer expire);

    public boolean set(String key, Object obj, Date expireDate);


    //map area
    public Object getMap(String key, String mapKey);

    public boolean setMap(String key, String mapKey, Object obj);

    public boolean setMap(String key, String mapKey, Object obj, Integer expire);

    public boolean setMap(String key, String mapKey, Object obj, Date expireDate);

    public boolean setMap(String key, Map<String, Object> map);

    public boolean setMap(String key, Map<String, Object> map, Integer expire);

    public boolean setMap(String key, Map<String, Object> map, Date expireDate);

    public boolean deleteMap(String key, String mapKey);

    //list area
    public List getList(String key);

    public boolean setList(String key, Object obj);

    public boolean setList(String key, Object obj, Integer expire);

    public boolean setList(String key, Object obj, Date expireDate);

    public boolean setList(String key, List list);

    public boolean setList(String key, List list, Integer expire);

    public boolean setList(String key, List list, Date expireDate);

    //set area
    public Set getSet(String key);

    public boolean setSet(String key, Object obj);

    public boolean setSet(String key, Object obj, Integer expire);

    public boolean setSet(String key, Object obj, Date expireDate);

    public boolean setSet(String key, Set set);

    public boolean setList(String key, Set set, Integer expire);

    public boolean setSet(String key, Set set, Date expireDate);

}
