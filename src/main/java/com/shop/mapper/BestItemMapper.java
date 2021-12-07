package com.shop.mapper;

import com.shop.dto.BestItemDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BestItemMapper {

    List<BestItemDto> getBestItemByDays();

    List<BestItemDto> getBestItemByWeek();

    List<BestItemDto> getBestItemByMonth();

}