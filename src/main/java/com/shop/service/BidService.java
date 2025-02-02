package com.shop.service;

import com.shop.constant.BidDepositType;
import com.shop.dto.BidDto;
import com.shop.dto.BidSearchDto;
import com.shop.dto.DiscountDto;
import com.shop.entity.Bid;
import com.shop.entity.Member;
import com.shop.entity.ReverseAuction;
import com.shop.repository.BidRepository;
import com.shop.repository.MemberRepository;
import com.shop.repository.ReverseAuctionRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;

@Service
@Transactional
@RequiredArgsConstructor
public class BidService {

    private static final Logger logger = LoggerFactory.getLogger(BidService.class);

    private final BidRepository bidRepository;
    private final MemberRepository memberRepository;
    private final ReverseAuctionRepository reverseAuctionRepository;
    private final KakaoPaymentService kakaoPaymentService;
    private final EncryptionService encryptionService;

    public String getUniqueDepositName(Member member) {
        try {
            String uniqueKey = member.getName() + member.getEmail();
            String uniqueSequence = encryptionService.encrypt(uniqueKey, "SHA-256").substring(0, 5);

            return member.getName() + uniqueSequence;
        } catch(Exception e) {
            logger.error(e.getMessage(), e);

            return null;
        }
    }

    public Bid saveBid(String email, Long reverseAuctionId) {
        Member member = memberRepository.findByEmail(email);

        ReverseAuction reverseAuction = reverseAuctionRepository.findById(reverseAuctionId).orElseThrow(EntityNotFoundException::new);

        DiscountDto discountDto = new DiscountDto(reverseAuction.getStartTime(), reverseAuction.getItem().getPrice(), reverseAuction.getTimeUnit(), reverseAuction.getPriceUnit());

        Bid bid = new Bid();
        bid.setDepositAmount(discountDto.getCurrentPrice() + reverseAuction.getItem().getShippingFee());
        bid.setDepositName(this.getUniqueDepositName(member));
        bid.setMember(member);
        bid.setReverseAuction(reverseAuction);
        bid.setDepositType(BidDepositType.TRANSFER);

        return bidRepository.save(bid);
    }

    public Bid saveBidWithPayment(String email, Long reverseAuctionId, Long payId) {
        Bid bid = this.saveBid(email, reverseAuctionId);
        bid.setDepositType(BidDepositType.KAKAO_PAY);
        bid.setApprovedYn("Y");
        bid.setApprovedTime(LocalDateTime.now());

        kakaoPaymentService.savePaymentHistory(bid.getId(), payId, bid.getDepositAmount());

        return bid;
    }

    public Long approveBid(Long id) {
        Bid bid = bidRepository.findById(id).orElseThrow(EntityNotFoundException::new);

        Bid approvedBid = bidRepository.findByReverseAuctionAndApprovedYn(bid.getReverseAuction(), "Y");

        if(approvedBid != null) {
            throw new IllegalStateException("이미 낙찰된 경매입니다.");
        }

        bid.setApprovedYn("Y");
        bid.setApprovedTime(LocalDateTime.now());

        return bid.getId();
    }

    @Transactional(readOnly = true)
    public Page<BidDto> getAdminBidPage(BidSearchDto bidSearchDto, Pageable pageable) {
        return bidRepository.getAdminBidPage(bidSearchDto, pageable);
    }

    @Transactional(readOnly = true)
    public Page<BidDto> getUserBidPage(String email, BidSearchDto bidSearchDto, Pageable pageable) {
        Member member = memberRepository.findByEmail(email);

        return bidRepository.getUserBidPage(member, bidSearchDto, pageable);
    }

}
