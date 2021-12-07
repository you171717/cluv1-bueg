package com.shop.entity;

import com.shop.constant.GiftStatus;
import com.shop.constant.OrderStatus;
import com.shop.constant.ReturnStatus;
import com.shop.dto.OrderDto;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter
@Setter
public class Order extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private LocalDateTime orderDate;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    private String address;

    private int usedPoint;

    private int accPoint;

    @Enumerated(EnumType.STRING)
    private GiftStatus giftStatus;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<OrderItem> orderItems = new ArrayList<>();

    @Column(name = "return_req_date", nullable = true)
    private LocalDateTime returnReqDate;

    @Column(name = "return_confirm_date", nullable = true)
    private LocalDateTime returnConfirmDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "return_status")
    private ReturnStatus returnStatus;

    public void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    public static Order createOrder(Member member, OrderDto orderDto, List<OrderItem> orderItemList) {
        Order order = new Order();
        order.setMember(member);
        order.setOrderStatus(OrderStatus.ORDER);
        order.setOrderDate(LocalDateTime.now());
        order.setGiftStatus(orderDto.getGiftStatus());
        order.setUsedPoint(orderDto.getUsedPoint());
        order.setAccPoint((int) ((order.getTotalPrice() - order.getUsedPoint()) * 0.01));

        if(orderDto.getGiftStatus().equals(GiftStatus.BUY)) {
            order.setAddress(member.getAddress() + " " + member.getAddressDetail());
        } else if(orderDto.getGiftStatus().equals(GiftStatus.GIFT)) {
            order.setAddress(orderDto.getAddress() + " " + orderDto.getAddressDetail());
        }

        for(OrderItem orderItem : orderItemList) {
            order.addOrderItem(orderItem);
        }

        return order;
    }

    public int getTotalPrice() {
        int totalPrice = 0;

        for(OrderItem orderItem : orderItems) {
            totalPrice += orderItem.getTotalPrice();
        }

        return totalPrice;
    }

    public void cancelOrder() {
        this.orderStatus = OrderStatus.CANCEL;

        this.getMember().updatePoint(accPoint, usedPoint);

        for(OrderItem orderItem : orderItems) {
            orderItem.cancel();
        }
    }

}
