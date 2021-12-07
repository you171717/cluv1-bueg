package com.shop.controller;

import com.shop.dto.CommentFormDto;
import com.shop.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    /*
    파라미터로 받은 아이디에 해당하는 문의 사항에 답글 다는 기능
    */
    @PostMapping(value = "/admin/cscenter/voclist/{id}")
    public String saveComment(@PathVariable("id")Long inquiryid,@Valid CommentFormDto commentFormDto, BindingResult bindingResult, Model model){
        commentService.saveComment(commentFormDto, inquiryid);
        return "redirect:/admin/cscenter/voclist/{id}";
    }
}
