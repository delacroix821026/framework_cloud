package com.fw.common.service;

import java.io.File;

import org.csource.common.NameValuePair;
import org.csource.fastdfs.FileInfo;
import org.csource.fastdfs.TrackerServer;

public interface FastDfsService {

    /**
     * 取得trackerServer
     *
     * @return TrackerServer
     */
    public TrackerServer getTrackerServer();

    /**
     * 上传文件
     *
     * @param data          本地文件内容
     * @param extension     文件扩展名
     * @param trackerServer TrackerServer
     * @return 文件系统生成的文件ID
     */
    String upload(byte[] data, String extension, TrackerServer trackerServer);

    /**
     * 上传文件
     *
     * @param pathname      本地文件路径
     * @param trackerServer TrackerServer
     * @return 文件系统生成的文件ID
     */
    String upload(String pathname, TrackerServer trackerServer);

    /**
     * 上传文件
     *
     * @param file          本地文件
     * @param trackerServer TrackerServer
     * @return 文件系统生成的文件ID
     */
    String upload(File file, TrackerServer trackerServer);

    /**
     * 下载文件
     *
     * @param fileId        文件ID
     * @param trackerServer TrackerServer
     * @return 文件内容
     */
    byte[] download(String fileId, TrackerServer trackerServer);

    /**
     * 下载文件
     *
     * @param fileId        文件ID
     * @param pathname      本地文件路径
     * @param trackerServer TrackerServer
     */
    void download(String fileId, String pathname, TrackerServer trackerServer);

    /**
     * 下载文件
     *
     * @param fileId        文件ID
     * @param file          本地文件
     * @param trackerServer TrackerServer
     */
    void download(String fileId, File file, TrackerServer trackerServer);

    /**
     * 删除文件
     *
     * @param fileId        文件ID
     * @param trackerServer TrackerServer
     */
    void remove(String fileId, TrackerServer trackerServer);

    /**
     * 获取文件信息
     *
     * @param fileId        文件ID
     * @param trackerServer TrackerServer
     * @return 文件信息
     */
    FileInfo getFileInfo(String fileId, TrackerServer trackerServer);

    /**
     * 获取文件元数据
     *
     * @param fileId        文件ID
     * @param trackerServer TrackerServer
     * @return 文件元数据
     */
    NameValuePair[] getMetaData(String fileId, TrackerServer trackerServer);

    /**
     * FastDFS client.upload_file1(...) 含文件属性
     *
     * @param file_buff file content/buff
     * @param extension file ext name , do not include dot(.)
     * @param meta_list NameValuePair[]
     * @return String file id(including group name and filename) if success,
     */
    String upload1(byte[] file_buff, String extension, NameValuePair[] meta_list);

    /**
     * FastDFS client.upload_file1(...) 不含文件属性
     *
     * @param file_buff file content/buff
     * @param extension file ext name , do not include dot(.)
     * @return String file id(including group name and filename) if success,
     */
    String upload1(byte[] file_buff, String extension);
}
