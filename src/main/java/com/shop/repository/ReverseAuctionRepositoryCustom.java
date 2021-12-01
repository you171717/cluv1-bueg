package com.shop.repository;

import com.shop.dto.ReverseAuctionDto;
import com.shop.dto.ReverseAuctionHistoryDto;
import com.shop.dto.ReverseAuctionSearchDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ReverseAuctionRepositoryCustom {

    Page<ReverseAuctionHistoryDto> getAdminReverseAuctionPage(ReverseAuctionSearchDto reverseAuctionSearchDto, Pageable pageable);

    Page<ReverseAuctionDto> getUserReverseAuctionPage(ReverseAuctionSearchDto reverseAuctionSearchDto, Pageable pageable);

    List<ReverseAuctionHistoryDto> getPreviousReverseAuctionPage();

    ReverseAuctionDto getUserReverseAuctionDetailPage(Long id);
}
