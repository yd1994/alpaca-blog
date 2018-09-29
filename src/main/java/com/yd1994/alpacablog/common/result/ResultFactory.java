package com.yd1994.alpacablog.common.result;

import java.io.Serializable;

/**
 * 返回信息工厂
 * @author yd
 */
public class ResultFactory implements Serializable {

    public static final int STATUS_200 = 200;

    private static final String STATUS_200_DEFAULT_MESSAGE = "请求成功。";

    public static final int STATUS_404 = 404;

    private static final String STATUS_404_DEFAULT_MESSAGE = "该资源不存在。";

    /**
     * 创建 简单的返回信息
     *
     * @return Info
     */
    public static Info getSimpleInfo() {
        return new SimpleResultInfo();
    }

    /**
     * 创建 Restful api 使用的返回信息
     *
     * @param status 错误码 可通过 ResultFactory.STATUS_* 得到
     * @return Info
     */
    public static Info getInfo(int status) {
        Info info = new RestResultInfo(status);
        switch (status) {
            case STATUS_200:
                info.message(STATUS_200_DEFAULT_MESSAGE);
                break;
            case STATUS_404:
                info.message(STATUS_404_DEFAULT_MESSAGE);
                break;
        }
        return info;
    }

    /**
     * 获取 错误码：200 的返回信息
     *
     * @return
     */
    public static Info get200Info() {
        return getInfo(STATUS_200);
    }

    /**
     * 获取 错误码：404 的返回信息
     *
     * @return
     */
    public static Info get404Info() {
        return getInfo(STATUS_404);
    }

    /**
     * 返回信息
     */
    public interface Info {
        Info message(String message);
    }

    /**
     * 简单的返回信息
     *
     */
    private static class SimpleResultInfo implements Info {

        /**
         * 提示信息
         */
        private String message;

        @Override
        public Info message(String message) {
            this.message = message;
            return this;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        @Override
        public String toString() {
            return "SimpleResultInfo{" +
                    "message='" + message + '\'' +
                    '}';
        }
    }

    /**
     * Restful api 使用的返回信息
     *
     */
    private static class RestResultInfo implements Info {

        /**
         * 错误码
         */
        private int status;
        /**
         * 提示信息
         */
        private String message;

        public RestResultInfo(int status) {
            this.status = status;
        }

        @Override
        public Info message(String message) {
            this.message = message;
            return this;
        }

    }

}
