package com.yd1994.alpacablog.common.exception;

/**
 * 乐观锁：version为null异常
 *
 * @author yd
 */
public class VersionNotFoundException extends NullPointerException {

    public VersionNotFoundException() {
        this("version不能为空。");
    }

    public VersionNotFoundException(String s) {
        super(s);
    }
}
