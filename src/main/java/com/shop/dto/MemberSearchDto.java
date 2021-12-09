package com.shop.dto;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MemberSearchDto {

    private String searchBy;

    private String searchQuery = "";
    
}
