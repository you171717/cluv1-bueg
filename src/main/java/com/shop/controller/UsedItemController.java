package com.shop.controller;

import com.shop.dto.ItemFormDto;
import com.shop.dto.UsedItemDto;
import com.shop.dto.UsedItemFormDto;
import com.shop.dto.UsedItemSearchDto;
import com.shop.service.UsedItemImgService;
import com.shop.service.UsedItemService;
import groovy.util.logging.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

import static org.reflections.Reflections.log;

@Slf4j
@Controller
@RequiredArgsConstructor
public class UsedItemController {

    private final UsedItemService usedItemService;

    @GetMapping(value = "/uitem")
    public String usedItem(UsedItemSearchDto usedItemSearchDto, Optional<Integer> page, Model model){
        log.info("================/Useitem 로 들어왔음============");
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 6);

        log.info("usedItemSearchDto : " + usedItemSearchDto.toString());
        Page<UsedItemDto> items = usedItemService.getUsedItemPage(usedItemSearchDto, pageable);

        model.addAttribute("items", items);
        model.addAttribute("usedItemSearchDto", usedItemSearchDto);
        model.addAttribute("maxPage", 5);

        return "usedItem";
    }

    @GetMapping(value = "/admin/uitem/new")
    public String itemForm(Model model) {
        model.addAttribute("usedItemFormDto", new UsedItemFormDto());

        return "/item/usedItemForm";
    }

    @PostMapping(value = "/admin/uitem/new")
    public String itemNew(@Valid UsedItemFormDto usedItemFormDto, BindingResult bindingResult, Model model, @RequestParam("itemImgFile") List<MultipartFile> itemImgFileList) {
        if(bindingResult.hasErrors()) {
            return "item/usedItemForm";
        }

        if(itemImgFileList.get(0).isEmpty() && usedItemFormDto.getId() == null) {
            model.addAttribute("errorMessage", "첫번째 상품 이미지는 필수 입력 값 입니다.");
            return "item/usedItemForm";
        }

        try {
            log.info(usedItemFormDto.toString());
            usedItemService.saveUsedItem(usedItemFormDto, itemImgFileList);
        } catch(Exception e) {
            model.addAttribute("errorMessage", "상품 등록 중 에러가 발생하였습니다.");

            return "item/usedItemForm";
        }

        return "redirect:/";
    }
}
