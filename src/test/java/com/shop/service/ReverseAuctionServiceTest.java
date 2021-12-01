package com.shop.service;

import com.shop.constant.ItemSellStatus;
import com.shop.constant.ReverseAuctionSearchSortColumn;
import com.shop.dto.*;
import com.shop.entity.ReverseAuction;
import com.shop.mapstruct.ReverseAuctionFormMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@TestPropertySource(locations="classpath:application-test.properties")
class ReverseAuctionServiceTest {

    @Autowired
    ItemService itemService;

    @Autowired
    ItemImgService itemImgService;

    @Autowired
    ReverseAuctionService reverseAuctionService;

    @Autowired
    ReverseAuctionFormMapper reverseAuctionFormMapper;

    List<MultipartFile> createMultipartFiles() {
        List<MultipartFile> multipartFileList = new ArrayList<>();

        for(int i = 0; i < 5; i++) {
            String path = "C:/shop/item/";
            String imageName = "image" + i + ".jpg";

            MockMultipartFile multipartFile = new MockMultipartFile(path, imageName, "image/png", new byte[] { 1, 2, 3, 4 });

            multipartFileList.add(multipartFile);
        }

        return multipartFileList;
    }

    public Long createItem() {
        ItemFormDto itemFormDto = new ItemFormDto();
        itemFormDto.setItemNm("테스트 상품");
        itemFormDto.setPrice(10000);
        itemFormDto.setShippingFee(1000);
        itemFormDto.setStockNumber(100);
        itemFormDto.setItemDetail("테스트 상품 상세 설명");
        itemFormDto.setItemSellStatus(ItemSellStatus.SELL);

        try {
            List<MultipartFile> multipartFileList = createMultipartFiles();

            return itemService.saveItem(itemFormDto, multipartFileList);
        } catch(Exception e) {
            return null;
        }
    }

    public ReverseAuction createReverseAuction() {
        Long itemId = this.createItem();

        ReverseAuctionFormDto reverseAuctionFormDto = new ReverseAuctionFormDto();
        reverseAuctionFormDto.setStartTime(LocalDateTime.now().minusHours(51));
        reverseAuctionFormDto.setPriceUnit(1000);
        reverseAuctionFormDto.setTimeUnit(1);
        reverseAuctionFormDto.setMaxRate(50);
        reverseAuctionFormDto.setItemId(itemId);

        return reverseAuctionService.saveReverseAuction(reverseAuctionFormDto);
    }

    public DiscountDto createDiscountDto(ReverseAuction reverseAuction) {
        return new DiscountDto(reverseAuction.getStartTime()
                , reverseAuction.getItem().getPrice()
                , reverseAuction.getTimeUnit()
                , reverseAuction.getPriceUnit());
    }

    @Test
    @DisplayName("역경매 등록 테스트")
    public void createReverseAuctionTest() {
        ReverseAuction reverseAuction = this.createReverseAuction();

        assertNotNull(reverseAuction);
    }

    @Test
    @DisplayName("역경매 기본 조회 테스트")
    public void getReverseAuctionDtlTest() {
        ReverseAuction reverseAuction = this.createReverseAuction();
        ReverseAuctionDto reverseAuctionDto = reverseAuctionService.getReverseAuctionDtl(reverseAuction.getId());
        DiscountDto discountDto = this.createDiscountDto(reverseAuction);

        assertEquals(reverseAuctionDto.getId(), reverseAuction.getId());
        assertEquals(reverseAuctionDto.getTimeUnit(), reverseAuction.getTimeUnit());
        assertEquals(reverseAuctionDto.getPriceUnit(), reverseAuction.getPriceUnit());
        assertEquals(reverseAuctionDto.getMaxRate(), reverseAuction.getMaxRate());
        assertEquals(reverseAuctionDto.getStartTime(), reverseAuction.getStartTime());
        assertEquals(reverseAuctionDto.getItemNm(), reverseAuction.getItem().getItemNm());
        assertEquals(reverseAuctionDto.getItemShippingFee(), reverseAuction.getItem().getShippingFee());
        assertEquals(reverseAuctionDto.getDiscountDto().getCurrentDiscountPrice(), discountDto.getCurrentDiscountPrice());
        assertEquals(reverseAuctionDto.getDiscountDto().getCurrentDiscountRate(), discountDto.getCurrentDiscountRate());
        assertEquals(reverseAuctionDto.getDiscountDto().getCurrentPrice(), discountDto.getCurrentPrice());
        assertNotNull(reverseAuctionDto.getImgUrl());
    }

    @Test
    @DisplayName("역경매 수정 테스트")
    public void updateReverseAuctionTest() {
        ReverseAuction reverseAuction = this.createReverseAuction();

        Long newItemId = this.createItem();

        ReverseAuctionFormDto reverseAuctionFormDto = reverseAuctionFormMapper.toDto(reverseAuction);
        reverseAuctionFormDto.setItemId(newItemId);
        reverseAuctionFormDto.setMaxRate(reverseAuction.getMaxRate() + 1);
        reverseAuctionFormDto.setPriceUnit(reverseAuction.getPriceUnit() + 1);
        reverseAuctionFormDto.setTimeUnit(reverseAuction.getTimeUnit() + 1);

        Long reverseAuctionId = reverseAuctionService.updateReserveAuction(reverseAuctionFormDto);

        ReverseAuctionDto reverseAuctionDto = reverseAuctionService.getReverseAuctionDtl(reverseAuctionId);

        assertEquals(reverseAuctionDto.getItemNm(), reverseAuction.getItem().getItemNm());
        assertEquals(reverseAuctionDto.getMaxRate(), reverseAuction.getMaxRate());
        assertEquals(reverseAuctionDto.getPriceUnit(), reverseAuction.getPriceUnit());
        assertEquals(reverseAuctionDto.getTimeUnit(), reverseAuction.getTimeUnit());
    }

    @Test
    @DisplayName("역경매 삭제 테스트")
    public void deleteReverseAuctionTest() {
        ReverseAuction reverseAuction = this.createReverseAuction();

        reverseAuctionService.deleteReverseAuction(reverseAuction.getId());

        ReverseAuctionDto reverseAuctionDto = reverseAuctionService.getReverseAuctionDtl(reverseAuction.getId());

        assertNull(reverseAuctionDto);
    }

    @Test
    @DisplayName("사용자 역경매 조회 테스트")
    public void getUserReverseAuctionPageTest() throws InterruptedException {
        List<ReverseAuction> reverseAuctionList = new ArrayList<>();

        int reverseAuctionCount = 10;
        int pageSize = 5;

        for(int i = 0; i < reverseAuctionCount; i++) {
            reverseAuctionList.add(this.createReverseAuction());

            Thread.sleep(10);
        }

        ReverseAuctionSearchDto reverseAuctionSearchDto = new ReverseAuctionSearchDto();
        reverseAuctionSearchDto.setSortColumn(ReverseAuctionSearchSortColumn.REG_TIME);
        reverseAuctionSearchDto.setSortDirection(Sort.Direction.DESC);

        Pageable pageable = PageRequest.of(0, pageSize);

        Page<ReverseAuctionDto> reverseAuctionDtoPage = reverseAuctionService.getUserReverseAuctionPage(reverseAuctionSearchDto, pageable);
        List<ReverseAuctionDto> reverseAuctionDtoList = reverseAuctionDtoPage.getContent();

        assertEquals(reverseAuctionDtoPage.getTotalElements(), reverseAuctionCount);
        assertEquals(reverseAuctionDtoPage.getTotalPages(), reverseAuctionCount / pageSize);

        for(int j = 0; j < pageSize; j++) {
            ReverseAuction reverseAuction = reverseAuctionList.get(reverseAuctionCount - j - 1);
            ReverseAuctionDto reverseAuctionDto = reverseAuctionDtoList.get(j);

            assertEquals(reverseAuctionDto.getId(), reverseAuction.getId());
        }
    }

    @Test
    @DisplayName("관리자 역경매 조회 테스트")
    public void getAdminReverseAuctionPageTest() throws InterruptedException {
        List<ReverseAuction> reverseAuctionList = new ArrayList<>();

        int reverseAuctionCount = 10;
        int pageSize = 5;

        for(int i = 0; i < reverseAuctionCount; i++) {
            reverseAuctionList.add(this.createReverseAuction());

            Thread.sleep(10);
        }

        ReverseAuctionSearchDto reverseAuctionSearchDto = new ReverseAuctionSearchDto();
        reverseAuctionSearchDto.setSortColumn(ReverseAuctionSearchSortColumn.REG_TIME);
        reverseAuctionSearchDto.setSortDirection(Sort.Direction.DESC);

        Pageable pageable = PageRequest.of(0, pageSize);

        Page<ReverseAuctionHistoryDto> reverseAuctionHistoryDtoPage = reverseAuctionService.getAdminReverseAuctionPage(reverseAuctionSearchDto, pageable);
        List<ReverseAuctionHistoryDto> reverseAuctionHistoryDtoList = reverseAuctionHistoryDtoPage.getContent();

        assertEquals(reverseAuctionHistoryDtoPage.getTotalElements(), reverseAuctionCount);
        assertEquals(reverseAuctionHistoryDtoPage.getTotalPages(), reverseAuctionCount / pageSize);

        for(int j = 0; j < pageSize; j++) {
            ReverseAuction reverseAuction = reverseAuctionList.get(reverseAuctionCount - j - 1);
            ReverseAuctionHistoryDto reverseAuctionDto = reverseAuctionHistoryDtoList.get(j);

            assertEquals(reverseAuctionDto.getId(), reverseAuction.getId());
        }
    }

}