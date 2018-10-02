package com.yd1994.alpacablog.repository;

import com.yd1994.alpacablog.entity.UserDO;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserDO, Long> {

    /**
     * 通过 username 查找
     * @param username
     * @return
     */
    UserDO findFirstByUsername(String username);

}
