package com.shop.dto;

import com.shop.entity.OrderItem;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@Getter @Setter
public class ReviewFormDto {

    private Long reviewId;

    @NotBlank(message = "상품 후기는 필수 입력 값입니다.")
    private String comment;

    private List<ReviewImgDto> reviewImgDtoList = new ArrayList<>();

    private List<Long> reviewImgIds = new ArrayList<>();

    private static ModelMapper modelMapper = new ModelMapper();

    public static ReviewFormDto of(OrderItem orderItem){
        return modelMapper.map(orderItem, ReviewFormDto.class);
    }
}
