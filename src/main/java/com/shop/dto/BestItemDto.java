package com.shop.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class BestItemDto {

    private String itemId;

    private String itemNm;

    private String itemDetail;

    private String imgUrl;

    private int price;

}