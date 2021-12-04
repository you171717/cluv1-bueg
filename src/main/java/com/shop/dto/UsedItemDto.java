package com.shop.dto;

import com.shop.constant.UsedItemSellStatus;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

public class UsedItemDto {

    private  Long id; // 상품 코드

    @NotBlank(message = "상품명은 필수 입력하셔야 합니다.")
    private String itemname; // 상품명

    @NotBlank(message = "가격은 필수 입력 값입니다.")
    private int price; // 가격

    private String detail; // 상세설명

    private UsedItemSellStatus sellStatus; //

    private LocalDateTime startday; // 등록일

    private LocalDateTime endday; // 마감일

}
