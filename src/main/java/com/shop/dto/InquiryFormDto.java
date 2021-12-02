package com.shop.dto;

import com.shop.entity.BaseEntity;
import com.shop.entity.BaseTimeEntity;
import com.shop.entity.Inquiry;
import com.shop.entity.Item;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.modelmapper.ModelMapper;
import org.springframework.data.annotation.LastModifiedBy;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
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

    private static ModelMapper modelMapper=new ModelMapper();

    public Inquiry toEntity() {
        Inquiry build = Inquiry.builder()
                .id(id)
                .title(title)
                .content(content)
                .build();
        return build;
    }

    @Builder
    public InquiryFormDto(Long id, String title, String content,LocalDateTime regTime,String createdBy) {
        this.id=id;
        this.title = title;
        this.content = content;
        this.regTime=regTime;
        this.createdBy=createdBy;
    }

    public static InquiryFormDto of(Inquiry inquiry){
        return modelMapper.map(inquiry,InquiryFormDto.class);
    }

}




