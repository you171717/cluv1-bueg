package com.shop.entity;

import com.shop.dto.ReviewFormDto;
import com.shop.constant.ReturnStatus;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class OrderItem extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "order_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    private int orderPrice;

    private int count;

    private String reviewYn;

    private String comment;
    
    //반품 갯수
    @Column(name = "return_count")
    private int returnCount;

    //반품 금액
    @Column(name = "return_price")
    private int returnPrice;

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

    public static OrderItem createOrderItem(Item item, int count) {
        OrderItem orderItem = new OrderItem();
        orderItem.setItem(item);
        orderItem.setCount(count);
        orderItem.setOrderPrice(item.getPrice());
        orderItem.setReviewYn("N");
        item.removeStock(count);

        return orderItem;
    }

    public int getTotalPrice() {
        return orderPrice * count + item.getShippingFee();
    }

    public void cancel() {
        this.getItem().addStock(count);
    }

    public void createReview(ReviewFormDto reviewFormDto){
        this.comment = reviewFormDto.getComment();
    }

    public void updateReview(ReviewFormDto reviewFormDto){
        this.comment = reviewFormDto.getComment();
    }

    public void deleteReview(ReviewFormDto reviewFormDto){
        this.comment = null;
    }

}
