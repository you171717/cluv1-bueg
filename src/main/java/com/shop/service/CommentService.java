package com.shop.service;

import com.shop.dto.CommentFormDto;
import com.shop.entity.Comment;
import com.shop.entity.Inquiry;
import com.shop.repository.CommentRepository;
import com.shop.repository.InquiryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final InquiryRepository inquiryRepository;

    public Comment saveComment(CommentFormDto commentFormDto, Long inquiryId) {
        Inquiry inquiry = inquiryRepository.findById(inquiryId).orElseThrow(EntityNotFoundException::new);

        Comment comment = Comment.createComment(commentFormDto, inquiry);

        return commentRepository.save(comment);
    }

    @Transactional(readOnly = true)
    public List<CommentFormDto> getCommentList(Long inquiryId) {
        List<Comment> commentList = commentRepository.findByInquiryId(inquiryId);
        List<CommentFormDto> commentDtoList = new ArrayList<>();

        for(Comment comment : commentList) {
            CommentFormDto commentFormDto = new CommentFormDto();
            commentFormDto.setId(comment.getId());
            commentFormDto.setContent(comment.getContent());
            commentFormDto.setCreatedBy(comment.getCreatedBy());
            commentFormDto.setRegTime(comment.getRegTime());

            commentDtoList.add(commentFormDto);
        }

        return commentDtoList;
    }

    public void deleteComment(Long inquiryId){
        commentRepository.deleteByInquiryId(inquiryId);
    }

}
