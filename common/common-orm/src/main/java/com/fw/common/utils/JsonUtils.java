package com.fw.common.utils;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;

public class JsonUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(JsonUtils.class);

    /*** 
     * 获取JSON类型 
     * 判断规则 ：判断第一个字母是否为{或[ 如果都不是则不是一个JSON格式的文本 
     * @param str String
     * @return 0.不是json对象；1.map对象；2.数组对象  
     */
    public static int getJSONType(String str) {
        if (StringUtils.isEmpty(str)) {
            return 0;
        }

        final char[] strChar = str.substring(0, 1).toCharArray();
        final char firstChar = strChar[0];

        LOGGER.info("getJSONType", " firstChar = " + firstChar);

        if (firstChar == '{') {
            return 1;
        } else if (firstChar == '[') {
            return 2;
        } else {
            return 0;
        }
    }

    public static Object assembleTwoModel(Object oldModel, Object newModel) {
        try {
            @SuppressWarnings("unchecked")
            Map<String, Object> oldMap = JSON.parseObject((String) oldModel, HashMap.class);
            @SuppressWarnings("unchecked")
            Map<String, Object> newMap = JSON.parseObject((String) newModel, HashMap.class);
            for (String key : oldMap.keySet()) {
                if (!newMap.containsKey(key)) {
                    newMap.put(key, oldMap.get(key));
                }
            }
            newModel = JSON.toJSONString(newMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return newModel;
    }
}
