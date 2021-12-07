package com.shop.dto;

import com.shop.entity.BaseEntity;
import com.shop.entity.Comment;
import com.shop.entity.Inquiry;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class CommentFormDto extends BaseEntity {

    private Long id;

    private String content;

    private Inquiry inquiry;

    private LocalDateTime regTime;

    private LocalDateTime updateTime;

    private String createdBy;

    private String modifiedBy;

    public Comment toEntity() {
        Comment comment = new Comment();
        comment.setId(id);
        comment.setContent(content);
        comment.setInquiry(inquiry);

        return comment;
    }

}
