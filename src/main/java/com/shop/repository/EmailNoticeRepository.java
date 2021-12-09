package com.shop.repository;

import com.shop.entity.EmailNotice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailNoticeRepository extends JpaRepository<EmailNotice, Long> {

}
