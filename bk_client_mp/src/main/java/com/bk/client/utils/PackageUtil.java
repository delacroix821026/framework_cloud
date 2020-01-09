package com.fw.oauth.client.utils;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class PackageUtil {

    /**
     * 从jar获取某包下所有类
     *
     * @param jarPath jar文件路径
     * @return 类的完整名称
     */
    public static List<String> getClassNameByJar(String jarPath) {
        List<String> myClassName = new ArrayList();
        try {
            JarFile jarFile = new JarFile(jarPath);
            Enumeration<JarEntry> entrys = jarFile.entries();
            while (entrys.hasMoreElements()) {
                JarEntry jarEntry = entrys.nextElement();
                String entryName = jarEntry.getName();
                entryName = entryName.replace("/", ".");
                if (entryName.endsWith(".class")) {
                    myClassName.add(entryName.substring(0, entryName.indexOf(".class")));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return myClassName;
    }

}
