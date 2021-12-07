package com.shop.controller;

import com.shop.dto.FAQDto;
import com.shop.dto.FAQSearchDto;
import com.shop.service.FAQService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class FAQController {

    private final FAQService faqService;

    /* faq 리스트 조회 */
    @GetMapping(value = "/cscenter")
    public String readFaq(Model model, FAQSearchDto faqSearchDto) {
        List<FAQDto> FAQDtoList = faqService.getFAQList();

        model.addAttribute("faqsearchdto",new FAQSearchDto());
        model.addAttribute("FAQList", FAQDtoList);

        return "cscenter/faq";
    }

    /* admin계정에서 faq 리스트 조회 */
    @GetMapping(value = "/admin/cscenter")
    public String adminReadFaq(Model model) {
        List<FAQDto> FAQDtoList = faqService.getFAQList();

        model.addAttribute("faqsearchdto", new FAQSearchDto());
        model.addAttribute("FAQList", FAQDtoList);

        return "cscenter/adminfaq";
    }


    /* question으로 faq 검색 결과 조회 */
    @PostMapping(value = "/cscenter/search")
    public String search(FAQSearchDto faqSearchDto, Model model) {
        List<FAQDto> FAQDtoList = faqService.getSearchResult(faqSearchDto);

        model.addAttribute("faqsearchdto",new FAQSearchDto());
        model.addAttribute("FAQDtoList",FAQDtoList);

        return "cscenter/faqsearch";
    }

    /* admin이 question으로 faq 검색 결과 조회 */
    @PostMapping(value = "/admin/cscenter/search")
    public String adminSearch(FAQSearchDto faqSearchDto, Model model) {
        List<FAQDto> FAQDtoList = faqService.getSearchResult(faqSearchDto);

        model.addAttribute("faqsearchdto",new FAQSearchDto());
        model.addAttribute("FAQDtoList",FAQDtoList);

        return "cscenter/adminfaqsearch";
    }

}
