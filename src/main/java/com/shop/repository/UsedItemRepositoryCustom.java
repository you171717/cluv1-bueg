package com.shop.repository;

import com.shop.dto.UsedItemDto;
import com.shop.dto.UsedItemSearchDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UsedItemRepositoryCustom {

    Page<UsedItemDto> getAllUsedItemPage(UsedItemSearchDto usedItemSearchDto, Pageable pageable);

    Page<UsedItemDto> getUserUsedItemPage(String email, UsedItemSearchDto usedItemSearchDto, Pageable pageable);

}
