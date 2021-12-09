package com.shop.entity;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "used_item_img")
@Getter @Setter
public class UsedItemImg extends BaseEntity{

    @Id
    @Column(name = "used_item_img_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "used_item_id")
    private UsedItem usedItem;

    private String imgName;

    private String oriImgName;

    private String imgUrl;

    private String repimgYn;

    public void updateUsedItemImg(String oriImgName, String imgName, String imgUrl) {
        this.oriImgName = oriImgName;
        this.imgName = imgName;
        this.imgUrl = imgUrl;
    }
}
