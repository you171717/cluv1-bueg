package com.shop.controller;

import com.shop.dto.UsedItemDto;
import com.shop.dto.UsedItemSearchDto;
import com.shop.service.UsedItemImgService;
import com.shop.service.UsedItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Optional;

import static org.reflections.Reflections.log;

@Controller
@RequiredArgsConstructor
public class UsedItemController {

    private final UsedItemService usedItemService;

    @GetMapping(value = "/uitem")
    public String usedItem(UsedItemSearchDto usedItemSearchDto, Optional<Integer> page, Model model){
        log.info("================/Useitem 로 들어왔음============");
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 6);
        Page<UsedItemDto> items = usedItemService.getUsedItemPage(usedItemSearchDto, pageable);

        model.addAttribute("items", items);
        model.addAttribute("usedItemSearchDto", usedItemSearchDto);
        model.addAttribute("maxPage", 5);

        return "useitem";
    }

}
