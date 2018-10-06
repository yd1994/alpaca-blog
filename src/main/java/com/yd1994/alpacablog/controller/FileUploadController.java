package com.yd1994.alpacablog.controller;

import com.yd1994.alpacablog.common.exception.RequestParamErrorException;
import com.yd1994.alpacablog.common.exception.UploadErrorException;
import com.yd1994.alpacablog.common.result.ResultFactory;
import com.yd1994.alpacablog.dto.FileInfo;
import com.yd1994.alpacablog.service.FileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.Random;

@RestController
@RequestMapping("/images")
public class FileUploadController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${system.file.filePath.image}")
    private String filePath;

    @Autowired
    private FileService fileService;

    @Autowired
    private ResourceLoader resourceLoader;

    @GetMapping("/{id}")
    public ResponseEntity get(@PathVariable("id") Long id) {
        FileInfo fileInfo = this.fileService.get(id, 0);
        try {
            return ResponseEntity.ok(resourceLoader.getResource("file:" + fileInfo.getPath()));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResultFactory.Info uploadImg(@RequestParam("image") MultipartFile file, HttpServletRequest request) {
        if (file.isEmpty()) {
            throw new RequestParamErrorException("文件不能为空。");
        }
        String basePath = this.filePath;
        if (StringUtils.isEmpty(basePath)) {
            throw new UploadErrorException("上传失败：目标文件夹地址不存在！");
        }
        if (basePath.lastIndexOf('/') != basePath.length() - 1) {
            basePath += '/';
        }
        try {
            // 图片文件类型
            String contentType = file.getContentType();
            // 图片名字
            String fileName = file.getOriginalFilename();

            String time = String.valueOf(System.currentTimeMillis());
            String randomStr = String.valueOf(new Random().nextInt(1000));

            String path = basePath + time + randomStr + fileName;
            File dest = new File(path);
            // 检测是否存在目录
            if (!dest.getParentFile().exists()){
                // 新建文件夹
                dest.getParentFile().mkdirs();
            }
            file.transferTo(dest);// 文件写入
            FileInfo fileInfo = new FileInfo();
            fileInfo.setName(fileName + "." + contentType);
            fileInfo.setPath(path);
            fileInfo.setType(0);
            fileInfo = this.fileService.add(fileInfo);
            return ResultFactory.get200Info().data(fileInfo.getId()).message("文件上传成功！");
        } catch (IOException e) {
            logger.error(e.toString());
            throw new UploadErrorException("上传失败：文件写入失败！");
        }
    }

}
