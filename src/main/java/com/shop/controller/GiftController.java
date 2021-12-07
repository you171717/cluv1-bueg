package com.shop.controller;

import com.shop.dto.AddressDto;
import com.shop.dto.GiftMainItemDto;
import com.shop.dto.ItemSearchDto;
import com.shop.dto.OrderDto;
import com.shop.service.AddressService;
import com.shop.service.ItemService;
import com.shop.service.OrderService;
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
@RequiredArgsConstructor
@Controller
public class GiftController {

    private final AddressService addressService;
    private final OrderService orderService;
    private final ItemService itemService;

    @GetMapping(value = "/giftMain/{cateCode}")
    public String giftMain(@PathVariable("cateCode") Long cateCode, ItemSearchDto itemSearchDto,
                           Optional<Integer> page, Model model){

        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0,6);
        Page<GiftMainItemDto> items =
                itemService.getGiftItemPage(itemSearchDto, pageable, cateCode);


        log.info(String.valueOf(cateCode));

        model.addAttribute("items", items);
        model.addAttribute("itemSearchDto", itemSearchDto);
        model.addAttribute("maxPage", 5);

        return "gift/giftMain" ;
    }



    // 문자 전송
    @PostMapping(value = "/sendSms")
    public ResponseEntity sendSms(@RequestBody OrderDto orderDto,
                                  Principal principal) throws Exception { // 휴대폰 문자보내기

        String api_key = "api_key";
        String api_secret = "api_secret";
        Message coolsms = new Message(api_key, api_secret);

        HashMap<String, String> params = new HashMap<String, String>();

        String email = principal.getName();
        Long orderId;

        // 수신번호
        params.put("to", "010-5363-6153");
        // 발신번호
        // params.put("from", orderDto.getFrom());
        params.put("from", "");
        // 문자내용
        // params.put("text", orderDto.getText());
        params.put("text", "");
        // 문자 타입
        params.put("type", "sms");
        // application name and version
        params.put("app_version", "JAVA SDK v2.2");

        System.out.println(params);

        // 보내기&전송결과받기
//        JSONObject result = coolsms.send(params);

        log.info("문자 전송 완료");

        return new ResponseEntity<Long>(HttpStatus.OK);
    }


    // 선물하기 폼
    @GetMapping(value ="/giftForm/{itemId}")
    public String giftForm(Model model, @PathVariable("itemId") Long itemId, @RequestParam("count") String count){
        log.info("itemId: " + itemId);
        log.info("count: " + count);

        model.addAttribute("addressDto", new AddressDto());
        model.addAttribute("itemId", itemId);
        model.addAttribute("count", count);
        return "gift/giftForm";
    }

    // 선물하기 폼
    @PostMapping(value = "/address")
    // 검증하려는 객체에 @Valid 선언, 결과는 bindingResult에 담아줌
    public String address(@Valid AddressDto addressDto,
                          BindingResult bindingResult, Principal principal) {

        if (bindingResult.hasErrors()) {
            return "gift/giftForm";
        }

        Long memberId;
        String email = principal.getName();
        log.info("AddressDto ===> " + addressDto.toString());
        memberId = addressService.address(addressDto,email);

        return "redirect:/";
    }

    // 선물
    @PostMapping(value = "/gift")
    public ResponseEntity gift(@RequestBody @Valid OrderDto orderDto,
                        BindingResult bindingResult, Principal principal) {

        if (bindingResult.hasErrors()) {
            StringBuilder sb = new StringBuilder();
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            for (FieldError fieldError : fieldErrors) {
                sb.append(fieldError.getDefaultMessage());
            }

            // 에러 정보를 ResponseEntity 객체에 담아서 반환
            return new ResponseEntity<String>(sb.toString(),
                    HttpStatus.BAD_REQUEST);
        }

        String email = principal.getName();
        Long orderId;

        // 주문 로직 호출
        try {
            orderId = orderService.order(orderDto, email);
            log.info("orderDto : " + orderDto.toString());
        } catch (Exception e) {
            return new ResponseEntity<String>(e.getMessage(),
                    HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<Long>(orderId, HttpStatus.OK);
    }
}
