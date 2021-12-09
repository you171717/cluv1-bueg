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

    @PostMapping(value = "/admin/cscenter/voclist/{id}")
    public String saveComment(@PathVariable("id") Long inquiryId, @Valid CommentFormDto commentFormDto, BindingResult bindingResult, Model model) {
        commentService.saveComment(commentFormDto, inquiryId);

        return "redirect:/admin/cscenter/voclist/{id}";
    }

}
