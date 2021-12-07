package com.shop.dto;

import com.shop.constant.ReturnStatus;
import com.shop.entity.OrderItem;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.format.DateTimeFormatter;

@Getter
@Setter
@ToString
public class OrderItemDto {

    private String itemNm;

    private int count;

    private int orderPrice;

    private int orderShippingFee;

    private String imgUrl;

    private String comment;

    private Long orderItemId;

    private String reviewYn;

    private int returnCount;

    private int returnPrice;

    private String returnReqDate;

    private String returnConfirmDate;

    private ReturnStatus returnStatus;

    public OrderItemDto(OrderItem orderItem, String imgUrl) {
        this.itemNm = orderItem.getItem().getItemNm();
        this.count = orderItem.getCount();
        this.orderPrice = orderItem.getOrderPrice();
        this.orderShippingFee = orderItem.getItem().getShippingFee();
        this.imgUrl = imgUrl;
        this.comment = orderItem.getComment();
        this.orderItemId = orderItem.getId();
        this.reviewYn = orderItem.getReviewYn();
        this.returnCount = orderItem.getReturnCount();
        this.returnPrice = orderItem.getReturnPrice();
        this.returnStatus = orderItem.getReturnStatus();

        if(orderItem.getReturnReqDate() != null) {
            this.returnReqDate = orderItem.getReturnReqDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        }
        
        if(orderItem.getReturnConfirmDate() != null) {
            this.returnConfirmDate = orderItem.getReturnConfirmDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        }
    }

}
