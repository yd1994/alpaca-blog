package com.yd1994.alpacablog.common.exception;

/**
 * 数据库的表中 乐观锁：version 为null异常
 *
 * @author yd
 */
public class TableVersionNotFoundException extends NullPointerException {

    private static final String msg = "数据库的表中 乐观锁：version 为null";

    public TableVersionNotFoundException() {
        super(msg);
    }

    public TableVersionNotFoundException(String s) {
        super(s);
    }

}
