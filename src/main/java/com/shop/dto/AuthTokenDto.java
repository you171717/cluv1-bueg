package com.shop.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
public class AuthTokenDto {

    private Long id;

    private String code;

    private LocalDateTime expireDate;
}
