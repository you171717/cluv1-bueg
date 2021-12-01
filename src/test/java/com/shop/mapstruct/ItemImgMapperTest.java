package com.shop.mapstruct;

import com.shop.dto.ItemImgDto;
import com.shop.entity.ItemImg;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ItemImgMapperTest {

    @Test
    @DisplayName("ItemImg에서 ItemImgDto로 변환 테스트")
    public void toDtoTest() {
        ItemImg itemImg = new ItemImg();
        itemImg.setImgName("img.png");
        itemImg.setImgUrl("C:/shop/item/img.png");
        itemImg.setOriImgName("ori.png");
        itemImg.setRepImgYn("Y");

        ItemImgDto itemImgDto = ItemImgDto.of(itemImg);

        assertEquals(itemImg.getImgName(), itemImgDto.getImgName());
        assertEquals(itemImg.getImgUrl(), itemImgDto.getImgUrl());
        assertEquals(itemImg.getOriImgName(), itemImgDto.getOriImgName());
        assertEquals(itemImg.getRepImgYn(), itemImgDto.getRepImgYn());
    }

}