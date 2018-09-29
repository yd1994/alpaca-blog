package com.yd1994.alpacablog.common.exception;

/**
 * 异常类：资源不存在
 *
 * @author yd
 */
public class SourceNotFoundException extends NullPointerException {

    public SourceNotFoundException() {
    }

    public SourceNotFoundException(String s) {
        super(s);
    }
}
