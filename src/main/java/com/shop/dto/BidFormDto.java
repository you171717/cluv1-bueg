package com.shop.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
public class BidFormDto {

    private Long id;

    @NotEmpty(message = "결제 방법은 필수 입력 값 입니다.")
    private String depositType;

    @NotEmpty(message = "예금주는 필수 입력 값 입니다.")
    private String depositName;

    @NotNull(message = "결제 금액은 필수 입력 값 입니다.")
    @Min(value = 1, message = "결제 금액은 최소 값은 1원 입니다.")
    private Integer depositAmount;
    
    private String approvedYn = "N";

    private LocalDateTime approvedTime;

    @NotNull(message = "역경매 상품은 필수 입력 값 입니다.")
    private Long reverseAuctionId;

}
