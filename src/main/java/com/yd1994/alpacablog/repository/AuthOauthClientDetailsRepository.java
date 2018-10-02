package com.yd1994.alpacablog.repository;

import com.yd1994.alpacablog.entity.AuthOauthClientDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthOauthClientDetailsRepository extends JpaRepository<AuthOauthClientDetails, Long> {

    /**
     * 通过clientId 查找
     * @param clientId
     * @return
     */
    AuthOauthClientDetails findFirstByClientId(String clientId);

}
