package com.shop.dto;

import com.shop.constant.UsedItemSellStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UsedItemSearchDto {

    private String searchDateType;

    private UsedItemSellStatus searchSellStatus;

    private String searchBy;

    private String searchQuery = "";

}
