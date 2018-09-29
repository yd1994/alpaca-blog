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
        return new Info();
    }

    /**
     * 创建 Restful api 使用的返回信息
     *
     * @param status 错误码 可通过 ResultFactory.STATUS_* 得到
     * @return Info
     */
    public static Info getInfo(int status) {
        Info info = new Info(status);
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
    public static class Info {

        /**
         * 错误码
         */
        private int status;
        /**
         * 提示信息
         */
        private String message;

        public Info() {
        }

        public Info(int status) {
            this.status = status;
        }

        public Info(int status, String message) {
            this.status = status;
            this.message = message;
        }

        public Info message(String message) {
            this.message = message;
            return this;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        @Override
        public String toString() {
            return "Info{" +
                    "status=" + status +
                    ", message='" + message + '\'' +
                    '}';
        }
    }

}
