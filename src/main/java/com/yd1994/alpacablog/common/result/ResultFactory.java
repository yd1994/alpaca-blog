package com.yd1994.alpacablog.common.result;

import org.springframework.security.core.parameters.P;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * 返回信息工厂
 * @author yd
 */
public class ResultFactory implements Serializable {

    public static final int STATUS_200 = 200;

    private static final String STATUS_200_DEFAULT_MESSAGE = "请求成功。";

    public static final int STATUS_400 = 400;

    private static final String STATUS_400_DEFAULT_MESSAGE = "请求出错。";

    public static final int STATUS_404 = 404;

    private static final String STATUS_404_DEFAULT_MESSAGE = "该资源不存在。";

    public static final int STATUS_500 = 500;

    private static final String STATUS_500_DEFAULT_MESSAGE = "服务器异常。";

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
                info.setMessage(STATUS_200_DEFAULT_MESSAGE);
                break;
            case STATUS_400:
                info.setMessage(STATUS_400_DEFAULT_MESSAGE);
                break;
            case STATUS_404:
                info.setMessage(STATUS_404_DEFAULT_MESSAGE);
                break;
            case STATUS_500:
                info.setMessage(STATUS_500_DEFAULT_MESSAGE);
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
     * 获取 错误码：400 的返回信息
     *
     * @return
     */
    public static Info get400Info() {
        return getInfo(STATUS_400);
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
     *
     * @return
     */
    public static Info get500Info() {
        return getInfo(STATUS_500);
    }

    /**
     * 返回信息
     */
    public static class Info implements Serializable {

        /**
         * 错误码
         */
        private int status;
        /**
         * 提示信息
         */
        private String message;
        /**
         * 返回数据
         */
        private Object data;

        public Info() {
        }

        public Info(int status) {
            this.status = status;
        }

        public Info(int status, String message) {
            this.status = status;
            this.message = message;
        }

        /**
         * 在提示信息后添加
         * @param message 提示信息
         * @return
         */
        public Info appendMessage(String message) {
            this.message += message;
            return this;
        }

        public Info data(Object data) {
            this.data = data;
            return this;
        }

        /**
         * 修改提示信息
         * @param message 提示信息
         * @return
         */
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

        public Object getData() {
            return data;
        }

        public void setData(Object data) {
            this.data = data;
        }

        @Override
        public String toString() {
            return "Info{" +
                    "status=" + status +
                    ", message='" + message + '\'' +
                    ", data=" + data +
                    '}';
        }
    }

    public static class CollectionData<T> implements Serializable {

        /**
         * 分页后的集合
         */
        private Collection<T> data;
        /**
         * 集合的总数
         */
        private Long total;

        public CollectionData(Collection<T> data, Long total) {
            this.data = data;
            this.total = total;
        }

        public Collection<T> getData() {
            return data;
        }

        public void setData(Collection<T> data) {
            this.data = data;
        }

        public Long getTotal() {
            return total;
        }

        public void setTotal(Long total) {
            this.total = total;
        }

        @Override
        public String toString() {
            return "CollectionData{" +
                    "data=" + data +
                    ", total=" + total +
                    '}';
        }
    }

    public static <T> CollectionData<T> getCollectionData(Collection<T> data, Long total) {
        return new CollectionData<T>(data, total);
    }

}
