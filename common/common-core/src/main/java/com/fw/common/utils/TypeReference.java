package com.fw.common.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public abstract class TypeReference<T> {
    private final Type type;
    private volatile Constructor<?> constructor;

    protected TypeReference() {
        Type superclass = getClass().getGenericSuperclass();
        if (superclass instanceof Class) {
            throw new RuntimeException("Missing type parameter.");
        }
        this.type = ((ParameterizedType) superclass).getActualTypeArguments()[0];
    }

    /**
     * Instantiates a new instance of {@code T} using the default, no-arg
     * constructor.
     * @return T f
     * @throws NoSuchMethodException 异常
     * @throws IllegalAccessException 异常
     * @throws InvocationTargetException 异常
     * @throws InstantiationException 异常
     */
    @SuppressWarnings("unchecked")
    public T newInstance()
            throws NoSuchMethodException, IllegalAccessException,
            InvocationTargetException, InstantiationException {
        if (constructor == null) {
            Class<?> rawType = type instanceof Class<?>
                    ? (Class<?>) type
                    : (Class<?>) ((ParameterizedType) type).getRawType();
            constructor = rawType.getConstructor();
        }
        return (T) constructor.newInstance();
    }

    /**
     * Gets the referenced type.
     * @return Type
     */
    public Type getType() {
        return this.type;
    }

    public static void main(String[] args) throws Exception {
        //List<String> l1 = new TypeReference<ArrayList<String>>() {}.newInstance();
        //List l2 = new TypeReference<ArrayList>() {}.newInstance();
        PageModel<ReflectUtils> l1 = new TypeReference<PageModel<ReflectUtils>>() {
        }.newInstance();
        l1.getModel();
    }
}
