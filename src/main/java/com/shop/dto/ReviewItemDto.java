package com.shop.dto;

import com.shop.entity.OrderItem;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

//상세 페이지에 리뷰 목록 보여줄 DTO
@Getter @Setter
public class ReviewItemDto {

    private String email;

    private String comment;

    public ReviewItemDto(OrderItem orderItem){
        this.email = orderItem.getCreatedBy();
        this.comment = orderItem.getComment();
    }

    private List<ReviewItemDto> reviewItemDtoList = new ArrayList<>();

    public void addReviewItemDto(ReviewItemDto reviewItemDto){
        reviewItemDtoList.add(reviewItemDto);
    }

}
