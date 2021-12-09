package com.shop.dto;

import com.shop.entity.OrderItem;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
public class ReviewItemDto {

    private String email;

    private String comment;

    private List<ReviewItemDto> reviewItemDtoList = new ArrayList<>();

    public ReviewItemDto(OrderItem orderItem){
        this.email = orderItem.getCreatedBy();
        this.comment = orderItem.getComment();
    }

    public void addReviewItemDto(ReviewItemDto reviewItemDto){
        reviewItemDtoList.add(reviewItemDto);
    }

}
