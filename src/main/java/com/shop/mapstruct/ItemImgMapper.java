package com.shop.mapstruct;

import com.shop.dto.ItemImgDto;
import com.shop.entity.ItemImg;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ItemImgMapper extends GenericMapper<ItemImgDto, ItemImg> {

}
