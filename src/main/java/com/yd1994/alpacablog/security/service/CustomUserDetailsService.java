package com.yd1994.alpacablog.security.service;

import com.yd1994.alpacablog.entity.UserDO;
import com.yd1994.alpacablog.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 加载用户信息
 *
 * @author yd
 */
public class CustomUserDetailsService implements UserDetailsService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        if (StringUtils.isEmpty(username)) {
            String msg = "登录失败：username is empty";
            logger.warn(msg);
            throw new UsernameNotFoundException(msg);
        }

        // 通过 username 查找 用户
        UserDO userDO = this.userService.getByUsername(username);
        if (userDO == null) {
            // 该用户不存在
            String msg = "登录失败：username:" + username + " not found.";
            logger.warn(msg);
            throw new UsernameNotFoundException(msg);
        }

        // 权限
        // oauth2 可使用 url 权限
        List<GrantedAuthority> authorityList = new ArrayList<>();

        return new User(userDO.getUsername(), userDO.getPassword(), authorityList);
    }

}
