package com.shop.entity;

import com.shop.constant.BidDepositType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "bid")
@Getter
@Setter
public class Bid extends BaseEntity {

    @Id
    @Column(name = "bid_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id; // 역경매 구매 코드

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private BidDepositType depositType; // 입금 방법

    @Column(nullable = false)
    private String depositName; // 입금자명

    @Column(nullable = false)
    private Integer depositAmount; // 입금해야 될 금액

    @Column(nullable = false, length = 1)
    private String approvedYn = "N"; // 확인(낙찰) 여부

    private LocalDateTime approvedTime; // 확인(낙찰) 시간

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rauction_id", nullable = false)
    private ReverseAuction reverseAuction;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

}
