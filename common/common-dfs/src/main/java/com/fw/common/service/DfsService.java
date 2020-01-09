/*
 * Copyright (C), 2013-2013, 汽车街电商有限公司
 * FileName: DfsService.java
 * Author: wei.liao
 * Date: 2015年2月3日 上午10:23:38
 * History: 
 * <author>    <time>      <version>   <desc>
 *  修改人姓名    修改时间               版本号               描述
 */
package com.fw.common.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * dfs文件操作封装接口
 * 
 * @author 廖维
 */
public interface DfsService {

    /**
     * 以zip包批量上传文件
     * 
     * @param zipContent
     *            zip包内容
     * @param originalFilename
     *            zip包文件名
     * @return 上传完成后，所有文件原名（key）和dfs返回的fileId（value）组成的map
     * @throws IOException 异常
     * @since 1.0
     */
    Map<String, String> uploadZip(byte[] zipContent, String originalFilename) throws IOException;

    /**
     * 批量删除dfs服务器上的文件
     * 
     * @param fileIdList List
     * @throws IOException 异常
     * @since 1.0
     */
    void dropFile(List<String> fileIdList) throws IOException;
    
    /**
     * 删除dfs服务器上的文件
     * 
     * @param fileId String
     * @throws IOException 异常
     * @since 1.0
     */
    void dropFile(String fileId) throws IOException;

    /**
     * 上传文件
     * 
     * @param fileContent
     *            文件内容
     * @param fileName
     *            文件名
     * @return String
     * @throws IOException 异常
     * @since 1.0
     */
    String uploadFile(byte[] fileContent, String fileName) throws IOException;

    /**
     * 批量上传文件
     * 
     * @param fileContentMap
     *            文件名（key）和文件内容（value）组成的map
     * @return Map
     * @throws IOException 异常
     * @since 1.0
     */
    Map<String, String> uploadFiles(Map<String, byte[]> fileContentMap) throws IOException;

    /**
     * 批量下载文件
     * 
     * @param fileIdList List
     * @return Map
     * @throws IOException 异常
     * @since 1.0
     */
    Map<String, byte[]> downloadFile(List<String> fileIdList) throws IOException;
}