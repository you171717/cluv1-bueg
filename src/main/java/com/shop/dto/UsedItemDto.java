package com.shop.dto;

import com.querydsl.core.annotations.QueryProjection;
import com.shop.constant.ItemSellStatus;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

public class UsedItemDto {

    private  Long id; // 상품 코드

    @NotBlank(message = "상품명은 필수 입력하셔야 합니다.")
    private String itemName; // 상품명

    @NotBlank(message = "가격은 필수 입력 값입니다.")
    private int price; // 가격

    private String detail; // 상세설명

    private ItemSellStatus itemSellStatus; // 판매 상태

    private String imgUrl; // 이미지 조회 경로

    private LocalDateTime startDay; // 등록일

    private LocalDateTime endDay; // 마감일

    @QueryProjection
    public UsedItemDto(Long id, String itemName, String itemDetail, String imgUrl, Integer price
                                ,LocalDateTime startDay, LocalDateTime endDay){
        this.id = id;
        this.itemName = itemName;
        this.detail = itemDetail;
        this.imgUrl = imgUrl;
        this.price = price;
        this.startDay = startDay;
        this.endDay = endDay;
    }

}
