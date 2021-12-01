package com.shop.dto;

import com.shop.entity.ItemImg;
import com.shop.mapstruct.ItemImgMapper;
import com.shop.mapstruct.ItemImgMapperImpl;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemImgDto {

    private Long id;

    private String imgName;

    private String oriImgName;

    private String imgUrl;

    private String repImgYn;

    /*
    private static ModelMapper modelMapper = new ModelMapper();

    public static ItemImgDto of(ItemImg itemImg) {
        return modelMapper.map(itemImg, ItemImgDto.class);
    }
    */

    private static ItemImgMapper itemImgMapper = new ItemImgMapperImpl();

    public static ItemImgDto of(ItemImg itemImg) {
        return itemImgMapper.toDto(itemImg);
    }

}
