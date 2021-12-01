package com.shop.service;

import com.shop.entity.Bid;
import com.shop.entity.KakaoPayment;
import com.shop.repository.BidRepository;
import com.shop.repository.KakaoPaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class KakaoPaymentService {

    private final BidRepository bidRepository;

    private final KakaoPaymentRepository kakaoPaymentRepository;

    public KakaoPayment savePaymentHistory(Long bidId, Long id, Integer amount) {
        Bid bid = bidRepository.findById(bidId).orElseThrow(EntityNotFoundException::new);

        KakaoPayment kakaoPayment = new KakaoPayment();
        kakaoPayment.setId(id);
        kakaoPayment.setBid(bid);
        kakaoPayment.setAmount(amount);
        kakaoPayment.setApprovedTime(LocalDateTime.now());

        return kakaoPaymentRepository.save(kakaoPayment);
    }

}
