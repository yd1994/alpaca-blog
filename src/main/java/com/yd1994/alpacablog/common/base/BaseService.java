package com.yd1994.alpacablog.common.base;


import org.springframework.data.domain.Pageable;

import java.util.List;

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
     * 通过 分页信息 pageable 获取列表
     * @param pageable 分页信息
     * @return List<D> List<DTO>
     */
    List<D> list(Pageable pageable);

    /**
     * 添加
     * @param d
     * @return 添加是否成功
     */
    boolean add(D d);

    /**
     * 通过 id 修改
     * @param D
     * @param id 修改实体的id
     * @return 修改是否成功
     */
    boolean update(D d, Long id);

    /**
     * 通过 id 删除
     * @param id
     * @return 删除是否成功
     */
    boolean delete(Long id);

}
