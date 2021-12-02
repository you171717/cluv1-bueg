package com.shop.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "review_img")
@Getter @Setter
public class ReviewImg extends BaseEntity {

    @Id
    @Column(name = "review_img_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String reviewImgName;

    private String reviewOriImgName;

    private String reviewImgUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_item_id")
    private OrderItem orderItem;

    public void updateReviewImg(String reviewOriImgName, String reviewImgName, String reviewImgUrl){
        this.reviewOriImgName = reviewOriImgName;
        this.reviewImgName = reviewImgName;
        this.reviewImgUrl = reviewImgUrl;
    }
}
