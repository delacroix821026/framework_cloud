package com.fw.common.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

public final class EntityUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(EntityUtils.class);

    /*public static List<String> convertEntityToImgUrl(List<? extends Object> list, String fieldName, String spec) {
        if (list == null) {
            return null;
        }
        List<String> fieldList = new ArrayList<String>();
        try {
            for (Object object : list) {
                Class<? extends Object> clazz = object.getClass();
                Method method = clazz.getMethod("get" + fieldName.replaceFirst(fieldName.substring(0, 1), fieldName.substring(0, 1).toUpperCase()), new Class[0]);
                String url = (String) method.invoke(object, new Object[0]);
                if (StringUtils.isNotEmpty(url) && !url.toLowerCase().endsWith("xps") && !url.toLowerCase().endsWith("pdf"))
                fieldList.add(ImageUtils.choiceImgUrl(spec, url));
            }
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            LOGGER.error(e.getMessage());
        }
        return fieldList;
    }*/

    public static List<? extends Object> convertEntityVo(List<? extends Object> list, Class<? extends Object> clazz) {
        if (list == null) {
            return null;
        }
        List<Object> voList = new ArrayList<Object>();
        try {
            for (Object object : list) {
                Object vo = clazz.newInstance();
                BeanUtils.copyProperties(object, vo);
                Method[] methods = clazz.getMethods();
                for (Method method : methods) {
                    if (method.getName().equals("callback")) {
                        method.invoke(vo, new Object[] { object });
                        break;
                    }
                }
                voList.add(vo);
            }
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            LOGGER.error("entity exception", e);
        }
        return voList;
    }

    public static List<String> convertEntityToString(List<? extends Object> list, String fieldName) {
        if (list == null) {
            return null;
        }
        List<String> fieldList = new ArrayList<String>();
        try {
            for (Object object : list) {
                Class<? extends Object> clazz = object.getClass();
                Method method = clazz.getMethod("get" + fieldName.replaceFirst(fieldName.substring(0, 1), fieldName.substring(0, 1).toUpperCase()), new Class[0]);
                fieldList.add((String) method.invoke(object, new Object[0]));
            }
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            LOGGER.error("entity exception", e);
        }
        return fieldList;
    }

    public static Object containEntity(List<? extends Object> list, String fieldName, Object value) {
        if (list == null) {
            return null;
        }
        try {
            for (Object object : list) {
                Class<? extends Object> clazz = object.getClass();
                Method method = clazz.getMethod("get" + fieldName.replaceFirst(fieldName.substring(0, 1), fieldName.substring(0, 1).toUpperCase()), new Class[0]);
                if (method.invoke(object, new Object[0]).equals(value)) {
                    return object;
                }
            }
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            LOGGER.error("entity exception", e);
        }
        return null;
    }

    public static boolean validEntityIsEmpty(Object obj, String... fieldNames) {
        if (obj == null) {
            LOGGER.error("valid object is null");
            return false;
        }
        if (fieldNames == null) {
            LOGGER.error("valid fieldNames is empty");
            return false;
        }
        try {
            Class<? extends Object> clazz = obj.getClass();
            for (String fieldName : fieldNames) {
                Method method = clazz.getMethod("get" + fieldName.replaceFirst(fieldName.substring(0, 1), fieldName.substring(0, 1).toUpperCase()), new Class[0]);
                Object t = method.invoke(obj, new Object[0]);
                if ((t instanceof String)) {
                    if (StringUtils.isBlank((String) t)) {
                        LOGGER.error("valid fieldName: " + fieldName + " is empty");
                        return false;
                    }
                } else if (t == null) {
                    LOGGER.error("valid fieldName: " + fieldName + " is empty");
                    return false;
                }
            }
        } catch (IllegalAccessException | InvocationTargetException | IllegalArgumentException | NoSuchMethodException | SecurityException e) {
            LOGGER.error("entity exception", e);
        }
        return true;
    }

    public static Map<Object, Object> convertToMapper(List<? extends Object> list, String keyFieldName, String valueFieldName) {
        if (list == null) {
            return null;
        }
        Map<Object, Object> map = new HashMap<Object, Object>();
        try {
            for (Object object : list) {
                Class<? extends Object> clazz = object.getClass();
                Method method = clazz.getMethod("get" + keyFieldName, new Class[0]);
                Method method2 = clazz.getMethod("get" + valueFieldName, new Class[0]);
                map.put(method.invoke(object, new Object[0]), method2.invoke(object, new Object[0]));
            }
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            LOGGER.error("entity exception", e);
        }
        return map;
    }

    public static Map<String, Object> convertToMap(Object obj, String... fieldNames) {
        Map<String, Object> result = new HashMap<String, Object>();
        if (obj == null) {
            LOGGER.error("valid object is null");
            return result;
        }
        if (fieldNames == null) {
            LOGGER.error("valid fieldNames is empty");
            return result;
        }
        try {
            Class<? extends Object> clazz = obj.getClass();
            for (String fieldName : fieldNames) {
                Method method = clazz.getMethod("get" + fieldName.replaceFirst(fieldName.substring(0, 1), fieldName.substring(0, 1).toUpperCase()), new Class[0]);
                Object t = method.invoke(obj, new Object[0]);
                if (t != null) {
                    result.put(fieldName, t);
                }
            }
        } catch (IllegalAccessException | InvocationTargetException | IllegalArgumentException | NoSuchMethodException | SecurityException e) {
            LOGGER.error("entity exception", e);
        }
        return result;
    }

    public static List<Map<String, Object>> convertToListMap(List<? extends Object> list, String... fieldNames) {
        if ((list == null) || (list.size() == 0)) {
            LOGGER.error("valid object is null");
            return new ArrayList<Map<String, Object>>();
        }
        List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
        for (Object obj : list) {
            resultList.add(convertToMap(obj, fieldNames));
        }
        return resultList;
    }
}
