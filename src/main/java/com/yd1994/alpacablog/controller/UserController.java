package com.yd1994.alpacablog.controller;

import com.yd1994.alpacablog.common.result.ResultFactory;
import com.yd1994.alpacablog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public Object get() {
        Object user = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return user;
    }

    @PutMapping("/password")
    public ResultFactory.Info updatePassword(String oldPassword, String newPassword) {
        Object user = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (user instanceof User) {
            this.userService.updateUserPassword(oldPassword, newPassword, ((User) user).getUsername());
        }
        return ResultFactory.get200Info().message("密码修改成功。");
    }

}
