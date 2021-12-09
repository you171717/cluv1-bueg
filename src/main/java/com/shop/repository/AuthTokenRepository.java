package com.shop.repository;

import com.shop.entity.AuthToken;
import com.shop.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthTokenRepository extends JpaRepository<AuthToken, Long> {

    AuthToken findFirstByMemberOrderByRegTimeDesc(Member member);
    AuthToken findFirstByCodeOrderByRegTimeDesc(String code);

}
