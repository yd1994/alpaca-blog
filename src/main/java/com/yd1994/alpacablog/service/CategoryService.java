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

    ResultFactory.Collection<Category> list(RestRequestParam requestParam);

}
