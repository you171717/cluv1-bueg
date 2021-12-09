package com.shop.service;

import com.shop.dto.BestItemDto;
import com.shop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class BestItemService {

    private final ItemRepository itemRepository;

    public List<BestItemDto> getBestOfDayItem() {
        return itemRepository.getBestOfDayItem();
    }

    public List<BestItemDto> getBestOfWeekItem() {
        return itemRepository.getBestOfWeekItem();
    }

    public List<BestItemDto> getBestOfMonthItem() {
        return itemRepository.getBestOfMonthItem();
    }

}
