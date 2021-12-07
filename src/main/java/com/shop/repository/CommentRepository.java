package com.shop.repository;

import com.shop.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment,Long> {

    @Query("select c from Comment c where c.inquiry.id = :inquiry_id")
    List<Comment> findByInquiryId(@Param("inquiry_id") Long inquiryId);

    @Transactional
    @Modifying
    @Query("delete from Comment c where c.inquiry.id = :inquiry_id")
    void deleteByInquiryId(@Param("inquiry_id") Long inquiryId);

}
