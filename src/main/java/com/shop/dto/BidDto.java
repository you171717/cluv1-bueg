package com.shop.dto;

import com.querydsl.core.annotations.QueryProjection;
import com.shop.constant.BidDepositType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class BidDto {

    private Long id;

    private String imgUrl;

    private String itemNm;

    private String memberEmail;

    private BidDepositType depositType;

    private String depositName;

    private Integer depositAmount;

    private String approvedYn;

    private LocalDateTime approvedTime;

    private LocalDateTime regTime;

    @QueryProjection
    public BidDto(Long id, String imgUrl, String itemNm, String memberEmail, BidDepositType depositType, String depositName, Integer depositAmount, String approvedYn, LocalDateTime approvedTime, LocalDateTime regTime) {
        this.id = id;
        this.imgUrl = imgUrl;
        this.itemNm = itemNm;
        this.memberEmail = memberEmail;
        this.depositType = depositType;
        this.depositName = depositName;
        this.depositAmount = depositAmount;
        this.approvedYn = approvedYn;
        this.approvedTime = approvedTime;
        this.regTime = regTime;
    }
}
