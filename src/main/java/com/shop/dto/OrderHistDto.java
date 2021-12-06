package com.shop.dto;

import com.shop.constant.GiftStatus;
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
    
    private Integer usedPoint; // 사용 포인트 dto

    private Integer accPoint; // 적립 포인트 dto

    private Integer totalPrice; // 최종 결제 금액

    private String orderAddress;

    //반품 요청일
    private String returnReqDate;

    //반품 확정일
    private String returnConfirmDate;

    //반품 여부
    private ReturnStatus returnStatus;

    private GiftStatus giftStatus;

    private List<OrderItemDto> orderItemDtoList = new ArrayList<>();

    public OrderHistDto(Order order) {
        this.orderId = order.getId();
        this.orderDate = order.getOrderDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        this.orderAddress = order.getAddress();
        this.orderStatus = order.getOrderStatus();
        this.giftStatus = order.getGiftStatus();

        if( order.getReturnReqDate() != null ) {
            this.returnReqDate = order.getReturnReqDate().format(DateTimeFormatter.ofPattern("yyyy-mm-dd HH:mm"));
        }

        if( order.getReturnConfirmDate() != null ) {
            this.returnConfirmDate = order.getReturnConfirmDate().format(DateTimeFormatter.ofPattern("yyyy-mm-dd HH:mm"));
        }

        this.returnStatus = order.getReturnStatus();

        this.usedPoint = order.getUsedPoint(); // 주문 시 사용된 포인트
        this.accPoint = order.getAccPoint(); // 주문 시 적립된 포인트
        this.totalPrice = order.getTotalPrice(); // 최종 결제 금액
    }

    public void addOrderItemDto(OrderItemDto orderItemDto) {
        orderItemDtoList.add(orderItemDto);
    }

}
