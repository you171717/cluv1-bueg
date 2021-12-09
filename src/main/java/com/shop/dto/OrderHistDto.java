package com.shop.dto;

import com.shop.constant.GiftStatus;
import com.shop.constant.OrderStatus;
import com.shop.constant.ReturnStatus;
import com.shop.entity.Order;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
public class OrderHistDto {

    private Long orderId;

    private String orderDate;

    private OrderStatus orderStatus;
    
    private Integer usedPoint;

    private Integer accPoint;

    private Integer totalPrice;

    private String orderAddress;

    private String returnReqDate;

    private String returnConfirmDate;

    private ReturnStatus returnStatus;

    private GiftStatus giftStatus;

    private List<OrderItemDto> orderItemDtoList = new ArrayList<>();

    public OrderHistDto(Order order) {
        this.orderId = order.getId();
        this.orderDate = order.getOrderDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        this.orderStatus = order.getOrderStatus();
        this.usedPoint = order.getUsedPoint();
        this.accPoint = order.getAccPoint();
        this.totalPrice = order.getTotalPrice();
        this.orderAddress = order.getAddress();
        this.returnStatus = order.getReturnStatus();
        this.giftStatus = order.getGiftStatus();

        if(order.getReturnReqDate() != null ) {
            this.returnReqDate = order.getReturnReqDate().format(DateTimeFormatter.ofPattern("yyyy-mm-dd HH:mm"));
        }

        if(order.getReturnConfirmDate() != null ) {
            this.returnConfirmDate = order.getReturnConfirmDate().format(DateTimeFormatter.ofPattern("yyyy-mm-dd HH:mm"));
        }
    }

    public void addOrderItemDto(OrderItemDto orderItemDto) {
        orderItemDtoList.add(orderItemDto);
    }

}
