package com.yd1994.alpacablog.service;

import com.yd1994.alpacablog.common.base.BaseService;
import com.yd1994.alpacablog.dto.SysInformation;

/**
 * SysInformation Service 接口
 *
 * @author yd
 */
public interface SysInformationService extends BaseService<SysInformation> {

    /**
     * 通过 name 获取
     * @param name 信息名
     * @return
     */
    SysInformation get(String name);

    /**
     * 通过 name 修改
     * @param sysInformation
     * @param name 信息名
     */
    void update(SysInformation sysInformation, String name);

    /**
     * 通过 name 删除
     * @param name
     */
    void delete(String name);
}
