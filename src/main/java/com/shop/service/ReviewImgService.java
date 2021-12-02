package com.shop.service;

import com.shop.dto.ReviewFormDto;
import com.shop.entity.ReviewImg;
import com.shop.repository.ReviewImgRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityNotFoundException;

@Service
@RequiredArgsConstructor
@Transactional
public class ReviewImgService {

    @Value("${reviewImgLocation}")
    private String reviewImgLocation;

    private final ReviewImgRepository reviewImgRepository;

    private final FileService fileService;

    public void saveReviewImg(ReviewImg reviewImg, MultipartFile reviewImgFile) throws Exception{
        String reviewOriImgName = reviewImgFile.getOriginalFilename();
        String reviewImgName = "";
        String reviewImgUrl = "";

        //파일 업로드
        if(!StringUtils.isEmpty(reviewOriImgName)){
            reviewImgName = fileService.uploadFile(reviewImgLocation, reviewOriImgName, reviewImgFile.getBytes());
            reviewImgUrl = "/images/review/" + reviewImgName;
        }

        //상품 이미지 정보 저장
        reviewImg.updateReviewImg(reviewOriImgName, reviewImgName, reviewImgUrl);
        reviewImgRepository.save(reviewImg);
    }

    public void updateReviewImg(Long reviewImgId, MultipartFile reviewImgFile) throws Exception{
        if(!reviewImgFile.isEmpty()){
            ReviewImg savedReviewImg = reviewImgRepository.findById(reviewImgId).orElseThrow(EntityNotFoundException::new);

            //기존 이미지 파일 삭제
            if(!StringUtils.isEmpty(savedReviewImg.getReviewImgName())){
                fileService.deleteFile(reviewImgLocation + "/" + savedReviewImg.getReviewImgName());
            }

            String reviewOriImgName = reviewImgFile.getOriginalFilename();
            String reviewImgName = fileService.uploadFile(reviewImgLocation, reviewOriImgName, reviewImgFile.getBytes());
            String reviewImgUrl = "/images/review/" + reviewImgName;
            savedReviewImg.updateReviewImg(reviewOriImgName, reviewImgName, reviewImgUrl);
        }
    }

    public void deleteReviewImg(Long orderItemId){
        ReviewImg reviewImg = reviewImgRepository.findByOrderItemId(orderItemId);
        reviewImgRepository.delete(reviewImg);
    }
}
