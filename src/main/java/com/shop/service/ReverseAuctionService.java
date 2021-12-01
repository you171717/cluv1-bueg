package com.shop.service;

import com.shop.dto.ReverseAuctionDto;
import com.shop.dto.ReverseAuctionFormDto;
import com.shop.dto.ReverseAuctionHistoryDto;
import com.shop.dto.ReverseAuctionSearchDto;
import com.shop.entity.Item;
import com.shop.entity.ReverseAuction;
import com.shop.mapstruct.ReverseAuctionFormMapper;
import com.shop.repository.ItemRepository;
import com.shop.repository.ReverseAuctionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ReverseAuctionService {

    private final ItemRepository itemRepository;

    private final ReverseAuctionFormMapper reverseAuctionFormMapper;

    private final ReverseAuctionRepository reverseAuctionRepository;

    public ReverseAuction saveReverseAuction(ReverseAuctionFormDto reverseAuctionFormDto) {
        Item item = itemRepository.findById(reverseAuctionFormDto.getItemId()).orElseThrow(EntityNotFoundException::new);

        ReverseAuction reverseAuction = reverseAuctionFormMapper.toEntity(reverseAuctionFormDto);

        reverseAuction.setItem(item);
        reverseAuction.setStartTime(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES));

        return reverseAuctionRepository.save(reverseAuction);
    }

    @Transactional(readOnly = true)
    public ReverseAuctionFormDto getReserveAuctionForm(Long id) {
        ReverseAuction reverseAuction = reverseAuctionRepository.findById(id).orElseThrow(EntityNotFoundException::new);

        ReverseAuctionFormDto reverseAuctionFormDto = reverseAuctionFormMapper.toDto(reverseAuction);
        reverseAuctionFormDto.setItemId(reverseAuction.getItem().getId());

        return reverseAuctionFormDto;
    }

    @Transactional(readOnly = true)
    public ReverseAuctionDto getReverseAuctionDtl(Long id) {
        return reverseAuctionRepository.getUserReverseAuctionDetailPage(id);
    }

    public Long updateReserveAuction(ReverseAuctionFormDto reverseAuctionFormDto) {
        Item item = itemRepository.findById(reverseAuctionFormDto.getItemId()).orElseThrow(EntityNotFoundException::new);

        ReverseAuction reverseAuction = reverseAuctionRepository.findById(reverseAuctionFormDto.getId()).orElseThrow(EntityNotFoundException::new);

        reverseAuction.setItem(item);

        reverseAuctionFormMapper.updateFromDto(reverseAuctionFormDto, reverseAuction);

        return reverseAuction.getId();
    }

    public void deleteReverseAuction(Long id) {
        ReverseAuction reverseAuction = reverseAuctionRepository.findById(id).orElseThrow(EntityNotFoundException::new);

        reverseAuctionRepository.delete(reverseAuction);
    }

    @Transactional(readOnly = true)
    public Page<ReverseAuctionHistoryDto> getAdminReverseAuctionPage(ReverseAuctionSearchDto reverseAuctionSearchDto, Pageable pageable) {
        return reverseAuctionRepository.getAdminReverseAuctionPage(reverseAuctionSearchDto, pageable);
    }

    @Transactional(readOnly = true)
    public Page<ReverseAuctionDto> getUserReverseAuctionPage(ReverseAuctionSearchDto reverseAuctionSearchDto, Pageable pageable) {
        return reverseAuctionRepository.getUserReverseAuctionPage(reverseAuctionSearchDto, pageable);
    }

    @Transactional(readOnly = true)
    public List<ReverseAuctionHistoryDto> getPreviousReverseAuctionPage() {
        return reverseAuctionRepository.getPreviousReverseAuctionPage();
    }

}
