package com.shop.dto;

import com.shop.entity.BaseEntity;
import com.shop.entity.Comment;
import com.shop.entity.Inquiry;
import com.shop.entity.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.criteria.CriteriaBuilder;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class CommentFormDto extends BaseEntity {
    private Long id;
    private String content;
    private Inquiry inquiry;
    private LocalDateTime regTime;
    private LocalDateTime updateTime;
    private String createdBy;
    private String modifiedBy;

    public Comment toEntity() {
        Comment build = Comment.builder()
                .id(id)
                .content(content)
                .inquiry(inquiry)
                .build();
        return build;
    }

    @Builder
    public CommentFormDto(Long id, String content, Inquiry inquiry,String createdBy,LocalDateTime regTime) {
        this.id=id;
        this.content = content;
        this.inquiry=inquiry;
        this.createdBy=createdBy;
        this.regTime=regTime;
    }


}
