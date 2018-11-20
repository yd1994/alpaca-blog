package com.yd1994.alpacablog.service.impl;

import com.yd1994.alpacablog.common.exception.RequestParamErrorException;
import com.yd1994.alpacablog.common.exception.ResourceNotFoundException;
import com.yd1994.alpacablog.entity.UserDO;
import com.yd1994.alpacablog.repository.UserRepository;
import com.yd1994.alpacablog.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@CacheConfig(cacheNames = "users")
public class UserServiceImpl implements UserService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Cacheable(key = "#id", unless = "#result == null")
    @Override
    public UserDO getById(Long id) {
        return null;
    }

    @Cacheable(key = "#username", unless = "#result == null")
    @Override
    public UserDO getByUsername(String username) {
        return this.userRepository.findFirstByUsername(username);
    }

    @CacheEvict(allEntries = true)
    @Override
    public void updateUserPassword(String oldPassword, String newPassword, String username) {
        if (StringUtils.isEmpty(oldPassword) || StringUtils.isEmpty(newPassword)) {
            throw new RequestParamErrorException("参数有误。");
        }
        UserDO userDO = this.userRepository.findFirstByUsername(username);
        if (userDO == null) {
            throw new ResourceNotFoundException("该用户不存在。");
        }
        // 验证旧密码
        if (!this.bCryptPasswordEncoder.matches(oldPassword, userDO.getPassword())) {
            throw new RequestParamErrorException("原密码错误。");
        }
        // 加密
        String byPassword = this.bCryptPasswordEncoder.encode(newPassword);
        userDO.setPassword(byPassword);
        this.userRepository.save(userDO);
    }
}
