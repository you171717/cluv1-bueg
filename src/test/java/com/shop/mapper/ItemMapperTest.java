package com.shop.mapper;

import com.shop.constant.ItemSellStatus;
import com.shop.dto.ItemDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
class ItemMapperTest {

    @Autowired
    ItemMapper itemMapper;

    // JPA Migration은 Auto Increment을 적용 시켜주지 않아서 수동으로 제어
    // 주의: 객체 생성할 때 `autoIncrement++` 를 꼭 사용할 것
    static Long autoIncrement = 1L;

    @Test
    @DisplayName("상품 저장 테스트")
    public void createItemTest() {
        ItemDto item = new ItemDto();
        item.setId(autoIncrement++);
        item.setItemNm("테스트 상품");
        item.setPrice(10000);
        item.setItemDetail("테스트 상품 상세 설명");
        item.setItemSellStatus(ItemSellStatus.SELL);
        item.setStockNumber(100);
        item.setRegTime(LocalDateTime.now());
        item.setUpdateTime(LocalDateTime.now());
        itemMapper.insertItem(item);
        System.out.println(item.toString());
    }

    public void createItemList() {
        for(int i = 1; i <= 10; i++) {
            ItemDto item = new ItemDto();
            item.setId(autoIncrement++);
            item.setItemNm("테스트 상품" + i);
            item.setPrice(10000 + i);
            item.setItemDetail("테스트 상품 상세 설명" + i);
            item.setItemSellStatus(ItemSellStatus.SELL);
            item.setStockNumber(100);
            item.setRegTime(LocalDateTime.now());
            item.setUpdateTime(LocalDateTime.now());
            itemMapper.insertItem(item);
        }
    }

    public void createItemList2() {
        for(int i = 1; i <= 5; i++) {
            ItemDto item = new ItemDto();
            item.setId(autoIncrement++);
            item.setItemNm("테스트 상품" + i);
            item.setPrice(10000 + i);
            item.setItemDetail("테스트 상품 상세 설명" + i);
            item.setItemSellStatus(ItemSellStatus.SELL);
            item.setStockNumber(100);
            item.setRegTime(LocalDateTime.now());
            item.setUpdateTime(LocalDateTime.now());
            itemMapper.insertItem(item);
        }

        for(int i = 6; i <= 10; i++) {
            ItemDto item = new ItemDto();
            item.setId(autoIncrement++);
            item.setItemNm("테스트 상품" + i);
            item.setPrice(10000 + i);
            item.setItemDetail("테스트 상품 상세 설명" + i);
            item.setItemSellStatus(ItemSellStatus.SOLD_OUT);
            item.setStockNumber(0);
            item.setRegTime(LocalDateTime.now());
            item.setUpdateTime(LocalDateTime.now());
            itemMapper.insertItem(item);
        }
    }

    @Test
    @DisplayName("상품명 조회 테스트")
    public void findByItemNmTest() {
        this.createItemList();
        List<ItemDto> itemList = itemMapper.findByItemNm("테스트 상품1");
        for(ItemDto item : itemList) {
            System.out.println(item.toString());
        }
    }

    @Test
    @DisplayName("상품명, 상품상세설명 or 테스트")
    public void findByItemNmOrItemDetailTest() {
        this.createItemList();
        List<ItemDto> itemList = itemMapper.findByItemNmOrItemDetail("테스트 상품1", "테스트 상품 상세 설명5");
        for(ItemDto item : itemList) {
            System.out.println(item.toString());
        }
    }

    @Test
    @DisplayName("가격 LessThan 테스트")
    public void findByPriceLessThanTest() {
        this.createItemList();
        List<ItemDto> itemList = itemMapper.findByPriceLessThan(10005);
        for(ItemDto item : itemList) {
            System.out.println(item.toString());
        }
    }

    @Test
    @DisplayName("가격 내림차순 조회 테스트")
    public void findByPriceLessThanOrderByPriceDesc() {
        this.createItemList();
        List<ItemDto> itemList = itemMapper.findByPriceLessThanOrderByPriceDesc(10005);
        for(ItemDto item : itemList) {
            System.out.println(item.toString());
        }
    }

    @Test
    @DisplayName("@Query를 이용한 상품 조회 테스트")
    public void findByItemDetailTest() {
        this.createItemList();
        List<ItemDto> itemList = itemMapper.findByItemDetail("테스트 상품 상세 설명");
        for(ItemDto item : itemList) {
            System.out.println(item.toString());
        }
    }

    @Test
    @DisplayName("nativeQuery 속성을 이용한 상품 조회 테스트")
    public void findByItemDetailByNative() {
        this.createItemList();
        List<ItemDto> itemList = itemMapper.findByItemDetailByNative("테스트 상품 상세 설명");
        for(ItemDto item : itemList) {
            System.out.println(item.toString());
        }
    }

}