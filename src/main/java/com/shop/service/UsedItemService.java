package com.shop.service;

import com.shop.dto.*;
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

        UsedItem usedItem = usedItemRepository.findById(itemId)
                .orElseThrow(EntityNotFoundException::new);
        UsedItemFormDto usedItemFormDto = UsedItemFormDto.of(usedItem);
        usedItemFormDto.setUsedItemImgDtoList(itemImgDtoList);
        return usedItemFormDto;
    }

    public Long updateItem(UsedItemFormDto usedItemFormDto, List<MultipartFile> itemImgFileList) throws Exception {

        //상품 수정
        UsedItem usedItem = usedItemRepository.findById(usedItemFormDto.getId())
                .orElseThrow(EntityNotFoundException::new);
        usedItem.updateItem(usedItemFormDto);

        List<Long> itemImgIds = usedItemFormDto.getItemImgIds();
        //이미지 등록
        for(int i=0;i<itemImgFileList.size();i++){
            usedItemImgService.updateItemImg(itemImgIds.get(i), itemImgFileList.get(i));
        }

        return usedItem.getId();
    }

    @Transactional(readOnly = true)
    public Page<UsedItemDto> getUsedItemPage(UsedItemSearchDto usedItemSearchDto, Pageable pageable){
        return usedItemRepository.getUsedItemPage (usedItemSearchDto, pageable);
    }
