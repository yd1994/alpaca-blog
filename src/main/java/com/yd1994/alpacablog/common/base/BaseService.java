package com.yd1994.alpacablog.common.base;


/**
 * 基础Service
 *
 * @param <D> DTO
 */
public interface BaseService<D> {

    /**
     * 通过 id 获取
     * @param id ID
     * @return DTO
     */
    D get(Long id);

    /**
     * 添加
     * @param d
     * @return 添加是否成功
     */
    void add(D d);

    /**
     * 通过 id 修改
     * @param d
     * @param id 修改实体的id
     * @return 修改是否成功
     */
    void update(D d, Long id);

    /**
     * 通过 id 删除
     * @param id
     * @return 删除是否成功
     */
    void delete(Long id);

}
