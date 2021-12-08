package com.shop.dto;

import com.querydsl.core.annotations.QueryProjection;
import com.shop.constant.UsedItemSellStatus;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class UsedItemDto {

    private Long id;

    private String name;

    private int price;

    private String detail;

    private UsedItemSellStatus usedItemSellStatus;

    private String imgUrl;

    private LocalDateTime endTime;

    @QueryProjection
    public UsedItemDto(Long id, String name, String detail, UsedItemSellStatus usedItemSellStatus, String imgUrl, Integer price, LocalDateTime endTime) {
        this.id = id;
        this.name = name;
        this.detail = detail;
        this.usedItemSellStatus = usedItemSellStatus;
        this.imgUrl = imgUrl;
        this.price = price;
        this.endTime = endTime;
    }

}
