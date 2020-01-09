package com.fw.common.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

public final class ImageUtils {

    public static String choiceImgUrl(String spec, String srcUrl) {
        if (StringUtils.isEmpty(srcUrl)) {
            return "";
        }
        if (!isImg(srcUrl)) {
            return "";
        }
        if (!isImgUrl(srcUrl)) {
            return "";
        }
        if (srcUrl.contains(".")) {
            srcUrl = srcUrl.substring(0, srcUrl.lastIndexOf(".")) + "_" + spec.replace("X", "x") + srcUrl.substring(srcUrl.lastIndexOf("."));
        }
        return srcUrl;
    }
    
    public static boolean isImg(String img){
        Pattern pattern = Pattern.compile("\\.(jpg)|(png)|(gif)|(bmp)|(GIF)|(JPG)|(PNG)|(JPEG)");
        Matcher matcher = pattern.matcher(img);
        return matcher.find();
    }
    
    public static boolean isImgUrl(String img){
        if (img.length() < 40 || !img.startsWith("group")) {
            return false;
        }
        return true;
    }
}
