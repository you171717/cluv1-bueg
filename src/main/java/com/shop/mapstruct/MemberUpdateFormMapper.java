package com.shop.mapstruct;

import com.shop.dto.MemberUpdateFormDto;
import com.shop.entity.Member;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface MemberUpdateFormMapper extends GenericMapper<MemberUpdateFormDto, Member> {

}
