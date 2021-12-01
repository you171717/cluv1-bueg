package com.shop.dto;

import com.shop.constant.ReverseAuctionSearchSortColumn;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Sort;

@Getter
@Setter
public class ReverseAuctionSearchDto {

    private String searchQuery = "";

    private String searchApprovedYn = "";

    private ReverseAuctionSearchSortColumn sortColumn = ReverseAuctionSearchSortColumn.REG_TIME;

    private Sort.Direction sortDirection = Sort.Direction.DESC;

}
