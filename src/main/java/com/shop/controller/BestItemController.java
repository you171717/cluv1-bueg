package com.shop.controller;

import com.shop.dto.BestItemDto;
import com.shop.service.BestItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/item/best")
@RequiredArgsConstructor
public class BestItemController {

    private final BestItemService myBatisService;

    @GetMapping("/Day")
    public String getBestItemByDays(Model model) {
        List<BestItemDto> items = myBatisService.getBestItemByDays();
        model.addAttribute("items", items);
        return "item/bestItem";
    }

    @GetMapping(value = "/Week")
    public String getBestItemByWeek(Model model){
        List<BestItemDto> items = myBatisService.getBestItemByWeek();
        model.addAttribute("items", items);
        return "item/bestItem";
    }

    @GetMapping(value = "/Month")
    public String getBestItemByMonth(Model model){
        List<BestItemDto> items = myBatisService.getBestItemByMonth();
        model.addAttribute("items", items);
        return "item/bestItem";
    }

}