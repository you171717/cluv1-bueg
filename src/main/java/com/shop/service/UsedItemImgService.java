package com.shop.service;

import com.shop.entity.UsedItemImg;
import com.shop.repository.UsedItemImgRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityNotFoundException;

@Service
@RequiredArgsConstructor
@Transactional
public class UsedItemImgService {

    @Value("${itemImgLocation}")
    private String itemImgLocation;

    private final UsedItemImgRepository usedItemImgRepository;

    private final FileService fileService;

    public void saveUsedItemImg(UsedItemImg usedItemImg, MultipartFile itemImgFile) throws Exception {
        String oriImgName = itemImgFile.getOriginalFilename();
        String imgName = "";
        String imgUrl = "";

        //파일 업로드
        if(!StringUtils.isEmpty(oriImgName)){
            imgName = fileService.uploadFile(itemImgLocation, oriImgName, itemImgFile.getBytes());
            imgUrl = "/images/item/" + imgName;
        }

        // 상품 이미지 정보 저장
        usedItemImg.updateUsedItemImg(oriImgName, imgName, imgUrl);
        usedItemImgRepository.save(usedItemImg);
    }

    public void updateItemImg(Long itemImgId, MultipartFile itemImgFile) throws Exception {
        if(!itemImgFile.isEmpty()){
            UsedItemImg savedUsedItemImg = usedItemImgRepository.findById(itemImgId)
                    .orElseThrow(EntityNotFoundException::new);

            //기존 이미지 파일 삭제
            if(!StringUtils.isEmpty(savedUsedItemImg.getImgName())) {
                fileService.deleteFile(itemImgLocation+"/"+savedUsedItemImg.getImgName());
            }

            String oriImgName = itemImgFile.getOriginalFilename();
            String imgName = fileService.uploadFile(itemImgLocation, oriImgName, itemImgFile.getBytes());
            String imgUrl = "/images/item/" + imgName;
            savedUsedItemImg.updateUsedItemImg(oriImgName, imgName, imgUrl);
        }
    }
}
