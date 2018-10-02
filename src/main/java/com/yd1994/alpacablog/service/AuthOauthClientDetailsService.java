package com.yd1994.alpacablog.service;

import com.yd1994.alpacablog.entity.AuthOauthClientDetails;

/**
 * AuthOauthClientDetails service 接口
 *
 * @author yd
 */
public interface AuthOauthClientDetailsService {

    /**
     * 通过 clientId 查找
     * @param clientId
     * @return
     */
    AuthOauthClientDetails getByClientId(String clientId);

}
