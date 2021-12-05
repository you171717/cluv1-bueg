package com.shop.service;

import com.shop.dto.ItemImgDto;
import com.shop.dto.UsedItemFormDto;
import com.shop.dto.UsedItemImgDto;
import com.shop.entity.UsedItem;
import com.shop.entity.UsedItemImg;
import com.shop.repository.UsedItemImgRepository;
import com.shop.repository.UsedItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class UsedItemService {

    private final UsedItemRepository usedItemRepository;
    private final UsedItemImgService usedItemImgService;
    private final UsedItemImgRepository usedItemImgRepository;

    public Long saveItem(UsedItemFormDto usedItemFormDto, List<MultipartFile> itemImgFileList) throws Exception {

        //상품 등록
        UsedItem usedItem = usedItemFormDto.createItem();
        usedItemRepository.save(usedItem);

        //이미지 등록
        for(int i=0;i<itemImgFileList.size();i++) {
            UsedItemImg usedItemImg = new UsedItemImg();
            usedItemImg.setUsedItem(usedItem);
            if(i == 0)
                usedItemImg.setRepimgYn("Y");
            else
                usedItemImg.setRepimgYn("N");
            usedItemImgService.saveItemImg(usedItemImg, itemImgFileList.get(i));
        }

        return usedItem.getId();
    }

    @Transactional(readOnly = true)
    public UsedItemFormDto getItemDtl(Long itemId){

        List<UsedItemImg> itemImgList = usedItemImgRepository.findByItemIdOrderByIdAsc(itemId);
        List<UsedItemImgDto> itemImgDtoList = new ArrayList<>();
        for (UsedItemImg usedItemImg : itemImgList) {
            UsedItemImgDto usedItemImgDto = UsedItemImgDto.of(usedItemImg);
            itemImgDtoList.add(usedItemImgDto);
        }

        UsedItem UsedItem = UsedItemRepository.findById(itemId)
                .orElseThrow(EntityNotFoundException::new);
        ItemFormDto itemFormDto = ItemFormDto.of(item);
        itemFormDto.setItemImgDtoList(itemImgDtoList);
        return itemFormDto;
    }

    public Long updateItem(ItemFormDto itemFormDto, List<MultipartFile> itemImgFileList) throws Exception {

        //상품 수정
        Item item = itemRepository.findById(itemFormDto.getId())
                .orElseThrow(EntityNotFoundException::new);
        item.updateItem(itemFormDto);

        List<Long> itemImgIds = itemFormDto.getItemImgIds();
        //이미지 등록
        for(int i=0;i<itemImgFileList.size();i++){
            itemImgService.updateItemImg(itemImgIds.get(i), itemImgFileList.get(i));
        }

        return item.getId();
    }

    @Transactional(readOnly = true)
    public Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable){
        return itemRepository.getAdminItemPage(itemSearchDto, pageable);
    }
