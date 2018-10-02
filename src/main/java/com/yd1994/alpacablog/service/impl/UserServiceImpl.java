package com.yd1994.alpacablog.service.impl;

import com.yd1994.alpacablog.entity.UserDO;
import com.yd1994.alpacablog.repository.UserRepository;
import com.yd1994.alpacablog.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDO getById(Long id) {
        return null;
    }

    @Override
    public UserDO getByUsername(String username) {
        return this.userRepository.findFirstByUsername(username);
    }

    @Override
    public void updateUserPassword(String newPassword, String oldPassword, Long id) {
    }
}
