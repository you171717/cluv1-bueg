package com.shop.controller;

import com.shop.dto.*;
import com.shop.entity.Item;
import com.shop.entity.Tag;
import com.shop.naverapi.NaverShopSearch;
import com.shop.service.ItemService;
import com.shop.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;
    private final ReviewService reviewService;
    private final NaverShopSearch naverShopSearch;

    @GetMapping(value = "/admin/item/new")
    public String itemForm(Model model) {
        model.addAttribute("itemFormDto", new ItemFormDto());

        return "/item/itemForm";
    }

    @PostMapping(value = "/admin/item/new")
    public String itemNew(@Valid ItemFormDto itemFormDto, BindingResult bindingResult, Model model, @RequestParam("itemImgFile") List<MultipartFile> itemImgFileList, @RequestParam("tags[]") List<String> tags) {
        if(bindingResult.hasErrors()) {
            return "item/itemForm";
        }

        if(itemImgFileList.get(0).isEmpty() && itemFormDto.getId() == null) {
            model.addAttribute("errorMessage", "첫번째 상품 이미지는 필수 입력 값 입니다.");

            return "item/itemForm";
        }

        try {
            itemService.saveItem(itemFormDto, itemImgFileList);
        } catch(Exception e) {
            model.addAttribute("errorMessage", "상품 등록 중 에러가 발생하였습니다.");

            return "item/itemForm";
        }

        return "redirect:/";
    }

    @GetMapping(value = "/admin/item/{itemId}")
    public String itemFormUpdate(@PathVariable("itemId") Long itemId, Model model) {
        try {
            ItemFormDto itemFormDto = itemService.getItemDtl(itemId);
            List<Tag> tag = itemService.getTagList(itemId);    //태그 조회
            List<String> tagList = new ArrayList<>();

            for(Tag t : tag) {
                tagList.add(t.getId().toString());
            }
            model.addAttribute("tags",tagList);

            model.addAttribute("itemFormDto", itemFormDto);
        } catch(EntityNotFoundException e) {
            model.addAttribute("errorMessage", "존재하지 않는 상품 입니다.");
            model.addAttribute("itemFormDto", new ItemFormDto());

            return "item/itemForm";
        }

        return "item/itemForm";
    }

    @PostMapping(value = "/admin/item/{itemId}")
    public String itemUpdate(@Valid ItemFormDto itemFormDto, BindingResult bindingResult, @RequestParam("itemImgFile") List<MultipartFile> itemImgFileList, Model model,@RequestParam("tags[]") List<String> tags) {
        if(bindingResult.hasErrors()) {
            return "item/itemForm";
        }

        if(itemImgFileList.get(0).isEmpty() && itemFormDto.getId() == null) {
            model.addAttribute("errorMessage", "첫번째 상품 이미지는 필수 입력 값 입니다.");

            return "item/itemForm";
        }

        try {
            itemService.updateItem(itemFormDto, itemImgFileList);
        } catch(Exception e) {
            model.addAttribute("errorMessage", "상품 수정 중 에러가 발생하였습니다.");

            return "item/itemForm";
        }

        return "redirect:/";
    }

    @GetMapping(value = {"/admin/items", "/admin/items/{page}"})
    public String itemManage(ItemSearchDto itemSearchDto, @PathVariable("page") Optional<Integer> page, Model model) {
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 5);
        Page<Item> items = itemService.getAdminItemPage(itemSearchDto, pageable);

        model.addAttribute("items", items);
        model.addAttribute("itemSearchDto", itemSearchDto);
        model.addAttribute("maxPage", 5);

        return "item/itemMng";
    }

    @GetMapping(value = "/item/{itemId}")
    public String itemDtl(Model model, Principal principal, @PathVariable("itemId") Long itemId) {
        ItemFormDto itemFormDto = itemService.getItemDtl(itemId);
        List<ReviewItemDto> orderItemDtoList = reviewService.getReviewItem(itemId);
        List<ReviewImgDto> reviewImgDtoList = reviewService.getReviewItemImg(itemId);

        model.addAttribute("item", itemFormDto);
        model.addAttribute("orderItemList", orderItemDtoList);
        model.addAttribute("reviewImgDtoList", reviewImgDtoList);

        return "item/itemDtl";
    }

    @ResponseBody
    @GetMapping(value = "/admin/item/newSearch")
    public List<NaverApiDto> getItems(@RequestParam("title") String query) {
        return naverShopSearch.search(query);
    }

    @ResponseBody
    @GetMapping(value = "/item/{itemId}/price")
    public List<NaverApiDto> getPrice(@RequestParam("itemNm") String query) {
        return naverShopSearch.search2(query);
    }

}
