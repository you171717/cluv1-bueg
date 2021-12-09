package com.shop.service;

import com.shop.dto.InquiryFormDto;
import com.shop.entity.Inquiry;
import com.shop.repository.InquiryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class InquiryService {

    private final InquiryRepository inquiryRepository;

    public Inquiry saveInquiry(InquiryFormDto inquiryFormDto){
        return inquiryRepository.save(inquiryFormDto.toEntity());
    }

    public List<InquiryFormDto> getInquiryList(String email) {
        List<Inquiry> inquiryList = inquiryRepository.findByCreatedBy(email);
        List<InquiryFormDto> inquiryDtoList = new ArrayList<>();

        for(Inquiry inquiry : inquiryList) {
            InquiryFormDto inquiryFormDto = new InquiryFormDto();
            inquiryFormDto.setId(inquiry.getId());
            inquiryFormDto.setTitle(inquiry.getTitle());
            inquiryFormDto.setContent(inquiry.getContent());
            inquiryFormDto.setRegTime(inquiry.getRegTime());
            inquiryFormDto.setCreatedBy(inquiry.getCreatedBy());

            inquiryDtoList.add(inquiryFormDto);
        }

        return inquiryDtoList;
    }

    public List<InquiryFormDto> getAllInquiryList() {
        List<Inquiry> inquiryList = inquiryRepository.findAll(Sort.by(Sort.Direction.DESC,"id"));
        List<InquiryFormDto> inquiryDtoList = new ArrayList<>();

        for(Inquiry inquiry : inquiryList) {
            InquiryFormDto inquiryFormDto = new InquiryFormDto();
            inquiryFormDto.setId(inquiry.getId());
            inquiryFormDto.setTitle(inquiry.getTitle());
            inquiryFormDto.setContent(inquiry.getContent());
            inquiryFormDto.setRegTime(inquiry.getRegTime());
            inquiryFormDto.setCreatedBy(inquiry.getCreatedBy());

            inquiryDtoList.add(inquiryFormDto);
        }

        return inquiryDtoList;
    }

    public InquiryFormDto getInquiryDtl(Long itemId){
        Inquiry inquiry = inquiryRepository.findById(itemId).orElseThrow(EntityNotFoundException::new);

        return InquiryFormDto.of(inquiry);
    }

    public Long updateInquiry(InquiryFormDto inquiryFormDto){
        Inquiry inquiry=inquiryRepository.findById(inquiryFormDto.getId()).orElseThrow(EntityNotFoundException::new);
        inquiry.updateInquiry(inquiryFormDto);

        return inquiry.getId();
    }

    public void deleteInquiry(Long id){
        inquiryRepository.deleteById(id);
    }
}
