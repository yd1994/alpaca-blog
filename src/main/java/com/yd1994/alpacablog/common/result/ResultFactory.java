package com.yd1994.alpacablog.common.result;

import java.io.Serializable;

/**
 * 简单的信息回馈
 * @author yd
 */
public class ResultFactory implements Serializable {

    /**
     * 成功
     */
    private final static String SUCCESS = "success";

    /**
     * 失败
     */
    private final static String ERROR = "error";

    /**
     * 成功时返回信息
     */
    public final static Info SIMPLE_SUCCESS_INFO = new SimpleResultInfo(SUCCESS);

    /**
     * 失败时返回信息
     */
    public final static Info SIMPLE_ERROR_INFO = new SimpleResultInfo(ERROR);

    public interface Info {
    }

    /**
     * 简单的返回信息
     *
     * @author yd
     */
    private static class SimpleResultInfo implements Info {

        private String info;

        public SimpleResultInfo(String info) {
            this.info = info;
        }

        public String getInfo() {
            return info;
        }

        public void setInfo(String info) {
            this.info = info;
        }

        @Override
        public String toString() {
            return "SimpleRestResult{" +
                    "info='" + info + '\'' +
                    '}';
        }
    }

}
