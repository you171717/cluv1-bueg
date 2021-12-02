package com.shop.dto;

import com.shop.entity.ReviewImg;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;

@Getter @Setter
public class ReviewImgDto {

    private Long id;

    private String reviewImgName;

    private String reviewOriImgName;

    private String reviewImgUrl;

    private static ModelMapper modelMapper = new ModelMapper();

    public static ReviewImgDto of(ReviewImg reviewImg){
        return modelMapper.map(reviewImg, ReviewImgDto.class);
    }
}
