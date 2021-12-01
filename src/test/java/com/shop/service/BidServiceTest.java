package com.shop.service;

import com.shop.constant.BidSearchSortColumn;
import com.shop.constant.ItemSellStatus;
import com.shop.dto.*;
import com.shop.entity.Bid;
import com.shop.entity.Member;
import com.shop.entity.ReverseAuction;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.password.PasswordEncoder;
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
class BidServiceTest {

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    MemberService memberService;

    @Autowired
    ItemService itemService;

    @Autowired
    ReverseAuctionService reverseAuctionService;

    @Autowired
    BidService bidService;

    public Member createMember() {
        return this.createMember(null);
    }

    public Member createMember(String email) {
        MemberFormDto memberFormDto = new MemberFormDto();
        memberFormDto.setEmail(email == null ? "test@email.com" : email);
        memberFormDto.setName("홍길동");
        memberFormDto.setAddress("서울시 마포구 합정동");
        memberFormDto.setPassword("12341234");

        Member member = Member.createMember(memberFormDto, passwordEncoder);

        return memberService.saveMember(member);
    }

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

    public Bid createBid(ReverseAuction reverseAuction) {
        Member member = this.createMember();

        return bidService.saveBid(member.getEmail(), reverseAuction.getId());
    }

    public DiscountDto createDiscountDto(ReverseAuction reverseAuction) {
        return new DiscountDto(reverseAuction.getStartTime()
                , reverseAuction.getItem().getPrice()
                , reverseAuction.getTimeUnit()
                , reverseAuction.getPriceUnit());
    }

    @Test
    @DisplayName("입찰 등록 테스트")
    public void createBidTest() {
        ReverseAuction reverseAuction = this.createReverseAuction();

        Bid bid = this.createBid(reverseAuction);

        assertNotNull(bid);
    }

    @Test
    @DisplayName("입찰 확인 테스트")
    public void approveBidTest() {
        ReverseAuction reverseAuction = this.createReverseAuction();

        Bid bid = this.createBid(reverseAuction);

        bidService.approveBid(bid.getId());

        assertEquals(bid.getApprovedYn(), "Y");
        assertNotNull(bid.getApprovedTime());
    }

    @Test
    @DisplayName("입찰(카카오 페이) 테스트")
    public void approveBidWithPaymentTest() {
        Member member = this.createMember();

        ReverseAuction reverseAuction = this.createReverseAuction();

        Bid bid = bidService.saveBidWithPayment(member.getEmail(), reverseAuction.getId(), 1L);

        assertEquals(bid.getApprovedYn(), "Y");
        assertNotNull(bid.getApprovedTime());
    }

    @Test
    @DisplayName("입찰 동시 확인 예외 처리 테스트")
    public void approveBidExceptionTest() {
        Member member = this.createMember();

        ReverseAuction reverseAuction = this.createReverseAuction();

        Bid bid = bidService.saveBid(member.getEmail(), reverseAuction.getId());
        Bid bid2 = bidService.saveBid(member.getEmail(), reverseAuction.getId());

        bidService.approveBid(bid.getId());

        try {
            bidService.approveBid(bid2.getId());

            fail();
        } catch(Exception e) {
            assertTrue(e instanceof IllegalStateException);
        }
    }

    @Test
    @DisplayName("관리자 결제 관리 조회 테스트")
    public void getAdminPageTest() throws InterruptedException {
        List<Bid> bidList = new ArrayList<>();

        ReverseAuction reverseAuction = this.createReverseAuction();

        int bidCount = 10;
        int pageSize = 5;

        for(int i = 0; i < bidCount; i++) {
            Member member = this.createMember("test" + i + "@test.com");

            Bid bid = bidService.saveBid(member.getEmail(), reverseAuction.getId());

            bidList.add(bid);

            Thread.sleep(10);
        }

        BidSearchDto bidSearchDto = new BidSearchDto();
        bidSearchDto.setSortColumn(BidSearchSortColumn.REG_TIME);
        bidSearchDto.setSortDirection(Sort.Direction.DESC);

        Pageable pageable = PageRequest.of(0, pageSize);

        Page<BidDto> bidDtoPage = bidService.getAdminBidPage(bidSearchDto, pageable);
        List<BidDto> bidDtoList = bidDtoPage.getContent();

        assertEquals(bidDtoPage.getTotalElements(), bidCount);
        assertEquals(bidDtoPage.getTotalPages(), bidCount / pageSize);

        for(int j = 0; j < pageSize; j++) {
            Bid bid = bidList.get(bidCount - j - 1);
            BidDto bidDto = bidDtoList.get(j);

            assertEquals(bidDto.getId(), bid.getId());
        }
    }

    @Test
    @DisplayName("사용자 참여 내역 조회 테스트")
    public void getUserPageTest() throws InterruptedException {
        List<Bid> bidList = new ArrayList<>();

        Member member = this.createMember();

        ReverseAuction reverseAuction = this.createReverseAuction();

        int bidCount = 10;
        int pageSize = 5;

        for(int i = 0; i < bidCount; i++) {
            Bid bid = bidService.saveBid(member.getEmail(), reverseAuction.getId());

            bidList.add(bid);

            Thread.sleep(10);
        }

        BidSearchDto bidSearchDto = new BidSearchDto();
        bidSearchDto.setSortColumn(BidSearchSortColumn.REG_TIME);
        bidSearchDto.setSortDirection(Sort.Direction.DESC);

        Pageable pageable = PageRequest.of(0, pageSize);

        Page<BidDto> bidDtoPage = bidService.getUserBidPage(member.getEmail(), bidSearchDto, pageable);
        List<BidDto> bidDtoList = bidDtoPage.getContent();

        assertEquals(bidDtoPage.getTotalElements(), bidCount);
        assertEquals(bidDtoPage.getTotalPages(), bidCount / pageSize);

        for(int j = 0; j < pageSize; j++) {
            Bid bid = bidList.get(bidCount - j - 1);
            BidDto bidDto = bidDtoList.get(j);

            assertEquals(bidDto.getId(), bid.getId());
        }
    }

    @Test
    @DisplayName("관리자 결제 관리 조회(낙찰 상태) 테스트")
    public void getAdminPageApprovedTest() {
        ReverseAuction reverseAuction = this.createReverseAuction();

        Bid bid = this.createBid(reverseAuction);

        bidService.approveBid(bid.getId());

        BidSearchDto bidSearchDto = new BidSearchDto();
        Pageable pageable = PageRequest.of(0, 1);

        Page<BidDto> bidDtoPage = bidService.getAdminBidPage(bidSearchDto, pageable);
        List<BidDto> bidDtoList = bidDtoPage.getContent();

        BidDto bidDto = bidDtoList.get(0);

        assertEquals(bidDto.getApprovedYn(), "Y");
    }

    @Test
    @DisplayName("관리자 결제 관리 조회(실패 상태) 테스트")
    public void getAdminPageFailedTest() throws InterruptedException {
        Member member = this.createMember("test1@test.com");
        Member member2 = this.createMember("test2@test.com");

        ReverseAuction reverseAuction = this.createReverseAuction();

        bidService.saveBid(member.getEmail(), reverseAuction.getId());

        Thread.sleep(10);

        bidService.saveBidWithPayment(member2.getEmail(), reverseAuction.getId(), 1L);

        BidSearchDto bidSearchDto = new BidSearchDto();
        bidSearchDto.setSortColumn(BidSearchSortColumn.REG_TIME);
        bidSearchDto.setSortDirection(Sort.Direction.ASC);

        Pageable pageable = PageRequest.of(0, 2);

        Page<BidDto> bidDtoPage = bidService.getAdminBidPage(bidSearchDto, pageable);
        List<BidDto> bidDtoList = bidDtoPage.getContent();

        BidDto bidDto = bidDtoList.get(0);
        BidDto bid2Dto = bidDtoList.get(1);

        assertEquals(bidDto.getApprovedYn(), "F");
        assertEquals(bid2Dto.getApprovedYn(), "Y");
    }

}