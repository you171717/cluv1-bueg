package com.shop.dto;

import com.shop.entity.UsedItemImg;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

@Getter
@Setter
public class UsedItemImgDto {

    private Long id;

    private String imgName;

    private String oriImgName;

    private String imgUrl;

    private String repImgYn;

    private static ModelMapper modelMapper = new ModelMapper();

    public static UsedItemImgDto of(UsedItemImg usedItemImg){
        return modelMapper.map(usedItemImg,UsedItemImgDto.class);
    }
}

