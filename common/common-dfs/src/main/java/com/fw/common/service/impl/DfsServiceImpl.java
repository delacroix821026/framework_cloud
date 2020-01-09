/*
 * Copyright (C), 2013-2013, 汽车街电商有限公司
 * FileName: DfsServiceImpl.java
 * Author: wei.liao
 * Date: 2015年2月3日 上午10:31:32
 * History: 
 * <author>    <time>      <version>   <desc>
 *  修改人姓名    修改时间               版本号               描述
 */
package com.fw.common.service.impl;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.fw.common.service.DfsService;
import com.fw.common.service.FastDfsService;
import org.csource.fastdfs.TrackerServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fw.common.utils.FileUtil;


@Service("dfsService")
public class DfsServiceImpl implements DfsService {

    /**
     * 日志
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(DfsServiceImpl.class);

    @Autowired
    private FastDfsService fastDfsService;

    @Value("${zip.path}")
    private String zipPath;

    @Override
    public Map<String, String> uploadZip(byte[] zipContent, String originalFilename) throws IOException {
        LOGGER.debug("以zip包批量上传文件");

        FileUtil.saveFile(zipContent, zipPath, originalFilename);
        String tempDirPath = FileUtil.unZip(zipPath, originalFilename);
        FileUtil.deleteFile(zipPath, originalFilename);
        List<File> fileList = FileUtil.getDirectoryFiles(tempDirPath);
        if (null == fileList || fileList.isEmpty()) {
            return null;
        }

        TrackerServer trackerServer = fastDfsService.getTrackerServer();
        Map<String, String> map = new HashMap<String, String>();
        for (File file : fileList) {
            map.put(file.getName(), fastDfsService.upload(file, trackerServer));
        }
        trackerServer.close();

        FileUtil.delDir(zipPath + originalFilename.substring(0, originalFilename.lastIndexOf(".zip")));

        return map;
    }

    @Override
    public void dropFile(List<String> fileIdList) throws IOException {
        LOGGER.debug("批量删除dfs服务器上的文件");
        TrackerServer trackerServer = fastDfsService.getTrackerServer();
        for (String fileId : fileIdList) {
            fastDfsService.remove(fileId, trackerServer);
        }
        trackerServer.close();
    }
    
    @Override
    public void dropFile(String fileId) throws IOException {
        LOGGER.debug("批量删除dfs服务器上的文件");
        TrackerServer trackerServer = fastDfsService.getTrackerServer();
        fastDfsService.remove(fileId, trackerServer);
        trackerServer.close();
    }

    @Override
    public String uploadFile(byte[] fileContent, String fileName) throws IOException {
        LOGGER.debug("上传照片");
        String extName = fileName.substring(fileName.lastIndexOf(".") + 1);
        TrackerServer trackerServer = fastDfsService.getTrackerServer();
        String fileId = fastDfsService.upload(fileContent, extName, trackerServer);
        trackerServer.close();
        return fileId;
    }

    @Override
    public Map<String, String> uploadFiles(Map<String, byte[]> fileContentMap) throws IOException {
        LOGGER.debug("批量上传照片");
        Map<String, String> map = new HashMap<String, String>();
        String extName;
        String fileId;

        TrackerServer trackerServer = fastDfsService.getTrackerServer();
        Set<String> fileNames = fileContentMap.keySet();
        for (String fileName : fileNames) {
            extName = fileName.substring(fileName.lastIndexOf(".") + 1);
            fileId = fastDfsService.upload(fileContentMap.get(fileName), extName, trackerServer);
            map.put(fileName, fileId);
        }
        trackerServer.close();
        return map;
    }

    @Override
    public Map<String, byte[]> downloadFile(List<String> fileIdList) throws IOException {
        LOGGER.debug("下载照片");
        byte[] fileContent;
        Map<String, byte[]> fileContentMap = new HashMap<String, byte[]>();

        TrackerServer trackerServer = fastDfsService.getTrackerServer();
        for (String fileId : fileIdList) {
            fileContent = fastDfsService.download(fileId, trackerServer);
            fileContentMap.put(fileId, fileContent);
        }
        trackerServer.close();

        return fileContentMap;
    }
}