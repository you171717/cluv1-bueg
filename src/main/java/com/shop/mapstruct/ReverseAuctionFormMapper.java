package com.shop.mapstruct;

import com.shop.dto.ReverseAuctionFormDto;
import com.shop.entity.ReverseAuction;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface ReverseAuctionFormMapper extends GenericMapper<ReverseAuctionFormDto, ReverseAuction> {

}