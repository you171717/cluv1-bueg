package com.shop.service;

import com.shop.dto.MemberFormDto;
import com.shop.dto.OrderDto;
import com.shop.entity.EmailNotice;
import com.shop.entity.Item;
import com.shop.entity.Order;
import com.shop.entity.OrderItem;
import com.shop.repository.AuthTokenRepository;
import com.shop.repository.EmailNoticeRepository;
import com.shop.repository.ItemRepository;
import com.shop.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpSession;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender javaMailSender;

    private final AuthTokenService authTokenService;
    private final ItemRepository itemRepository;
    private final OrderRepository orderRepository;
    private final EmailNoticeRepository emailNoticeRepository;

    public void sendEmail(String to, String subject, String text) {
        this.sendEmail(to, subject, text, false);
    }

    @Async
    public void sendEmail(String to, String subject, String text, boolean html) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
            mimeMessageHelper.setTo(to);
            mimeMessageHelper.setSubject(subject);
            mimeMessageHelper.setText(text, html);

            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendEmailAuthCode(String email, HttpSession httpSession) {
        String code = authTokenService.getTokenCode(email).substring(0, 6);
        String subject = "[Bueg] 인증코드입니다.";
        String text = "이메일 인증코드는 " + code + "입니다.";

        httpSession.setAttribute("emailConfirmCode", code);

        this.sendEmail(email, subject, text);
    }

    public void sendOrderEmail(String email, OrderDto orderDto) {
        Item item = itemRepository.findById(orderDto.getItemId()).orElseThrow(EntityNotFoundException::new);

        String subject = "주문 상품 내역입니다.";

        StringBuffer sb = new StringBuffer();
        sb.append("[Bueg] 주문 상품 내역입니다.\n");
        sb.append("주문 상품 : ");
        sb.append(item.getItemNm());
        sb.append("\n주문 수량 : ");
        sb.append(orderDto.getCount());
        sb.append("\n주문 금액 : ");
        sb.append(item.getPrice() * orderDto.getCount());
        sb.append("원 입니다.\n");

        this.sendEmail(email, subject, sb.toString());
        this.addEmailCount();
    }

    public void sendCartOrderEmail(String email, Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(EntityNotFoundException::new);

        String subject = "주문 상품 내역입니다.";

        StringBuffer sb = new StringBuffer("[Bueg] 주문 상품 내역입니다.\n\n");

        for(OrderItem orderItem : order.getOrderItems()) {
            sb.append(orderItem.getItem().getItemNm());
            sb.append("(");
            sb.append(orderItem.getItem().getPrice());
            sb.append(" 원) x ");
            sb.append(orderItem.getCount() + "개\n");
        }

        sb.append("\n주문 금액 : ");
        sb.append(order.getTotalPrice());
        sb.append("원\n");

        this.sendEmail(email, subject, sb.toString());
        this.addEmailCount();
    }

    public void sendPasswordEmail(String email) {
        String subject = "[Bueg] 비밀번호를 변경해주세요";

        StringBuffer sb = new StringBuffer();
        sb.append("<a href='http://localhost:8080/members/updatePassword?code=");
        sb.append(authTokenService.createToken(email).getCode());
        sb.append("&email=");
        sb.append(email);
        sb.append("'>비밀번호 변경페이지</a>");

        this.sendEmail(email, subject, sb.toString(), true);
    }

    public void addEmailCount() {
        EmailNotice emailNotice = new EmailNotice();

        emailNoticeRepository.save(emailNotice);
    }

}