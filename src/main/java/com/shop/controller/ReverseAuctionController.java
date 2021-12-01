package com.shop.controller;

import com.shop.dto.*;
import com.shop.entity.ReverseAuction;
import com.shop.repository.ReverseAuctionRepository;
import com.shop.service.ItemService;
import com.shop.service.ReverseAuctionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class ReverseAuctionController {

    private final ItemService itemService;

    private final ReverseAuctionService reverseAuctionService;

    private final ReverseAuctionRepository reverseAuctionRepository;

    @GetMapping(value = {"/rauctions", "/rauctions/{page}"})
    public String reverseAuctionList(ReverseAuctionSearchDto reverseAuctionSearchDto, @PathVariable("page") Optional<Integer> page, Model model) {
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 6);
        Page<ReverseAuctionDto> reverseAuctionDtoList = reverseAuctionService.getUserReverseAuctionPage(reverseAuctionSearchDto, pageable);
        List<ReverseAuctionHistoryDto> previousReverseAuctionDtoList = reverseAuctionService.getPreviousReverseAuctionPage();

        model.addAttribute("reverseAuctionDtoList", reverseAuctionDtoList);
        model.addAttribute("reverseAuctionSearchDto", reverseAuctionSearchDto);
        model.addAttribute("previousReverseAuctionDtoList", previousReverseAuctionDtoList);
        model.addAttribute("maxPage", 6);

        return "reverseAuction/reverseAuctionList";
    }

    @GetMapping(value = "/rauction/{reverseAuctionId}")
    public String reverseAuctionDtl(@PathVariable("reverseAuctionId") Long reverseAuctionId, Model model) {
        ReverseAuctionDto reverseAuctionDto = reverseAuctionService.getReverseAuctionDtl(reverseAuctionId);

        ReverseAuction reverseAuction = reverseAuctionRepository.findById(reverseAuctionId).orElseThrow(EntityNotFoundException::new);

        ItemFormDto itemFormDto = itemService.getItemDtl(reverseAuction.getItem().getId());

        model.addAttribute("reverseAuctionDto", reverseAuctionDto);
        model.addAttribute("itemFormDto", itemFormDto);

        return "reverseAuction/reverseAuctionDtl";
    }

    @GetMapping(value = {"/admin/rauctions", "/admin/rauctions/{page}"})
    public String reverseAuctionManage(ReverseAuctionSearchDto reverseAuctionSearchDto, @PathVariable("page") Optional<Integer> page, Model model) {
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 5);
        Page<ReverseAuctionHistoryDto> reverseAuctionDtoList = reverseAuctionService.getAdminReverseAuctionPage(reverseAuctionSearchDto, pageable);

        model.addAttribute("reverseAuctionDtoList", reverseAuctionDtoList);
        model.addAttribute("reverseAuctionSearchDto", reverseAuctionSearchDto);
        model.addAttribute("maxPage", 5);

        return "reverseAuction/reverseAuctionMng";
    }

    @GetMapping(value = "/admin/rauction/new")
    public String reverseAuctionForm(Model model) {
        Pageable pageable = PageRequest.of(0, Integer.MAX_VALUE);
        ItemSearchDto itemSearchDto = new ItemSearchDto();

        Page<MainItemDto> itemList = itemService.getMainItemPage(itemSearchDto, pageable);

        model.addAttribute("reverseAuctionFormDto", new ReverseAuctionFormDto());
        model.addAttribute("itemList", itemList);

        return "reverseAuction/reverseAuctionForm";
    }

    @PostMapping(value = "/admin/rauction/new")
    public String reverseAuctionNew(@Valid ReverseAuctionFormDto reverseAuctionFormDto, BindingResult bindingResult, Model model) {
        Pageable pageable = PageRequest.of(0, Integer.MAX_VALUE);
        ItemSearchDto itemSearchDto = new ItemSearchDto();

        Page<MainItemDto> itemList = itemService.getMainItemPage(itemSearchDto, pageable);

        if(bindingResult.hasErrors()) {
            model.addAttribute("itemList", itemList);

            return "reverseAuction/reverseAuctionForm";
        }

        try {
            reverseAuctionService.saveReverseAuction(reverseAuctionFormDto);
        } catch(Exception e) {
            model.addAttribute("itemList", itemList);
            model.addAttribute("errorMessage", "역경매 상품 등록 중 에러가 발생하였습니다.");

            return "reverseAuction/reverseAuctionForm";
        }

        return "redirect:/admin/rauctions";
    }

    @GetMapping(value = "/admin/rauction/{reverseAuctionId}")
    public String reverseAuctionForm(@PathVariable("reverseAuctionId") Long reverseAuctionId, Model model) {
        Pageable pageable = PageRequest.of(0, Integer.MAX_VALUE);
        ItemSearchDto itemSearchDto = new ItemSearchDto();

        Page<MainItemDto> itemList = itemService.getMainItemPage(itemSearchDto, pageable);

        model.addAttribute("itemList", itemList);

        try {
            ReverseAuctionFormDto reverseAuctionFormDto = reverseAuctionService.getReserveAuctionForm(reverseAuctionId);

            model.addAttribute("reverseAuctionFormDto", reverseAuctionFormDto);
        } catch(Exception e) {
            model.addAttribute("reverseAuctionFormDto", new ReverseAuctionFormDto());
            model.addAttribute("errorMessage", "존재하지 않는 상품 입니다.");

            return "reverseAuction/reverseAuctionForm";
        }

        return "reverseAuction/reverseAuctionForm";
    }

    @PostMapping(value = "/admin/rauction/{reverseAuctionId}")
    public String reverseAuctionUpdate(@Valid ReverseAuctionFormDto reverseAuctionFormDto, BindingResult bindingResult, Model model) {
        Pageable pageable = PageRequest.of(0, Integer.MAX_VALUE);
        ItemSearchDto itemSearchDto = new ItemSearchDto();

        Page<MainItemDto> itemList = itemService.getMainItemPage(itemSearchDto, pageable);

        if(bindingResult.hasErrors()) {
            model.addAttribute("itemList", itemList);

            return "reverseAuction/reverseAuctionForm";
        }

        try {
            reverseAuctionService.updateReserveAuction(reverseAuctionFormDto);
        } catch(Exception e) {
            model.addAttribute("itemList", itemList);
            model.addAttribute("errorMessage", "역경매 상품 수정 중 에러가 발생하였습니다.");

            return "reverseAuction/reverseAuctionForm";
        }

        return "redirect:/admin/rauctions";
    }

    @GetMapping(value = "/admin/rauction/{reverseAuctionId}/delete")
    public String reverseAuctionDelete(@PathVariable("reverseAuctionId") Long reverseAuctionId, Model model) {
        reverseAuctionService.deleteReverseAuction(reverseAuctionId);

        return "redirect:/admin/rauctions";
    }

}
