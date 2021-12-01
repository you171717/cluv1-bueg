package com.shop.dto;

import com.shop.constant.OrderStatus;
import com.shop.constant.ReturnStatus;
import com.shop.entity.Order;
import lombok.Getter;
import lombok.Setter;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class OrderHistDto {


    private Long orderId;

    private String orderDate;

    private OrderStatus orderStatus;

    //반품 요청일
    private String returnReqDate;

    //반품 확정일
    private String returnConfirmDate;

    //반품 여부
    private ReturnStatus returnStatus;

    private List<OrderItemDto> orderItemDtoList = new ArrayList<>();

    public OrderHistDto(Order order) {
        this.orderId = order.getId();
        this.orderDate = order.getOrderDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        this.orderStatus = order.getOrderStatus();

        if( order.getReturnReqDate() != null ) {
            this.returnReqDate = order.getReturnReqDate().format(DateTimeFormatter.ofPattern("yyyy-mm-dd HH:mm"));
        }

        if( order.getReturnConfirmDate() != null ) {
            this.returnConfirmDate = order.getReturnConfirmDate().format(DateTimeFormatter.ofPattern("yyyy-mm-dd HH:mm"));
        }

        this.returnStatus = order.getReturnStatus();

    }

    public void addOrderItemDto(OrderItemDto orderItemDto) {
        orderItemDtoList.add(orderItemDto);
    }

}
