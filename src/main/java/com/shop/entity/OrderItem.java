package com.shop.entity;

import com.shop.constant.ReturnStatus;
import com.shop.dto.ReviewFormDto;
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
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @Column(nullable = false)
    private int orderPrice;

    @Column(nullable = false)
    private int count;

    @Column(nullable = false)
    private String reviewYn = "N";

    private String comment;

    @Column(name = "return_count")
    private int returnCount;

    @Column(name = "return_price")
    private int returnPrice;

    @Column(name = "return_req_date")
    private LocalDateTime returnReqDate;

    @Column(name = "return_confirm_date")
    private LocalDateTime returnConfirmDate;

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
