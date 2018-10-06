package com.yd1994.alpacablog.common.exception;

/**
 * 文件上传出错异常
 *
 * @author yd
 */
public class UploadErrorException extends RuntimeException {

    public UploadErrorException() {
    }

    public UploadErrorException(String message) {
        super(message);
    }
}
