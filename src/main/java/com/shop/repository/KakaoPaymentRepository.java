package com.shop.repository;

import com.shop.entity.KakaoPayment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KakaoPaymentRepository extends JpaRepository<KakaoPayment, Long> {

}
