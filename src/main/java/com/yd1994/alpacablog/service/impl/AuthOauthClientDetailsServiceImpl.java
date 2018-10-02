package com.yd1994.alpacablog.service.impl;

import com.yd1994.alpacablog.entity.AuthOauthClientDetails;
import com.yd1994.alpacablog.repository.AuthOauthClientDetailsRepository;
import com.yd1994.alpacablog.service.AuthOauthClientDetailsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthOauthClientDetailsServiceImpl implements AuthOauthClientDetailsService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private AuthOauthClientDetailsRepository authOauthClientDetailsRepository;

    @Override
    public AuthOauthClientDetails getByClientId(String clientId) {
        return authOauthClientDetailsRepository.findFirstByClientId(clientId);
    }

}
