package com.shop.repository;

import com.shop.dto.BidDto;
import com.shop.dto.BidSearchDto;
import com.shop.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BidRepositoryCustom {

    Page<BidDto> getAdminBidPage(BidSearchDto reverseAuctionSearchDto, Pageable pageable);

    Page<BidDto> getUserBidPage(Member member, BidSearchDto reverseAuctionSearchDto, Pageable pageable);

}
