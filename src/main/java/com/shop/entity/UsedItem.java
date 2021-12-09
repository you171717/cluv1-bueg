package com.shop.entity;

import com.shop.constant.UsedItemSellStatus;
import com.shop.dto.UsedItemFormDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "used_item")
@Getter
@Setter
@ToString
public class UsedItem extends BaseEntity{

    @Id
    @Column(name = "used_item_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private Member owner;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false)
    private int price;

    @Lob
    @Column(nullable = false)
    private String detail;

    @Enumerated(EnumType.STRING)
    private UsedItemSellStatus usedItemSellStatus;

    private LocalDateTime endTime;

    public void updateItem(UsedItemFormDto usedItemFormDto) {
        this.name = usedItemFormDto.getName();
        this.price = usedItemFormDto.getPrice();
        this.detail = usedItemFormDto.getDetail();
        this.usedItemSellStatus = usedItemFormDto.getUsedItemSellStatus();
        this.endTime = usedItemFormDto.getEndTime();
    }

}
