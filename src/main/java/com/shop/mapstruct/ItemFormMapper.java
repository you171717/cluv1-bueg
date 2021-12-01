package com.shop.mapstruct;

import com.shop.dto.ItemFormDto;
import com.shop.entity.Item;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ItemFormMapper extends GenericMapper<ItemFormDto, Item> {

}
