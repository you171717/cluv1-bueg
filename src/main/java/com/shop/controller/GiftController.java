package com.shop.controller;

import com.shop.dto.*;
import com.shop.service.AddressService;
import com.shop.service.ItemService;
import com.shop.service.OrderService;
import com.shop.service.SmsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.Optional;

@Slf4j
@Controller
@RequiredArgsConstructor
public class GiftController {

    private final AddressService addressService;
    private final OrderService orderService;
    private final ItemService itemService;
    private final SmsService smsService;

    @GetMapping(value = "/giftMain/{cateCode}")
    public String giftMain(@PathVariable("cateCode") Long cateCode, ItemSearchDto itemSearchDto, Optional<Integer> page, Model model) {
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0,6);
        Page<GiftMainItemDto> items = itemService.getGiftItemPage(itemSearchDto, pageable, cateCode);

        log.info(String.valueOf(cateCode));

        model.addAttribute("items", items);
        model.addAttribute("itemSearchDto", itemSearchDto);
        model.addAttribute("maxPage", 5);

        return "gift/giftMain";
    }

    @PostMapping(value = "/sendSms")
    public @ResponseBody ResponseEntity sendSms(@RequestParam(name = "from") String from, @RequestParam(name = "text") String text) {
        smsService.sendSms(from, text);

        log.info("문자 전송 완료");

        return new ResponseEntity<Long>(HttpStatus.OK);
    }

    // 선물하기 폼
    @GetMapping(value ="/giftForm/{itemId}")
    public String giftForm(@PathVariable("itemId") Long itemId, @RequestParam("count") Integer count, Model model) {
        GiftDto giftDto = new GiftDto();
        giftDto.setItemId(itemId);
        giftDto.setCount(count);

        model.addAttribute("giftDto", giftDto);

        return "gift/giftForm";
    }

    @PostMapping(value = "/gift")
    public String gift(@Valid GiftDto giftDto, BindingResult bindingResult, Principal principal, Model model) {
        if(bindingResult.hasErrors()) {
            return "gift/giftForm";
        }

        String email = principal.getName();

        try {
            orderService.order(giftDto.toOrderDto(), email);
            addressService.saveAddress(giftDto.toAddressDto(), email);
        } catch(Exception e) {
            log.error(e.getMessage(), e);

            return "gift/giftForm";
        }

        model.addAttribute("message", "선물하기가 완료되었습니다.");
        model.addAttribute("location", "/orders");

        return "redirect";
    }

}
