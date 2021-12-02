package com.shop.controller;

import com.shop.dto.*;
import com.shop.service.OrderService;
import com.shop.service.ReviewImgService;
import com.shop.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.Optional;


@Controller
@RequiredArgsConstructor
public class ReviewController {

    private final OrderService orderService;
    private final ReviewService reviewService;
    private final ReviewImgService reviewImgService;

    @GetMapping(value = {"/reviews", "reviews/{page}"})
    public String reviews(@PathVariable("page") Optional<Integer> page, Principal principal, Model model){

        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 4);
        Page<OrderHistDto> ordersHistDtoList = orderService.getOrderList(principal.getName(), pageable);

        model.addAttribute("orders", ordersHistDtoList);
        model.addAttribute("page", pageable.getPageNumber());
        model.addAttribute("maxPage", 5);

        return "review/reviewList";
    }

    @GetMapping("/reviews/new/{itemId}")
    public String reviewForm(@PathVariable("itemId") Long orderItemId ,Model model){
        ReviewFormDto reviewFormDto = new ReviewFormDto();
        reviewFormDto.setReviewId(orderItemId);
        model.addAttribute("reviewFormDto", reviewFormDto);
        return "review/reviewWrite";
    }

    @PostMapping("/reviews/new/{itemId}")
    public String reviewNew(@PathVariable("itemId") Long orderItemId, @Valid ReviewFormDto reviewFormDto,
                            BindingResult bindingResult, Model model, @RequestParam("reviewImgFile")List<MultipartFile> reviewImgFile) {

        if (bindingResult.hasErrors()) {
            return "review/reviewWrite";
        }

        if ( reviewImgFile.get(0).isEmpty()) {
            model.addAttribute("errorMessage", "사진 등록은 필수 입력 값입니다.");
            return "review/reviewWrite";
        }

        if( reviewFormDto.getComment().length() == 0){
            model.addAttribute("errorMessage", "리뷰 작성은 필수 입력 값입니다.");
            return "review/reviewWrite";
        }

        try{
            reviewService.saveReview(orderItemId, reviewFormDto, reviewImgFile);
        } catch (Exception e){
            model.addAttribute("errorMessage", "리뷰 작성 중 에러가 발생하였습니다.");
            return "review/reviewWrite";
        }

        return "redirect:/reviews";
    }

    @GetMapping("/reviews/update/{itemId}")
    public String reviewDtl(@PathVariable("itemId") Long orderItemId, Model model){
        try{
            ReviewFormDto reviewFormDto = reviewService.getReviewDtl(orderItemId);
            reviewFormDto.setReviewId(orderItemId);
            model.addAttribute("reviewFormDto", reviewFormDto);
        } catch(EntityNotFoundException e){
            model.addAttribute("errorMessage", "후기를 작성하지 않았습니다.");
            model.addAttribute("reviewFormDto", new ReviewFormDto());
            return "review/reviewWrite";
        }

        return "review/reviewWrite";
    }

    @PostMapping("/reviews/update/{itemId}")
    public String reviewUpdate(@PathVariable("itemId") Long orderItemId, @Valid ReviewFormDto reviewFormDto,
                               BindingResult bindingResult, @RequestParam("reviewImgFile")List<MultipartFile> reviewImgFile,Model model){
        if(bindingResult.hasErrors()){
            return "review/reviewWrite";
        }

        if(reviewFormDto.getComment() == null){
            model.addAttribute("errorMessage", "후기 작성은 필수 입력 값입니다.");
            return "review/reviewWrite";
        }

        try{
            reviewService.updateReview(orderItemId, reviewFormDto, reviewImgFile);
        }catch (Exception e){
            model.addAttribute("errorMessage", "리뷰 작성 중 에러가 발생하였습니다.");
            return "review/reviewWrite";
        }

        return "redirect:/reviews";
    }

    @GetMapping("/reviews/delete/{itemId}")
    public String reviewDelete(@PathVariable("itemId") Long orderItemId, ReviewFormDto reviewFormDto){
        reviewService.deleteReview(orderItemId, reviewFormDto);
        reviewImgService.deleteReviewImg(orderItemId);

        return "redirect:/reviews";
    }


}
