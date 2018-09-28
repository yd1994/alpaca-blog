package com.yd1994.alpacablog.common.base;

import java.io.Serializable;

/**
 * 基础DTO
 * 定义DTO功能
 * @param <E> 对应 entity
 *
 * @author yd
 */
public abstract class BaseDTO<E> implements Serializable {

    public BaseDTO() {
    }

    /**
     * 通过 entity 创建
     * @param entity
     */
    public BaseDTO(E entity) {
    }

    /**
     * 转换为 Entity
     * @return
     */
    public abstract E toEntity();
}
