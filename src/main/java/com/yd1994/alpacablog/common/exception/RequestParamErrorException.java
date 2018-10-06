package com.yd1994.alpacablog.common.exception;

/**
 * request 上传的参数格式错位、参数错位、有误异常
 *
 * @author yd
 */
public class RequestParamErrorException extends RuntimeException {

    public RequestParamErrorException() {
    }

    public RequestParamErrorException(String message) {
        super(message);
    }

}
