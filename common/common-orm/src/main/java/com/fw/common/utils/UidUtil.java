package com.fw.common.utils;

import java.security.SecureRandom;
import java.util.UUID;

/**
 * 封装各种生成唯一性ID算法的工具类.
 * @author ThinkGem
 * @version 2013-01-15
 */
public class UidUtil {

    private static SecureRandom random = new SecureRandom();
    
    /**
     * 封装JDK自带的UUID, 通过Random数字生成, 中间无-分割.
     * @return String
     */
    public static String uuid() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
    
    /**
     * 使用SecureRandom随机生成Long.
     * @return long
     */
    public static long randomLong() {
        return Math.abs(random.nextLong());
    }

    /**
     * 基于Base62编码的SecureRandom随机生成bytes.
     * @param length int
     * @return String
     */
    public static String randomBase62(int length) {
        byte[] randomBytes = new byte[length];
        random.nextBytes(randomBytes);
        return Encodes.encodeBase62(randomBytes);
    }
    
    public String getNextId() {
        return UidUtil.uuid();
    }

    public static void main(String[] args) {
        System.out.println(UidUtil.uuid());
        System.out.println(UidUtil.uuid().length());
        System.out.println(new UidUtil().getNextId());
        for (int i=0; i<1000; i++){
            System.out.println(UidUtil.randomLong() + "  " + UidUtil.randomBase62(5));
        }
    }

}
