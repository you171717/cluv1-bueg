package com.shop.controller;

import com.shop.constant.GiftStatus;
import com.shop.dto.OrderDto;
import com.shop.dto.OrderHistDto;
import com.shop.entity.Order;
import com.shop.repository.MemberRepository;
import com.shop.service.EmailService;
import com.shop.service.OrderService;
import com.shop.service.SmsService;
import lombok.RequiredArgsConstructor;
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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final EmailService emailService;
    private final SmsService smsService;
    private final MemberRepository memberRepository;

    @PostMapping(value = "/order")
    public @ResponseBody ResponseEntity order(@RequestBody @Valid OrderDto orderDto, BindingResult bindingResult, Principal principal) {
        if(bindingResult.hasErrors()) {
            StringBuilder sb = new StringBuilder();

            List<FieldError> fieldErrors = bindingResult.getFieldErrors();

            for(FieldError fieldError : fieldErrors) {
                sb.append(fieldError.getDefaultMessage());
            }

            return new ResponseEntity<String>(sb.toString(), HttpStatus.BAD_REQUEST);
        }

        String email = principal.getName();
        String phone = memberRepository.findByEmail(email).getPhone();  //SMS 전송할 휴대폰 번호 갖고오기

        Long orderId;

        String notice = "email";  //알림 전송 방식 가져오기

        try {
            orderId = orderService.order(orderDto, email);

            if(notice.equals("email")){
                emailService.sendOrderEmail(email, orderDto);
            } else if(notice.equals("sms")){
                smsService.sendOrderSms(phone, orderDto);
            }
        } catch(Exception e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<Long>(orderId, HttpStatus.OK);
    }

    @GetMapping(value = {"/orders", "/orders/{page}"})
    public String orderHist(@PathVariable("page") Optional<Integer> page, Principal principal, Model model) {
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 4);

        Page<OrderHistDto> orderHistDtoList = orderService.getOrderList(principal.getName(), pageable);

        model.addAttribute("orders", orderHistDtoList);
        model.addAttribute("page", pageable.getPageNumber());
        model.addAttribute("maxPage", 5);

        return "order/orderHist";
    }

    // 구매/선물 상태 조회
    @GetMapping(value = {"/ordersStatus/{status}", "/ordersStatus/{page}"})
    public String orderStatus(@PathVariable("page") Optional<Integer> page,
                              @PathVariable(required = false, value="status") GiftStatus giftStatus,
                              Principal principal, Model model){

        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 4);
        Page<OrderHistDto> ordersHistDtoList;

        ordersHistDtoList =
                orderService.getOrderListStatus(principal.getName(), pageable, giftStatus);

        model.addAttribute("orders", ordersHistDtoList);
        model.addAttribute("page", pageable.getPageNumber());
        model.addAttribute("maxPage", 5);
        return "order/orderHist";
    }

    @PostMapping("/order/{orderId}/cancel")
    public @ResponseBody ResponseEntity cancelOrder(@PathVariable("orderId") Long orderId, Principal principal) {
        if(!orderService.validateOrder(orderId, principal.getName())) {
            return new ResponseEntity<String>("주문 취소 권한이 없습니다.", HttpStatus.FORBIDDEN);
        }

        orderService.cancelOrder(orderId);

        return new ResponseEntity<Long>(orderId, HttpStatus.OK);
    }

    @PostMapping("/order/{orderId}/return")
    @ResponseBody
    public ResponseEntity returnOrderProc(@PathVariable("orderId") Long orderId, Principal principal, Model model) throws Exception {

        if (!orderService.validateOrder(orderId, principal.getName())) {
            return new ResponseEntity<String>("반품 요청 권한이 없습니다.", HttpStatus.FORBIDDEN);
        }

        Order order = orderService.getOrder(orderId);

        orderService.requestReturn(order);

        return new ResponseEntity<Long>(orderId, HttpStatus.OK);
    }

    @GetMapping("/order/{orderId}/return")
    public String returnOrder(@PathVariable("orderId") Long orderId, Principal principal, Model model) {

        if (!orderService.validateOrder(orderId, principal.getName())) {
            return "redirect:/orders";
        }
        Order order = orderService.getOrder(orderId);

        model.addAttribute("order", order);
        model.addAttribute("nowDate", new SimpleDateFormat("yyyy.MM.dd").format(new Date()));
        return "order/orderReturn";
    }

    @PostMapping("/order/return/confirm")
    @ResponseBody
    public ResponseEntity returnOrderconfirm(
            @RequestBody Map<String, Object> paramMap,
            Principal principal, Model model) throws Exception {

        List<String> orderIdList = (List<String>) paramMap.get("orderId");
        for (String orderId : orderIdList) {
            orderService.confirmReturn(Long.valueOf(orderId));
        }

        return new ResponseEntity<String>("처리 완료되었습니다.", HttpStatus.OK);
    }

    @GetMapping(value = {"/returns", "/returns/{page}"})
    public String returnsHist(@PathVariable("page") Optional<Integer> page, Principal principal, Model model) throws Exception {

        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 4);
        Page<OrderHistDto> ordersHistDtoList = orderService.getReturnList(principal.getName(), pageable);

        model.addAttribute("returns", ordersHistDtoList);
        model.addAttribute("page", pageable.getPageNumber());
        model.addAttribute("maxPage", 5);

        return "order/orderReturnHist";
    }

}
