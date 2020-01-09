package com.fw.common.controller;

import com.fw.common.exception.DfsException;
import com.fw.common.service.DfsService;
import com.fw.common.service.FastDfsService;
import lombok.extern.slf4j.Slf4j;
import org.csource.fastdfs.TrackerServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
//@RestController
public class DfsController {
    @Autowired
    private DfsService dfsService;

    @Autowired
    private FastDfsService fastDfsService;

    //@RequestMapping(value = "/uploadZip", method = RequestMethod.POST)
    public Map<String, String> uploadZip(byte[] zipContent, String originalFilename) {
        log.info("multipartFile upload Enter!");
        try {
            return dfsService.uploadZip(zipContent, originalFilename);
        } catch (IOException e) {
            log.info("multipartFile upload Zip error!");
            //Todo
            throw new DfsException("文件{0}上传失败！", originalFilename);
        }
    }

    //@RequestMapping(value = "/uploadBytes", method = RequestMethod.POST)
    public String uploadFile(byte[] fileContent, String fileName) throws IOException {
        log.debug("上传照片");
        String extName = fileName.substring(fileName.lastIndexOf(".") + 1);
        TrackerServer trackerServer = null;
        try {
            trackerServer = fastDfsService.getTrackerServer();
            return fastDfsService.upload(fileContent, extName, trackerServer);
        } catch (Exception e) {

        } finally {
            if(trackerServer != null)
                trackerServer.close();
        }
        return  "";
    }

    //@RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
    public String uploadFile(@RequestParam("file") MultipartFile multipartFile, @RequestParam("fileName") String fileName) {
        log.info("multipartFile upload Enter!");
        try {
            return dfsService.uploadFile(multipartFile.getBytes(), fileName);
        } catch (IOException e) {
            log.info("multipartFile upload error!");
            //Todo
            throw new DfsException("文件{0}上传失败！", fileName);
        }
    }

    //@RequestMapping(value = "/uploadFiles", method = RequestMethod.POST)
    public Map<String, String> uploadFiles(@RequestParam("files") MultipartFile[] multipartFiles, @RequestParam("fileNames") String[] fileNames) {
        log.info("multipartFiles upload Enter!");
        Map<String, byte[]> fileContentMap = new HashMap<>();
        try {
            for (int i = 0; i < fileNames.length; i++) {
                fileContentMap.put(fileNames[i], multipartFiles[i].getBytes());
            }
            return dfsService.uploadFiles(fileContentMap);
        } catch (IOException e) {
            log.info("multipartFile upload error!");
            //Todo
            throw new DfsException("文件上传失败！");
        }
    }

    //@RequestMapping(value = "/downloadFiles", method = RequestMethod.POST)
    public Map<String, byte[]> downloadFiles(@RequestParam("fileNames") List<String> fileIdList) {
        log.info("multipartFiles download Enter! fileIdList size:" + fileIdList == null ? 0 : fileIdList.size());

        try {
            return dfsService.downloadFile(fileIdList);
        } catch (IOException e) {
            log.info("multipartFile download error!");
            throw new DfsException("文件下载失败！");
        }
    }

    //@RequestMapping(value = "/dropFile", method = RequestMethod.POST)
    public void dropFile(@RequestParam("fileName") String fileName) {
        log.info("multipartFiles download Enter! fileName:" + fileName);

        try {
            dfsService.dropFile(fileName);
        } catch (IOException e) {
            log.info("multipartFile dorp error!");
            throw new DfsException("文件删除失败！");
        }
    }

    //@RequestMapping(value = "/dropFiles", method = RequestMethod.POST)
    public void dropFiles(@RequestParam("fileNames") List<String> fileNames) {
        log.info("multipartFiles download Enter! fileNames size:" + fileNames == null ? 0 : fileNames.size());

        try {
            dfsService.dropFile(fileNames);
        } catch (IOException e) {
            log.info("multipartFile dorp error!");
            throw new DfsException("文件删除失败！");
        }
    }
}
