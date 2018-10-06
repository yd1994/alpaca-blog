package com.yd1994.alpacablog.service.impl;

import com.yd1994.alpacablog.common.exception.ResourceNotFoundException;
import com.yd1994.alpacablog.dto.FileInfo;
import com.yd1994.alpacablog.entity.FileDO;
import com.yd1994.alpacablog.repository.FileRepository;
import com.yd1994.alpacablog.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 *
 * @author yd
 */
@Service
public class FileServiceImpl implements FileService {

    @Autowired
    private FileRepository fileRepository;

    @Override
    public FileInfo get(Long id) {
        FileDO fileDO = this.fileRepository.findFirstByIdAndDelete(id, false);
        if (fileDO == null) {
            throw new ResourceNotFoundException("该文件不存在。");
        }
        return new FileInfo(fileDO);
    }

    @Override
    public FileInfo get(Long id, Integer type) {
        FileDO fileDO = this.fileRepository.findFirstByIdAndTypeAndDelete(id, type, false);
        if (fileDO == null) {
            throw new ResourceNotFoundException("该文件不存在。");
        }
        return new FileInfo(fileDO);
    }

    @Override
    public FileInfo add(FileInfo fileInfo) {
        FileDO fileDO = fileInfo.toEntity();
        fileDO.setId(null);
        fileDO.setDelete(false);
        Date date = new Date();
        fileDO.setGmtCreated(date);
        return new FileInfo(this.fileRepository.saveAndFlush(fileDO));
    }

}
