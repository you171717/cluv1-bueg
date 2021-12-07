package com.shop.entity;

import com.shop.constant.ItemSellStatus;
import com.shop.dto.ItemFormDto;
import com.shop.exception.OutOfStockException;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name = "item")
@Getter
@Setter
@ToString
public class Item extends BaseEntity {

    @Id
    @Column(name = "item_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cateCode")
    private Category category; // 카테고리 코드 조인

    @Column(nullable = false, length = 50)
    private String itemNm; // 상품명

    @Column(name="price", nullable = false)
    private int price; // 가격

    @Column(nullable = false)
    private int stockNumber; // 재고수량

    @Lob
    @Column(nullable = false)
    private String itemDetail; // 상품 상세 설명

    @Enumerated(EnumType.STRING)
    private ItemSellStatus itemSellStatus; // 상품 판매 상태
    
    private Integer shippingFee; // 배송비

    /*
    @ManyToMany
    @JoinTable(
        name = "member_item",
        joinColumns = @JoinColumn(name = "member_id"),
        inverseJoinColumns = @JoinColumn(name = "item_id")
    )
    private List<Member> member;
    */

    public static Item createItem(ItemFormDto itemFormDto, Category category){
        Item item = new Item();
        item.setCategory(category);
        item.itemNm = itemFormDto.getItemNm();
        item.price = itemFormDto.getPrice();
        item.stockNumber = itemFormDto.getStockNumber();
        item.shippingFee = itemFormDto.getShippingFee();
        item.itemDetail = itemFormDto.getItemDetail();
        item.itemSellStatus = itemFormDto.getItemSellStatus();

        return item;
    }

    public void updateItem(ItemFormDto itemFormDto) {
        this.itemNm = itemFormDto.getItemNm();
        this.price = itemFormDto.getPrice();
        this.stockNumber = itemFormDto.getStockNumber();
        this.shippingFee = itemFormDto.getShippingFee();
        this.itemDetail = itemFormDto.getItemDetail();
        this.itemSellStatus = itemFormDto.getItemSellStatus();
    }

    public void removeStock(int stockNumber) {
        int restStock = this.stockNumber - stockNumber;

        if(restStock < 0) {
            throw new OutOfStockException("상품 재고가 부족 합니다. (현재 재고 수량: " + this.stockNumber + ")");
        }

        this.stockNumber = restStock;
    }

    public void addStock(int stockNumber) {
        this.stockNumber += stockNumber;
    }

}
