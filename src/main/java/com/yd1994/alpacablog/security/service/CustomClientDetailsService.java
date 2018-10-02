package com.yd1994.alpacablog.security.service;

import com.yd1994.alpacablog.entity.AuthOauthClientDetails;
import com.yd1994.alpacablog.service.AuthOauthClientDetailsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.security.oauth2.provider.NoSuchClientException;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 加载客户端信息
 *
 * @author yd
 */
public class CustomClientDetailsService implements ClientDetailsService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private AuthOauthClientDetailsService authOauthClientDetailsService;

    @Override
    public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {

        if (StringUtils.isEmpty(clientId)) {
            String msg = "授权失败：clientId is empty";
            logger.warn(msg);
            throw new NoSuchClientException(msg);
        }

        AuthOauthClientDetails authOauthClientDetails = this.authOauthClientDetailsService.getByClientId(clientId);

        if (authOauthClientDetails == null) {

            // 该客户端不存在
            String msg = "授权失败：clientId:" + clientId + " not found.";
            throw new NoSuchClientException("No client with requested id: " + clientId);
        }

        BaseClientDetails baseClientDetails = new BaseClientDetails();

        baseClientDetails.setClientId(authOauthClientDetails.getClientId());
        baseClientDetails.setClientSecret(authOauthClientDetails.getClientSecret());

        if (!StringUtils.isEmpty(authOauthClientDetails.getScope())) {
            baseClientDetails.setScope(Arrays.asList(authOauthClientDetails.getScope().split(",")));
        }
        if (!StringUtils.isEmpty(authOauthClientDetails.getResourceIds())) {
            baseClientDetails.setResourceIds(Arrays.asList(authOauthClientDetails.getResourceIds().split(",")));
        }
        if (!StringUtils.isEmpty(authOauthClientDetails.getAuthorities())) {
            List<String> authorityList = Arrays.asList(authOauthClientDetails.getAuthorities().split(","));
            List<GrantedAuthority> grantedAuthorityList = new ArrayList<>(authorityList.size());
            authorityList.forEach(authority -> grantedAuthorityList.add(new SimpleGrantedAuthority(authority)));
            baseClientDetails.setAuthorities(grantedAuthorityList);
        }
        if (!StringUtils.isEmpty(authOauthClientDetails.getAuthorizedGrantTypes())) {
            baseClientDetails.setAuthorizedGrantTypes(Arrays.asList(authOauthClientDetails.getAuthorizedGrantTypes().split(",")));
        }
        return baseClientDetails;
    }

}
