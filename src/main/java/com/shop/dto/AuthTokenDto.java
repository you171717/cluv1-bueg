package com.shop.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class AuthTokenDto {

    private Long id;

    private String code;

    private LocalDateTime expireDate;

}
