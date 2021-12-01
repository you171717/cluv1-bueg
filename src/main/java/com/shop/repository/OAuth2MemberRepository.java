package com.shop.repository;

import com.shop.entity.Member;
import com.shop.entity.OAuth2Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OAuth2MemberRepository extends JpaRepository<OAuth2Member, Member> {

    OAuth2Member findByMember(Member member);

}
