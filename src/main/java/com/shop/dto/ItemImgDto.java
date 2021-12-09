package com.shop.dto;

import com.shop.entity.ItemImg;
import com.shop.mapstruct.ItemImgMapper;
import com.shop.mapstruct.ItemImgMapperImpl;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ItemImgDto {

    private Long id;

    private String imgName;

    private String oriImgName;

    private String imgUrl;

    private String repImgYn;

    public static ItemImgDto of(ItemImg itemImg) {
        return ItemImgMapper.INSTANCE.toDto(itemImg);
    }

}
