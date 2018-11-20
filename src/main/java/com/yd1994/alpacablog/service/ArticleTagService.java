package com.yd1994.alpacablog.service;

import com.yd1994.alpacablog.common.base.BaseService;
import com.yd1994.alpacablog.common.param.RestRequestParam;
import com.yd1994.alpacablog.common.result.ResultFactory;
import com.yd1994.alpacablog.dto.ArticleTag;
import com.yd1994.alpacablog.dto.Category;

/**
 * ArticleTag service 接口
 *
 * @author yd
 */
public interface ArticleTagService extends BaseService<ArticleTag> {

    /**
     * 通过 requestParam 获取 ArticleTag 长度
     *
     * @param requestParam
     * @return
     */
    Long listTotal(RestRequestParam requestParam);

    /**
     * 通过 requestParam 获取 ArticleTag 列表
     * @param requestParam
     * @return
     */
    ResultFactory.CollectionData<ArticleTag> list(RestRequestParam requestParam);

}
