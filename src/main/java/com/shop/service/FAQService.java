package com.shop.service;

import com.shop.dto.FAQDto;
import com.shop.dto.FAQSearchDto;
import com.shop.entity.FAQ;
import com.shop.repository.FAQRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FAQService {
    private final FAQRepository faqRepository;

    @Transactional
    public List<FAQDto> getFAQList() {
        List<FAQ> faqLists = faqRepository.findAll();
        List<FAQDto> faqDtoList = new ArrayList<>();

        for(FAQ faq : faqLists) {
            FAQDto faqDto = new FAQDto();
            faqDto.setId(faq.getId());
            faqDto.setQuestion(faq.getQuestion());
            faqDto.setAnswer(faq.getAnswer());

            faqDtoList.add(faqDto);
        }

        return faqDtoList;
    }

    @Transactional(readOnly = true)
    public List<FAQDto> getSearchResult(FAQSearchDto faqSearchDto) {
        List<FAQ> faqLists = faqRepository.findByQuestion(faqSearchDto.getSearchQuery());
        List<FAQDto> faqDtoList = new ArrayList<>();

        for(FAQ faq : faqLists) {
            FAQDto faqDto = new FAQDto();
            faqDto.setId(faq.getId());
            faqDto.setQuestion(faq.getQuestion());
            faqDto.setAnswer(faq.getAnswer());

            faqDtoList.add(faqDto);
        }

        return faqDtoList;

    }
}
