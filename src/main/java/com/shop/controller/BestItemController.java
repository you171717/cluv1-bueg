package com.shop.controller;

import com.shop.dto.BestItemDto;
import com.shop.repository.ItemRepository;
import com.shop.service.BestItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.constraints.Size;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/item/best")
@RequiredArgsConstructor
public class BestItemController {

    private final ItemRepository itemRepository;

    @GetMapping("/day")
    public String getBestItemByDays(Model model) {
        List<BestItemDto> items = itemRepository.getBestItemList();

        log.info("items : " + items);

        model.addAttribute("items", items);

        return "item/bestItem";
    }

//    @GetMapping(value = "/week")
//    public String getBestItemByWeek(Model model){
//        List<BestItemDto> items = myBatisService.getBestItemByWeek();
//
//        model.addAttribute("items", items);
//
//        return "item/bestItem";
//    }
//
//    @GetMapping(value = "/month")
//    public String getBestItemByMonth(Model model){
//        List<BestItemDto> items = myBatisService.getBestItemByMonth();
//
//        model.addAttribute("items", items);
//
//        return "item/bestItem";
//    }

}