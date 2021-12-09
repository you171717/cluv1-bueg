package com.shop.service;

import com.shop.constant.UsedItemSellStatus;
import com.shop.dto.UsedItemDto;
import com.shop.dto.UsedItemFormDto;
import com.shop.dto.UsedItemImgDto;
import com.shop.dto.UsedItemSearchDto;
import com.shop.entity.Member;
import com.shop.entity.UsedItem;
import com.shop.entity.UsedItemImg;
import com.shop.repository.MemberRepository;
import com.shop.repository.UsedItemImgRepository;
import com.shop.repository.UsedItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class UsedItemService {

    private final MemberRepository memberRepository;
    private final UsedItemRepository usedItemRepository;
    private final UsedItemImgService usedItemImgService;
    private final UsedItemImgRepository usedItemImgRepository;

    public Long saveUsedItem(UsedItemFormDto usedItemFormDto, List<MultipartFile> usedItemImgFileList, String email) throws Exception {
        Member member = memberRepository.findByEmail(email);

        UsedItem usedItem = usedItemFormDto.createItem(member);

        for (int i = 0; i < usedItemImgFileList.size(); i++) {
            UsedItemImg usedItemImg = new UsedItemImg();
            usedItemImg.setUsedItem(usedItem);

            if (i == 0) {
                usedItemImg.setRepimgYn("Y");
            } else {
                usedItemImg.setRepimgYn("N");
            }

            usedItemImgService.saveUsedItemImg(usedItemImg, usedItemImgFileList.get(i));
        }

        usedItemRepository.save(usedItem);

        return usedItem.getId();
    }

    @Transactional(readOnly = true)
    public UsedItemFormDto getUsedItemDtl(Long usedItemId) {
        List<UsedItemImg> usedItemImgList = usedItemImgRepository.findByUsedItemIdOrderByIdAsc(usedItemId);
        List<UsedItemImgDto> usedItemImgDtoList = new ArrayList<>();

        for (UsedItemImg usedItemImg : usedItemImgList) {
            UsedItemImgDto usedItemImgDto = UsedItemImgDto.of(usedItemImg);

            usedItemImgDtoList.add(usedItemImgDto);
        }

        UsedItem usedItem = usedItemRepository.findById(usedItemId).orElseThrow(EntityNotFoundException::new);

        UsedItemFormDto usedItemFormDto = UsedItemFormDto.of(usedItem);
        usedItemFormDto.setUsedItemImgDtoList(usedItemImgDtoList);

        return usedItemFormDto;
    }

    public Long updateUsedItem(UsedItemFormDto usedItemFormDto, List<MultipartFile> usedItemImgFileList) throws Exception {
        UsedItem usedItem = usedItemRepository.findById(usedItemFormDto.getId()).orElseThrow(EntityNotFoundException::new);
        usedItem.updateItem(usedItemFormDto);

        List<Long> usedItemImgIds = usedItemFormDto.getUsedItemImgIds();

        for (int i = 0; i < usedItemImgFileList.size(); i++) {
            usedItemImgService.updateItemImg(usedItemImgIds.get(i), usedItemImgFileList.get(i));
        }

        return usedItem.getId();
    }

    public Long updateUsedItemSellStatus(Long usedItemId, UsedItemSellStatus usedItemSellStatus) {
        UsedItem usedItem = usedItemRepository.findById(usedItemId).orElseThrow(EntityNotFoundException::new);
        usedItem.setUsedItemSellStatus(usedItemSellStatus);

        usedItemRepository.save(usedItem);

        return usedItem.getId();
    }

    public boolean validateUsedItem(Long usedItemId, String email) {
        UsedItem usedItem = usedItemRepository.findById(usedItemId).orElseThrow(EntityNotFoundException::new);
        Member curMember = memberRepository.findByEmail(email);
        Member savedMember = usedItem.getOwner();

        if(!StringUtils.equals(curMember.getEmail(), savedMember.getEmail())) {
            return false;
        }

        return true;
    }

    @Transactional(readOnly = true)
    public Page<UsedItemDto> getAllUsedItemPage(UsedItemSearchDto usedItemSearchDto, Pageable pageable) {
        return usedItemRepository.getAllUsedItemPage(usedItemSearchDto, pageable);
    }

    @Transactional(readOnly = true)
    public Page<UsedItemDto> getUserUsedItemPage(String email, UsedItemSearchDto usedItemSearchDto, Pageable pageable) {
        return usedItemRepository.getUserUsedItemPage(email, usedItemSearchDto, pageable);
    }

}
