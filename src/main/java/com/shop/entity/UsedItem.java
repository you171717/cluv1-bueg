package com.shop.entity;

import com.shop.constant.UsedItemSellStatus;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "UsedItem")
@Getter
@Setter
@ToString
public class UsedItem extends BaseEntity{

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;        // 상품 코드

    @Column(nullable = false, length = 50)
    private String itemname; // 상품 이름

    @Column(name = "price", nullable = false)
    private int price;      // 가격

    @Lob
    @Column(nullable = false)
    private String detail;  // 중고상품 상세설명

    @Enumerated(EnumType.STRING) // SOLD_NOW(판매중) , SOLD_OUT(거래완료)
    private UsedItemSellStatus usedItemSellStatus; // 중고상품판매상태

    @Column(name = "start_day")
    private LocalDateTime startday;         // 게시글 게시일 컬럼

    @Column(name = "end_day")
    private LocalDateTime endday;           // 게시글 마감일 컬럼

}
