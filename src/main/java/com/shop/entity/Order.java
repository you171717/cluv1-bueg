package com.shop.entity;

import com.shop.constant.GiftStatus;
import com.shop.constant.OrderStatus;
import com.shop.constant.ReturnStatus;
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

    private String address; // 주문 배송지

    @Enumerated(EnumType.STRING)
    private GiftStatus giftStatus; // 구매/선물 상태

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<OrderItem> orderItems = new ArrayList<>();

    //반품 요청일
    @Column(name = "return_req_date", nullable = true)
    private LocalDateTime returnReqDate;

    //반품 확정일
    @Column(name = "return_confirm_date", nullable = true)
    private LocalDateTime returnConfirmDate;

    //반품 여부
    @Enumerated(EnumType.STRING)
    @Column(name = "return_status")
    private ReturnStatus returnStatus;
    
    private int usedPoint; // 적용 포인트

    private int accPoint; // 사용된 포인트 기록

    public void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    public static Order createOrder(Member member, Integer usedPoint, List<OrderItem> orderItemList, GiftStatus giftStatus,
                                    String address, String addressDetail) {
        Order order = new Order();
        order.setMember(member);

        order.setUsedPoint(usedPoint); // 사용 포인트 저장

        for(OrderItem orderItem : orderItemList) {
            order.addOrderItem(orderItem);
        }

        order.setAccPoint((int)((order.getTotalPrice() - order.getUsedPoint()) * 0.01)); // 적립 포인트 저장

        order.setOrderStatus(OrderStatus.ORDER);
        order.setOrderDate(LocalDateTime.now());
        order.setGiftStatus(giftStatus);
        order.setAddress(address + " " +addressDetail);


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

        for(OrderItem orderItem : orderItems) {
            orderItem.cancel();
        }
    }

}
