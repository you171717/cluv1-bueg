package com.shop.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "kakao_payment")
@Getter
@Setter
public class KakaoPayment extends BaseEntity {

    @Id
    @Column(name = "pay_id")
    private Long id;

    @Column(nullable = false)
    private Integer amount;

    @Column(nullable = false)
    private LocalDateTime approvedTime;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bid_id", nullable = false)
    private Bid bid;

}
