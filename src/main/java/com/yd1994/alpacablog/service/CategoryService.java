package com.yd1994.alpacablog.service;

import com.yd1994.alpacablog.common.base.BaseService;
import com.yd1994.alpacablog.common.param.RestRequestParam;
import com.yd1994.alpacablog.common.result.ResultFactory;
import com.yd1994.alpacablog.dto.Category;

import java.util.List;

/**
 * Category service 接口
 *
 * @author yd
 */
public interface CategoryService extends BaseService<Category> {

    /**
     * 通过 requestParam 获取 Category列表 长度
     *
     * @param requestParam
     * @return
     */
    Long listTotal(RestRequestParam requestParam);

    /**
     * 通过 requestParam 获取 Category列表
     * @param requestParam
     * @return
     */
    ResultFactory.CollectionData<Category> list(RestRequestParam requestParam);

}
