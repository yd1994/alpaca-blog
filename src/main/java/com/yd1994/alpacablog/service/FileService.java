package com.yd1994.alpacablog.service;

import com.yd1994.alpacablog.dto.FileInfo;
import com.yd1994.alpacablog.entity.FileDO;

/**
 * 文件信息
 *
 * @author yd
 */
public interface FileService {

    /**
     * 通过id获取
     * @param id
     * @return
     */
    FileInfo get(Long id);

    /**
     * 通过id与type获取
     * @param id
     * @param type 类型
     * @return
     */
    FileInfo get(Long id, Integer type);

    /**
     * 添加文件信息
     * @param fileInfo
     */
    FileInfo add(FileInfo fileInfo);

}
