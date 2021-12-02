package com.shop.dto;

import com.querydsl.core.annotations.QueryProjection;
import com.shop.entity.FAQ;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class FAQDto {
    private Long id;
    private String question;
    private String answer;

    @Builder
    @QueryProjection
    public FAQDto(Long id, String question, String answer) {
        this.id = id;
        this.question = question;
        this.answer = answer;
    }



}
