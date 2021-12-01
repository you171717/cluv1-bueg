package com.shop.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "reverse_auction")
@Getter
@Setter
public class ReverseAuction extends BaseEntity {

    @Id
    @Column(name = "rauction_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id; // 역경매 코드

    @Column(nullable = false)
    private Integer timeUnit; // 자동 할인 시간

    @Column(nullable = false)
    private Integer priceUnit; // 경매 단위

    @Column(nullable = false)
    private Integer maxRate; // 최대 할인율

    @Column(nullable = false)
    private LocalDateTime startTime; // 시작 시간

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "reverseAuction")
    private List<Bid> bids = new ArrayList<>(); // 입찰 목록

}
