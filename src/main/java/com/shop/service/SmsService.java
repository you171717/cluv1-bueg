package com.shop.service;

import com.shop.dto.OrderDto;
import com.shop.entity.Item;
import com.shop.entity.Order;
import com.shop.entity.OrderItem;
import com.shop.entity.SmsNotice;
import com.shop.repository.ItemRepository;
import com.shop.repository.OrderRepository;
import com.shop.repository.SmsNoticeRepository;
import lombok.RequiredArgsConstructor;
import net.nurigo.java_sdk.api.Message;
import net.nurigo.java_sdk.exceptions.CoolsmsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class SmsService {

    private static final Logger logger = LoggerFactory.getLogger(SmsService.class);

    private final ItemRepository itemRepository;
    private final OrderRepository orderRepository;
    private final SmsNoticeRepository smsNoticeRepository;

    @Value("${spring.sms.api-key}")
    private String smsApiKey;

    @Value("${spring.sms.api-secret}")
    private String smsApiSecret;

    @Async
    public void sendSms(String phone, String text) {
        Message coolsms = new Message(smsApiKey, smsApiSecret);
        HashMap<String, String> params = new HashMap<String, String>();

        params.put("to", phone);
        params.put("from", "010-3583-7031");
        params.put("type", "SMS");
        params.put("text", text);
        params.put("app_version", "test app 1.2");

        try {
            coolsms.send(params);
        } catch (CoolsmsException e) {
            logger.error(e.getMessage(), e);
        }
    }

    public void sendOrderSms(String phone, OrderDto orderDto) {
        Item item = itemRepository.findById(orderDto.getItemId()).orElseThrow(EntityNotFoundException::new);

        StringBuffer sb = new StringBuffer("[Bueg] 주문 상품 내역\n");
        sb.append("주문 상품 : ");
        sb.append(item.getItemNm());
        sb.append("\n주문 수량 : ");
        sb.append(orderDto.getCount());
        sb.append("\n주문 금액 : ");
        sb.append(item.getPrice() * orderDto.getCount());
        sb.append("원");

        this.sendSms(phone, sb.toString());
        this.addSmsCount();
    }

    public void sendCartOrderSms(String phone, Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(EntityNotFoundException::new);

        StringBuffer sb = new StringBuffer("[Bueg]주문상품 내역\n");

        for(OrderItem orderItem : order.getOrderItems()) {
            sb.append(orderItem.getItem().getItemNm());
            sb.append("(");
            sb.append(orderItem.getItem().getPrice());
            sb.append(" 원) x ");
            sb.append(orderItem.getCount());
            sb.append("개\n");
        }

        sb.append("\n주문 금액 : ");
        sb.append(order.getTotalPrice());
        sb.append("원\n");

        this.sendSms(phone, sb.toString());
        this.addSmsCount();
    }

    public void addSmsCount() {
        SmsNotice smsNotice = new SmsNotice();

        smsNoticeRepository.save(smsNotice);
    }

}
