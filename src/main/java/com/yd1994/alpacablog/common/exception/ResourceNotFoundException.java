package com.yd1994.alpacablog.common.exception;

import java.util.NoSuchElementException;

/**
 * 异常类：资源不存在
 *
 * @author yd
 */
public class ResourceNotFoundException extends NoSuchElementException {

    public ResourceNotFoundException() {
    }

    public ResourceNotFoundException(String s) {
        super(s);
    }
}
