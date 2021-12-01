package com.shop.dto;

import com.shop.constant.Bank;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class OAuth2FormDto {

    @NotBlank(message = "이름은 필수 입력 값입니다.")
    private String name;

    @NotEmpty(message = "이메일은 필수 입력 값입니다.")
    private String email;

    @NotEmpty(message = "주소는 필수 입력 값입니다.")
    private String address;

    @NotNull(message = "환불 은행은 필수 입력 값입니다.")
    private Bank refundBank;

    @NotEmpty(message = "환불 계좌 번호는 입력 값입니다.")
    private String refundAccount;

}