/*
 * Copyright (C), 2014-2015, 上海澍勋电子商务有限公司
 * FileName: SerializableUtils.java
 * Author:   long.yu
 * Date:     2015年12月2日 下午6:25:27
 * Description: //模块目的、功能描述      
 * History: //修改记录
 * <author>      <time>      <version>    <desc>
 * 修改人姓名             修改时间            版本号                  描述
 */
package com.fw.common.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * 序列化反序列化工具
 *
 * @author long.yu
 * @since [产品/模块版本] （可选）
 */
public class SerializableUtils {

    /**
     * 序列化对象为字节数组
     *
     * @param obj Object
     * @return byte[]
     * @throws IOException 异常
     * @since [产品/模块版本](可选)
     */
    public static byte[] serialize(Object obj) throws IOException {
        if (obj == null) {
            return null;
        }
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(bos));) {
            oos.writeObject(obj);
            oos.flush();
            return bos.toByteArray();
        }
    }

    /**
     * 反序列化字节数组为对象
     *
     * @param b   byte[]
     * @param <T> T
     * @return T
     * @throws IOException 异常
     * @throws ClassNotFoundException 异常
     * @since [产品/模块版本](可选)
     */
    @SuppressWarnings("unchecked")
    public static <T> T deserialize(byte[] b) throws IOException, ClassNotFoundException {
        if (b != null) {
            try (BufferedInputStream bis = new BufferedInputStream(new ByteArrayInputStream(b));
                 ObjectInputStream ois = new ObjectInputStream(bis);) {
                return (T) ois.readObject();
            }
        }
        return null;
    }
}
