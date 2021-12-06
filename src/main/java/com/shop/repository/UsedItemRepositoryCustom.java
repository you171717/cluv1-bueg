package com.shop.repository;

import com.shop.dto.UsedItemDto;
import com.shop.dto.UsedItemSearchDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UsedItemRepositoryCustom {

    Page<UsedItemDto> getUsedItemPage(UsedItemSearchDto usedItemSearchDto, Pageable pageable);

}
