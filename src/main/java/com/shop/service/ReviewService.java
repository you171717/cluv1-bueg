package com.shop.service;

import com.shop.dto.ReviewFormDto;
import com.shop.dto.ReviewImgDto;
import com.shop.dto.ReviewItemDto;
import com.shop.entity.OrderItem;
import com.shop.entity.ReviewImg;
import com.shop.repository.OrderItemRepository;
import com.shop.repository.ReviewImgRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ReviewService {

    private final OrderItemRepository orderItemRepository;
    private final ReviewImgService reviewImgService;
    private final ReviewImgRepository reviewImgRepository;

    public Long saveReview(Long orderItemId, ReviewFormDto reviewFormDto, List<MultipartFile> reviewImgFile) throws Exception {
        OrderItem orderItem = orderItemRepository.findById(orderItemId).orElseThrow(EntityNotFoundException::new);
        orderItem.createReview(reviewFormDto);
        orderItem.setReviewYn("Y");

        for(int i = 0; i < reviewImgFile.size(); i++) {
            ReviewImg reviewImg = new ReviewImg();
            reviewImg.setOrderItem(orderItem);

            reviewImgService.saveReviewImg(reviewImg, reviewImgFile.get(i));
        }

        return orderItem.getId();
    }

    @Transactional(readOnly = true)
    public ReviewFormDto getReviewDtl(Long orderItemId) {
        List<ReviewImg> reviewImgList = reviewImgRepository.findByOrderItemIdOrderByIdAsc(orderItemId);
        List<ReviewImgDto> reviewImgDtoList = new ArrayList<>();

        for(ReviewImg reviewImg : reviewImgList){
            ReviewImgDto reviewImgDto = ReviewImgDto.of(reviewImg);
            reviewImgDtoList.add(reviewImgDto);
        }

        OrderItem orderItem = orderItemRepository.findById(orderItemId).orElseThrow(EntityNotFoundException::new);

        ReviewFormDto reviewFormDto = ReviewFormDto.of(orderItem);
        reviewFormDto.setReviewImgDtoList(reviewImgDtoList);

        return reviewFormDto;
    }

    public Long updateReview(Long orderItemId, ReviewFormDto reviewFormDto, List<MultipartFile> reviewImgFile) throws Exception {
        OrderItem orderItem = orderItemRepository.findById(orderItemId).orElseThrow(EntityNotFoundException::new);
        orderItem.updateReview(reviewFormDto);

        List<Long> reviewImgIds = reviewFormDto.getReviewImgIds();

        for(int i = 0; i < reviewImgFile.size(); i++){
            reviewImgService.updateReviewImg(reviewImgIds.get(i), reviewImgFile.get(i));
        }

        return orderItem.getId();

    }

    public Long deleteReview(Long orderItemId, ReviewFormDto reviewFormDto) {
        OrderItem orderItem = orderItemRepository.findById(orderItemId).orElseThrow(EntityNotFoundException::new);
        orderItem.deleteReview(reviewFormDto);
        orderItem.setReviewYn("N");

        return orderItem.getId();
    }

    @Transactional(readOnly = true)
    public List<ReviewItemDto> getReviewItem(Long itemId) {
        List<OrderItem> orderItems = orderItemRepository.findByItemIdAndReviewYn(itemId, "Y");
        List<ReviewItemDto> reviewItemDtoList = new ArrayList<>();

        for(OrderItem orderItem : orderItems){
            ReviewItemDto reviewItemDto = new ReviewItemDto(orderItem);
            reviewItemDto.addReviewItemDto(reviewItemDto);

            reviewItemDtoList.add(reviewItemDto);
        }

        return reviewItemDtoList;
    }

    @Transactional(readOnly = true)
    public List<ReviewImgDto> getReviewItemImg(Long itemId) {
        List<OrderItem> orderItems = orderItemRepository.findByItemIdAndReviewYn(itemId, "Y");
        List<ReviewImgDto> reviewImgDtoList = new ArrayList<>();

        for(OrderItem orderItem : orderItems){
            Long orderItemId = orderItem.getId();

            ReviewImg reviewImg = reviewImgRepository.findByOrderItemId(orderItemId);

            ReviewImgDto reviewImgDto = new ReviewImgDto();
            reviewImgDto.setId(reviewImg.getId());
            reviewImgDto.setReviewImgName(reviewImg.getReviewImgName());
            reviewImgDto.setReviewImgUrl(reviewImg.getReviewImgUrl());
            reviewImgDto.setReviewOriImgName(reviewImg.getReviewOriImgName());

            reviewImgDtoList.add(reviewImgDto);
        }

        return reviewImgDtoList;
    }

}
