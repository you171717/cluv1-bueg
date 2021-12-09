package com.shop.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.shop.constant.UsedItemSellStatus;
import com.shop.entity.Member;
import com.shop.entity.UsedItem;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.modelmapper.ModelMapper;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
public class UsedItemFormDto {
    private Long id;

    @NotBlank(message = "상품명은 필수 입력 값입니다.")
    private String name;

    @NotNull(message = "가격은 필수 입력 값입니다.")
    private Integer price;

    @NotBlank(message = "이름은 필수 입력 값입니다.")
    private String detail;

    @NotNull(message = "판매 상태는 필수 입력 값입니다.")
    private UsedItemSellStatus usedItemSellStatus;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime endTime; // 종료일

    private List<UsedItemImgDto> usedItemImgDtoList = new ArrayList<>();

    private List<Long> usedItemImgIds = new ArrayList<>();

    private static ModelMapper modelMapper = new ModelMapper();

    public UsedItem createItem(Member member) {
        UsedItem usedItem = modelMapper.map(this, UsedItem.class);
        usedItem.setOwner(member);

        return usedItem;
    }

    public static UsedItemFormDto of(UsedItem usedItem){
        return modelMapper.map(usedItem,UsedItemFormDto.class);
    }

}
