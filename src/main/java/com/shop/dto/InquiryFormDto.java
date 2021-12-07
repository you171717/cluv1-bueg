package com.shop.dto;

import com.shop.entity.BaseEntity;
import com.shop.entity.Inquiry;
import lombok.*;
import org.modelmapper.ModelMapper;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class InquiryFormDto extends BaseEntity {

    private Long id;

    @NotBlank(message = "제목은 필수 입력 값입니다.")
    private String title;

    @NotNull(message = "내용은 필수 입력 값입니다.")
    private String content;

    private LocalDateTime regTime;

    private LocalDateTime updateTime;

    private String createdBy;

    private String modifiedBy;

    private static ModelMapper modelMapper = new ModelMapper();

    public Inquiry toEntity() {
        Inquiry inquiry = new Inquiry();
        inquiry.setId(id);
        inquiry.setTitle(title);
        inquiry.setContent(content);

        return inquiry;
    }

    public static InquiryFormDto of(Inquiry inquiry) {
        return modelMapper.map(inquiry, InquiryFormDto.class);
    }

}




