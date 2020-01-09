package com.fw.oauth.client.utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

public class ReflectAnnotation {
    public static Map getAnnotationMap(Annotation annotation) {
        if (annotation != null) {
            InvocationHandler invo = Proxy.getInvocationHandler(annotation); //获取被代理的对象
            Map map = (Map) getFieldValue(invo, "memberValues");
            if (map != null) {
                Map result = new HashMap();
                for (Method method : annotation.annotationType().getDeclaredMethods()) {
                    Object obj = map.get(method.getName());

                    System.out.println(method.getName());
                    if (obj != null)
                        result.put(method.getName(), obj);
                }
                return result;
                //return map;
            }
        }
        return new HashMap();
    }

    public static Object getAnnotationValue(Annotation annotation, String property) {
        if("defaultValue".equals(property))
            return "";
        return getAnnotationMap(annotation).get(property);
    }

    public static <T> Object getFieldValue(T object, String property) {
        if (object != null && property != null) {
            Class<T> currClass = (Class<T>) object.getClass();

            try {
                Field field = currClass.getDeclaredField(property);
                field.setAccessible(true);
                return field.get(object);
            } catch (NoSuchFieldException e) {
                throw new IllegalArgumentException(currClass + " has no property: " + property);
            } catch (IllegalArgumentException e) {
                throw e;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static void main(String... arg) {

    }
}
