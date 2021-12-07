package com.shop.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class GiftMainItemDto {

    private Long id;

    private Long cateCode;

    private String itemNm;

    private String itemDetail;

    private String imgUrl;

    private Integer price;

    @QueryProjection
    public GiftMainItemDto(Long id, Long cateCode, String itemNm, String itemDetail, String imgUrl, Integer price){
        this.id = id;
        this.cateCode = cateCode;
        this.itemNm = itemNm;
        this.itemDetail = itemDetail;
        this.imgUrl = imgUrl;
        this.price = price;
    }

}
