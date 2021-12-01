package com.shop.mapstruct;

import com.shop.dto.BidFormDto;
import com.shop.entity.Bid;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface BidFormMapper extends GenericMapper<BidFormDto, Bid> {

}
