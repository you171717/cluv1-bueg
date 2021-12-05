package com.shop.service;

import com.shop.dto.BestItemDto;
import com.shop.mapper.BestItemMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Log
@Service
@Transactional
@RequiredArgsConstructor
public class BestItemService {

    private final BestItemMapper myBatisBestItemMapper;

    public List<BestItemDto> getBestItemByDays() {
        return myBatisBestItemMapper.getBestItemByDays();
    }

    public List<BestItemDto> getBestItemByWeek() {
        return myBatisBestItemMapper.getBestItemByWeek();
    }

    public List<BestItemDto> getBestItemByMonth() {
        return myBatisBestItemMapper.getBestItemByMonth();
    }
}
