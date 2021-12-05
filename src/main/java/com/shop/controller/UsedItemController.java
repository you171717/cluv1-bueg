package com.shop.controller;

import com.shop.dto.UsedItemSearchDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Optional;

import static org.reflections.Reflections.log;

public class UsedItemController {

    @GetMapping(value = "/uitem")
    public String useitem(UsedItemSearchDto usedItemSearchDto, Optional<Integer> page, Model model){
        log.info("================/Useitem 로 들어왔음============");
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 6);
        Page<MainItemDto> items = itemService.getOldItemPage(itemSearchDto, pageable);

        model.addAttribute("items", items);
        model.addAttribute("itemSearchDto", itemSearchDto);
        model.addAttribute("maxPage", 5);

        return "useitem";
    }

}
