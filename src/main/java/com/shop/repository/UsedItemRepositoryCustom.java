package com.shop.repository;

import com.shop.dto.ItemSearchDto;
import com.shop.dto.UsedItemDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UsedItemRepositoryCustom {

    Page<UsedItemDto> getUsedItemPage(ItemSearchDto itemSearchDto, Pageable pageable);

}
