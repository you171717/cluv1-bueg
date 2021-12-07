package com.shop.controller;

import com.shop.dto.AddressDto;
import com.shop.dto.GiftMainItemDto;
import com.shop.dto.ItemSearchDto;
import com.shop.dto.OrderDto;
import com.shop.service.AddressService;
import com.shop.service.ItemService;
import com.shop.service.OrderService;
import com.shop.service.SmsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.nurigo.java_sdk.api.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
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
    public String giftForm(@PathVariable("itemId") Long itemId, @RequestParam("count") String count, Model model) {
        log.info("itemId: " + itemId);
        log.info("count: " + count);

        model.addAttribute("addressDto", new AddressDto());
        model.addAttribute("itemId", itemId);
        model.addAttribute("count", count);

        return "gift/giftForm";
    }

    // 선물하기 폼
    @PostMapping(value = "/address")
    public String address(@Valid AddressDto addressDto, BindingResult bindingResult, Principal principal) {
        if(bindingResult.hasErrors()) {
            return "gift/giftForm";
        }

        String email = principal.getName();

        log.info("AddressDto ===> " + addressDto.toString());

        addressService.address(addressDto, email);

        return "redirect:/";
    }

    // 선물
    @PostMapping(value = "/gift")
    public @ResponseBody ResponseEntity gift(@RequestBody @Valid OrderDto orderDto, BindingResult bindingResult, Principal principal) {
        if(bindingResult.hasErrors()) {
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();

            StringBuffer sb = new StringBuffer();

            for(FieldError fieldError : fieldErrors) {
                sb.append(fieldError.getDefaultMessage());
            }

            return new ResponseEntity<String>(sb.toString(), HttpStatus.BAD_REQUEST);
        }

        String email = principal.getName();

        Long orderId;

        try {
            log.info("OrderDto : " + orderDto);

            orderId = orderService.order(orderDto, email);
        } catch(Exception e) {
            log.error(e.getMessage(), e);

            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<Long>(orderId, HttpStatus.OK);
    }

}
