package com.shop.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@ToString
public class AddressDto {

    private String name;

    private String phone;

    private String address;

    private String addressDetail;

}
