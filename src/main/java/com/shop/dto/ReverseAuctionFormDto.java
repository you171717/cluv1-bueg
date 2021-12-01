package com.shop.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
public class ReverseAuctionFormDto {

    private Long id;

    @NotNull(message = "자동 할인 시간은 필수 입력 값입니다.")
    @Min(value = 1, message = "자동 할인 시간의 최소 값은 1시간 입니다.")
    private Integer timeUnit;

    @NotNull(message = "경매 단위는 필수 입력 값입니다.")
    @Min(value = 1, message = "경매 단위의 최소 값은 1원 입니다.")
    private Integer priceUnit;

    @NotNull(message = "최대 할인율은 필수 입력 값입니다.")
    @Min(value = 1, message = "최대 할인율은 최대 100%, 최소 1% 입니다.")
    @Max(value = 100, message = "최대 할인율은 최대 100%, 최소 1% 입니다.")
    private Integer maxRate;

    private LocalDateTime startTime;

    @NotNull(message = "상품은 필수 입력 값입니다.")
    private Long itemId;

}
