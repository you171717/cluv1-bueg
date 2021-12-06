package com.shop.controller;

import com.shop.repository.EmailNoticeRepository;
import com.shop.repository.SmsNoticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class StatsController {

    private final EmailNoticeRepository emailNoticeRepository;
    private final SmsNoticeRepository smsNoticeRepository;

    @GetMapping(value= "/admin/noticeStats")
    public String stats(Model model) {

        long emailCount = emailNoticeRepository.count();
        long smsCount = smsNoticeRepository.count();

        model.addAttribute("emailCount", emailCount);
        model.addAttribute("smsCount", smsCount);

        return "/notice/noticeStats";
    }
}
