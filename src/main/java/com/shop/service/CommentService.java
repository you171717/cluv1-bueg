package com.shop.service;

import com.shop.dto.CommentFormDto;
import com.shop.entity.*;
import com.shop.repository.CommentRepository;
import com.shop.repository.InquiryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final InquiryRepository inquiryRepository;

    public Comment saveComment(Long id, CommentFormDto commentFormDto) {
        Inquiry inquiry = inquiryRepository.findById(id).orElseThrow(EntityNotFoundException::new);

        List<Comment> CommentList = new ArrayList<>();
        Comment comment = Comment.createComment(inquiry,commentFormDto);
        CommentList.add(comment);

        return commentRepository.save(comment);
    }
    @Transactional
    public List<CommentFormDto> getCommentList(Long id) {
        List<Comment> commentList= commentRepository.findByInquiryId(id);
        List<CommentFormDto> commentDtoList = new ArrayList<>();

        for(Comment comment : commentList) {
            CommentFormDto commentFormDto=CommentFormDto.builder()
                    .id(comment.getId())
                    .content(comment.getContent())
                    .createdBy(comment.getCreatedBy())
                    .regTime(comment.getRegTime())
                    .build();
            commentDtoList.add(commentFormDto);
        }

        return commentDtoList;
    }

    @Transactional
    public void deleteComment(Long id){
        commentRepository.deleteByInquiryId(id);
    }
}
