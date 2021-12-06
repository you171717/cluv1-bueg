package com.shop.dto;

import com.shop.constant.ItemSellStatus;
import com.shop.constant.UsedItemSellStatus;
import com.shop.entity.UsedItem;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter @Setter
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

    private int shippingFee;

    private UsedItemSellStatus usedItemSellStatus;

    private LocalDateTime startDay;                         // 게시글 작성 날

    private LocalDateTime endDay;                         // 게시글 끝 날

    private List<UsedItemImgDto> usedItemImgDtoList = new ArrayList<>();

    private List<Long> itemImgIds = new ArrayList<>();

    private static ModelMapper modelMapper = new ModelMapper();

    public UsedItem createItem() {
        return modelMapper.map(this, UsedItem.class);
    }

    public static UsedItemFormDto of(UsedItem usedItem){
        return modelMapper.map(usedItem,UsedItemFormDto.class);
    }

    @Override
    public String toString() {
        return "UsedItemFormDto{" +
                "id=" + id +
                ", itemNm='" + itemNm + '\'' +
                ", price=" + price +
                ", itemDetail='" + itemDetail + '\'' +
                ", stockNumber=" + stockNumber +
                ", shippingFee=" + shippingFee +
                ", usedItemSellStatus=" + usedItemSellStatus +
                ", startDay=" + startDay +
                ", endDay=" + endDay +
                ", usedItemImgDtoList=" + usedItemImgDtoList +
                ", itemImgIds=" + itemImgIds +
                '}';
    }
}
