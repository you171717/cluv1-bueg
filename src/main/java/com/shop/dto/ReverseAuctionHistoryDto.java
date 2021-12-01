package com.shop.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ReverseAuctionHistoryDto {

    private Long id;

    private String imgUrl;

    private String itemNm;

    private Integer itemShippingFee;

    private Integer startPrice;

    private LocalDateTime startTime;

    private Integer priceUnit;

    private Integer timeUnit;

    private Integer maxRate;

    private String approvedYn;

    private LocalDateTime approvedTime;

    private String memberEmail;

    private Integer depositAmount;

    private DiscountDto discountDto;

    @QueryProjection
    public ReverseAuctionHistoryDto(Long id, String imgUrl, String itemNm, Integer itemShippingFee, Integer startPrice, LocalDateTime startTime, Integer priceUnit, Integer timeUnit, Integer maxRate, String approvedYn, LocalDateTime approvedTime, String memberEmail, Integer depositAmount) {
        this.id = id;
        this.imgUrl = imgUrl;
        this.itemNm = itemNm;
        this.itemShippingFee = itemShippingFee;
        this.startPrice = startPrice;
        this.startTime = startTime;
        this.priceUnit = priceUnit;
        this.timeUnit = timeUnit;
        this.maxRate = maxRate;
        this.approvedYn = approvedYn;
        this.approvedTime = approvedTime;
        this.memberEmail = memberEmail;
        this.depositAmount = depositAmount;
        this.discountDto = new DiscountDto(startTime, startPrice, timeUnit, priceUnit);
    }
}
