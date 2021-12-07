package com.shop.entity;

import com.shop.dto.CommentFormDto;
import lombok.*;

import javax.persistence.*;


@Entity
@Table(name="comment")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Comment extends BaseEntity {

    @Id
    @Column(name="comment_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(name = "inquiry_id")
    private Inquiry inquiry;

    private String content;


    public static Comment createComment(CommentFormDto commentFormDto, Inquiry inquiry){
        Comment comment=new Comment();
        comment.setContent(commentFormDto.getContent());
        comment.setInquiry(inquiry);
        return comment;
    }

    @Builder
    public Comment(Long id, String content, Inquiry inquiry) {
        this.id=id;
        this.content = content;
        this.inquiry=inquiry;
    }

}
