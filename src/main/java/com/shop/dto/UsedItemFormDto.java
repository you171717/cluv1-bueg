package com.shop.dto;

import com.shop.constant.ItemSellStatus;
import com.shop.entity.Item;
import com.shop.entity.UsedItem;
import org.modelmapper.ModelMapper;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UsedItemFormDto {
    private Long id;

    @NotBlank(message = "상품명은 필수 입력 값입니다.")
    private String itemNm;

    @NotNull(message = "가격은 필수 입력 값입니다.")
    private Integer price;

    @NotBlank(message = "이름은 필수 입력 값입니다.")
    private String itemDetail;

    @NotNull(message = "재고는 필수 입력 값입니다.")
    private Integer stockNumber;

    private ItemSellStatus itemSellStatus;

    private Date start_day;                         // 게시글 작성 날

    private Date end_day;                         // 게시글 끝 날

    private List<ItemImgDto> itemImgDtoList = new ArrayList<>();

    private List<Long> itemImgIds = new ArrayList<>();

    private static ModelMapper modelMapper = new ModelMapper();

    public UsedItem createItem() {
        return modelMapper.map(this, UsedItem.class);
    }

    public static UsedItemFormDto of(Item item){
        return modelMapper.map(item,UsedItemFormDto.class);
    }
}
