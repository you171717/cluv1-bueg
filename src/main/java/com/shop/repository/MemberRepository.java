package com.shop.repository;

import com.shop.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface MemberRepository extends JpaRepository<Member, Long>, QuerydslPredicateExecutor<Member>, MemberRepositoryCustom {

    Member findByEmail(String email);

    //비밀번호 수정 쿼리
    @Transactional
    @Modifying
    @Query("update Member m set m.password = ?2 where m.id = ?1")
    void updatePassword(Long memberId, String password);

}
