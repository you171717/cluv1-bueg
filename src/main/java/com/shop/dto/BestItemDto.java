package com.shop.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class BestItemDto {

    private Long itemId;

    private String itemNm;

    private String itemDetail;

    private String imgUrl;

    private int price;

    @QueryProjection
    public BestItemDto(Long itemId, String itemNm, String itemDetail, String imgUrl, Integer price){
        this.itemId = itemId;
        this.itemNm = itemNm;
        this.itemDetail = itemDetail;
        this.imgUrl = imgUrl;
        this.price = price;
    }

}