/*
 * Copyright (C), 2013-2013, 汽车街电商有限公司
 * FileName: FileUtil.java
 * Author: wei.liao
 * Date: 2015年1月5日 上午10:19:35
 * History: 
 * <author>    <time>      <version>   <desc>
 *  修改人姓名    修改时间               版本号               描述
 */
package com.fw.common.utils;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * 文件操作工具
 *
 * @author 廖维
 */
public final class FileUtil {

    private static Logger LOGGER = LoggerFactory.getLogger(FileUtil.class);

    /**
     * 文件空验证
     *
     * @param file CommonsMultipartFile
     * @return boolean
     */
    public static boolean isNullOrEmpty(CommonsMultipartFile file) {
        return file == null || file.isEmpty() || file.getSize() == 0;
    }

    /**
     * 文件非空验证
     *
     * @param file CommonsMultipartFile
     * @return boolean
     */
    public static boolean isNotNullOrEmpty(CommonsMultipartFile file) {
        return !isNullOrEmpty(file);
    }

    /**
     * 保存文件
     *
     * @param fileContent 文件内容
     * @param filePath    文件目录路径
     * @param filename    文件名
     * @throws IOException 异常
     * @since 1.0
     */
    public static void saveFile(byte[] fileContent, String filePath, String filename) throws IOException {

        // 删除已存在的同名文件
        File file = new File(filePath + filename);
        if (file.exists()) {
            file.delete();
        }

        // 保存文件
        FileOutputStream fos = new FileOutputStream(filePath + filename);
        fos.write(fileContent, 0, fileContent.length);
        fos.flush();
        fos.close();
    }

    /**
     * 保存文件
     *
     * @param fileContent 文件内容
     * @param file        文件对象
     * @throws IOException 异常
     * @since 1.0
     */
    public static void saveFile(byte[] fileContent, File file) throws IOException {

        // 保存文件
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(fileContent, 0, fileContent.length);
        fos.flush();
        fos.close();
    }

    /**
     * 删除文件
     *
     * @param filePath 文件目录路径
     * @param filename 文件名
     * @since 1.0
     */
    public static void deleteFile(String filePath, String filename) {
        File file = new File(filePath + filename);
        if (file.exists()) {
            file.delete();
        }
    }

    /**
     * 创建目录
     *
     * @param path 目录路径
     * @since 1.0
     */
    public static void mkDir(String path) {

        // 删除已存在的同名目录
        delDir(path);

        // 创建目录
        File file = new File(path);
        file.mkdirs();
    }

    /**
     * 删除目录及其下的文件和子目录
     *
     * @param path 目录路径
     * @since 1.0
     */
    public static void delDir(String path) {

        File file = new File(path);
        if (file.exists() && file.isDirectory()) {
            File[] delFiles = file.listFiles();
            for (File delFile : delFiles) {
                if (delFile.isDirectory()) {
                    delDir(delFile.getAbsolutePath());
                } else {
                    delFile.delete();
                }
            }
        }
        file.delete();
    }

    public static byte[] zip(Map<String, byte[]> photoMap) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ZipOutputStream zipOut = new ZipOutputStream(out);

        byte[] fileContent;
        ByteArrayInputStream in;
        BufferedInputStream bi;
        int b;
        Set<String> keySet = photoMap.keySet();
        for (String fileName : keySet) {
            fileContent = photoMap.get(fileName);
            zipOut.putNextEntry(new ZipEntry(fileName));
            in = new ByteArrayInputStream(fileContent);
            bi = new BufferedInputStream(in);
            while ((b = bi.read()) != -1) {
                zipOut.write(b); // 将字节流写入当前zip目录
            }
            bi.close();
            in.close(); // 输入流关闭
        }

        zipOut.close();
        out.close();
        return out.toByteArray();
    }

    /**
     * 解压zip文件
     *
     * @param filePath 文件目录路径
     * @param filename 文件名
     * @return 解压后目录路径 即zip文件同文件夹下新增目录，目录名与zip文件相同去掉.zip后缀
     * @throws IOException 异常
     * @since 1.0
     */
    public static String unZip(String filePath, String filename) throws IOException {

        FileInputStream fis = new FileInputStream(filePath + filename);
        ZipInputStream zis = new ZipInputStream(new BufferedInputStream(fis));

        String path = filePath + filename.substring(0, filename.lastIndexOf(".zip"));
        mkDir(path);
        path = path + File.separator;

        ZipEntry entry;
        int buffer = 2048;
        byte[] data;
        String temp;
        int index;
        File file;
        FileOutputStream fos;
        BufferedOutputStream bos;
        int count;
        while ((entry = zis.getNextEntry()) != null) {
            data = new byte[buffer];

            temp = entry.getName();

            index = temp.lastIndexOf(File.separator);
            if (index > -1) {
                temp = temp.substring(index + 1);
            }
            temp = path + temp;

            file = new File(temp);
            file.createNewFile();

            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos, buffer);

            while ((count = zis.read(data, 0, buffer)) != -1) {
                bos.write(data, 0, count);
            }
            bos.flush();
            bos.close();
        }

        zis.close();

        return path;
    }

    /**
     * 读取文件
     *
     * @param filePath 文件路径
     * @return 文件内容字节数组
     * @throws IOException 异常
     * @since 1.0
     */
    public static byte[] readFile(String filePath) throws IOException {

        // 读取文件
        File tempfile = new File(filePath);
        FileInputStream fis = new FileInputStream(tempfile);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] b = new byte[1024];
        int n;
        while ((n = fis.read(b)) != -1) {
            out.write(b, 0, n);
        }
        fis.close();
        byte[] fileContent = out.toByteArray();
        return fileContent;
    }

    public static List<File> getDirectoryFiles(String filePath) {
        File directory = new File(filePath);
        return getDirectoryFiles(directory);
    }

    private static List<File> getDirectoryFiles(File directory) {
        List<File> filesList = new ArrayList<File>();
        if (directory.isDirectory()) {
            File[] files = directory.listFiles();
            for (File file : files) {
                if (file.isFile()) {
                    filesList.add(file);
                } else if (file.isDirectory()) {
                    filesList.addAll(getDirectoryFiles(file));
                }
            }
        }
        return filesList;
    }

    public static boolean isImage(CommonsMultipartFile file) {
        boolean flag = false;
        try {
            ImageInputStream is = ImageIO.createImageInputStream(file);
            if (null == is) {
                return flag;
            }
            is.close();
            flag = true;
        } catch (Exception e) {
            LOGGER.error("图片验证失败", e);
        }
        return flag;
    }

    public static byte[] uploadPic(String imgData) {
        if (StringUtils.isEmpty(imgData)) {
            return null;
        }
        return Base64.decodeBase64(imgData.getBytes());
    }

    /**
     * 图片添加水印
     *
     * @param srcImgPath       需要添加水印的图片的路径
     * @param outImgPath       添加水印后图片输出路径
     * @param markContentColor 水印文字的颜色
     * @param waterMarkContent 水印的文字
     */
    public void mark(String srcImgPath, String outImgPath, Color markContentColor, String waterMarkContent) {
        try {
            // 读取原图片信息  
            File srcImgFile = new File(srcImgPath);
            Image srcImg = ImageIO.read(srcImgFile);
            int srcImgWidth = srcImg.getWidth(null);
            int srcImgHeight = srcImg.getHeight(null);
            // 加水印  
            BufferedImage bufImg = new BufferedImage(srcImgWidth, srcImgHeight, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = bufImg.createGraphics();
            g.drawImage(srcImg, 0, 0, srcImgWidth, srcImgHeight, null);
            //Font font = new Font("Courier New", Font.PLAIN, 12);  
            Font font = new Font("宋体", Font.PLAIN, 50);
            g.setColor(markContentColor); //根据图片的背景设置水印颜色  

            g.setFont(font);
            int x = srcImgWidth - getWatermarkLength(waterMarkContent, g) - 3;
            int y = srcImgHeight - 3;
            //int x = (srcImgWidth - getWatermarkLength(watermarkStr, g)) / 2;  
            //int y = srcImgHeight / 2;  
            g.drawString(waterMarkContent, x, y);
            g.dispose();
            // 输出图片  
            FileOutputStream outImgStream = new FileOutputStream(outImgPath);
            ImageIO.write(bufImg, "jpg", outImgStream);
            outImgStream.flush();
            outImgStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取水印文字总长度
     *
     * @param waterMarkContent 水印的文字
     * @param g Graphics2D
     * @return 水印文字总长度
     */
    public int getWatermarkLength(String waterMarkContent, Graphics2D g) {
        return g.getFontMetrics(g.getFont()).charsWidth(waterMarkContent.toCharArray(), 0, waterMarkContent.length());
    }

    // 根据str,font的样式以及输出文件目录  
    public static void createImage(String str, Font font, File outFile,
                                   Integer width, Integer height) throws Exception {
        // 创建图片  
        BufferedImage image = new BufferedImage(width, height,
                BufferedImage.TYPE_INT_BGR);
        Graphics g = image.getGraphics();
        g.setClip(0, 0, width, height);
        g.setColor(Color.black);
        g.fillRect(0, 0, width, height);// 先用黑色填充整张图片,也就是背景  
        g.setColor(Color.red);// 在换成黑色  
        g.setFont(font);// 设置画笔字体  
        /** 用于获得垂直居中y */
        Rectangle clip = g.getClipBounds();
        FontMetrics fm = g.getFontMetrics(font);
        int ascent = fm.getAscent();
        int descent = fm.getDescent();
        int y = (clip.height - (ascent + descent)) / 2 + ascent;
        for (int i = 0; i < 6; i++) {// 256 340 0 680  
            g.drawString(str, i * 680, y);// 画出字符串  
        }
        g.dispose();
        ImageIO.write(image, "png", outFile);// 输出png图片  
    }

    // 创建目录
    public static boolean createDir(String destDirName) {
        File dir = new File(destDirName);
        if (dir.exists()) {// 判断目录是否存在
            return true;
        }
        if (!destDirName.endsWith(File.separator)) {// 结尾是否以"/"结束
            destDirName = destDirName + File.separator;
        }
        if (dir.mkdirs()) {// 创建目标目录
            return true;
        } else {
            LOGGER.error("创建目录失败");
            return false;
        }
    }

    public static void createFile(byte[] bfile, String filePath, String fileName) {
        File file = null;
        File dir = new File(filePath);
        if (!dir.exists() && dir.isDirectory()) {//判断文件目录是否存在
            dir.mkdirs();
        }
        file = new File(filePath + "\\" + fileName);
        try (FileOutputStream fos = new FileOutputStream(file);
             BufferedOutputStream bos = new BufferedOutputStream(fos)) {
            bos.write(bfile);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    public static byte[] File2byte(String filePath) {
        byte[] buffer = null;
        try {
            File file = new File(filePath);
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] b = new byte[1024];
            int n;
            while ((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            fis.close();
            bos.close();
            buffer = bos.toByteArray();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buffer;
    }

    public static File byte2File(byte[] buf, String filePath, String fileName) {
        BufferedOutputStream bos = null;
        FileOutputStream fos = null;
        File file = null;
        try {
            File dir = new File(filePath);
            if (!dir.exists() && dir.isDirectory()) {
                dir.mkdirs();
            }
            file = new File(filePath + File.separator + fileName);
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            bos.write(buf);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return file;
    }

    /**
     * 验证文件后缀
     *
     * @param fileName String
     * @param suffixArr ["pdf","jpg","png"]
     * @return boolean
     */
    public static boolean checkFileSuffix(String fileName, String[] suffixArr) {
        for (int i = 0; i < suffixArr.length; i++) {
            if (fileName.indexOf(".") != -1) {
                String suf = fileName.substring(fileName.lastIndexOf(".") + 1);
                if (suf.equalsIgnoreCase(suffixArr[i])) {
                    return true;
                }
            }
        }
        return false;
    }

    public static void main(String args[]) {
        boolean b = FileUtil.checkFileSuffix("123123.pdf", new String[]{"pdf", "jpg", "png"});
        System.out.println(b);
    }
}