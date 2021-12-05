package com.shop.dto;

import com.shop.constant.ReturnStatus;
import com.shop.entity.OrderItem;
import lombok.Getter;
import lombok.Setter;

import java.time.format.DateTimeFormatter;

@Getter
@Setter
public class OrderItemDto {

    private String itemNm;

    private int count;

    private int orderPrice;

    private int orderShippingFee;

    private String imgUrl;

    private String comment;

    private Long orderItemId;

    private String reviewYn;

    //반품 갯수
    private int returnCount;

    //반품 금액
    private int returnPrice;

    //반품 요청일
    private String returnReqDate;

    //반품 확정일
    private String returnConfirmDate;

    //반품 여부
    private ReturnStatus returnStatus;

    public OrderItemDto(OrderItem orderItem, String imgUrl) {
        this.itemNm = orderItem.getItem().getItemNm();
        this.count = orderItem.getCount();
        this.orderPrice = orderItem.getOrderPrice();
        this.orderShippingFee = orderItem.getItem().getShippingFee();
        this.imgUrl = imgUrl;
        this.reviewYn = orderItem.getReviewYn();
        this.orderItemId = orderItem.getId();
        this.comment = orderItem.getComment();

        this.returnCount = orderItem.getReturnCount();
        this.returnPrice = orderItem.getReturnPrice();

        if( orderItem.getReturnReqDate() != null ) {
            this.returnReqDate = orderItem.getReturnReqDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        }
        
        if( orderItem.getReturnConfirmDate() != null ) {
            this.returnConfirmDate = orderItem.getReturnConfirmDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        }

        this.returnStatus = orderItem.getReturnStatus();
    }

}
