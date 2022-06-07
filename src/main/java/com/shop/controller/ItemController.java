package com.shop.controller;

import com.shop.dto.ItemDto;
import com.shop.dto.ItemFormDto;
import com.shop.dto.ItemSearchDto;
import com.shop.entity.Item;
import com.shop.service.ItemService;
import lombok.NoArgsConstructor;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @GetMapping("/admin/item/new")
    public String itemForm(Model model) {
        model.addAttribute("itemFormDto",new ItemFormDto());
        return "item/itemForm";
    }

    @PostMapping("/admin/item/new")
    public String itemNew(@Valid ItemFormDto itemFormDto, BindingResult bindingResult, Model model,
                          @RequestParam("itemImgFile") List<MultipartFile> itemImgFileList){
        if (bindingResult.hasErrors()) {
            return "item/itemForm";
        }

        if(itemImgFileList.get(0).isEmpty() && itemFormDto.getId() == null) {
            model.addAttribute("errorMessage", "첫번째 상품 이미지는 필수 입력 값 입니다.");
            return "item/itemForm";
        }
        try{
            itemService.saveItem(itemFormDto, itemImgFileList);

        } catch (IOException e) {
            model.addAttribute("errorMessage", "상품 등록 중 에러가 발생 하였습니다.");
            return "item/itemForm";
        }

        return "redirect:/";
    }

    @GetMapping("/admin/item/{itemId}")
    public String itemDtl(@PathVariable Long itemId, Model model){

        // 아이템 수정페이지를 아이템 생성 페이지와 공유해서 쓴다.
        // 아이템이 존재한다면 채워진 커맨드객체가 넘어갈 것이고
        // 아이템이 존재하지 않는다면 에러메세지와 함께 빈 커맨드객체가 넘어갈 것이다.

        try {
            ItemFormDto itemFormDto = itemService.getItemDtl(itemId);
            model.addAttribute("itemFormDto", itemFormDto);
        } catch (EntityNotFoundException e) {
            model.addAttribute("errorMessage", "존재하지 않는 상품 입니다.");
            // 아이템이 존재하지 않는다면 새로운 ItemFormDto 객체를 만들어서 뷰에 쏴주고 다시 뷰를 띄워준다.
            model.addAttribute("itemFormDto", new ItemFormDto());
            return "item/itemForm";
        }

        return "item/itemForm";

    }

    @PostMapping("/admin/item/{itemId}")
    public String itemUpdate(@Valid ItemFormDto itemFormDto, BindingResult bindingResult,
                             @RequestParam("itemImgFile") List<MultipartFile> itemImgFileList, Model model) {

        if (bindingResult.hasErrors()) {
            return "item/itemForm";
        }
        if (itemImgFileList.get(0).isEmpty() && itemFormDto.getId() == null) {
            model.addAttribute("errorMessage", "첫번째 상품 이미지는 필수 입력 값 입니다.");
            return "item/itemForm";
        }

        try {
            itemService.updateItem(itemFormDto, itemImgFileList);
        } catch (Exception e) {
            model.addAttribute("errorMessage", "상품 수정중 에러가 발생했습니다.");
            return "item/itemForm";
        }

        return "redirect:/";

    }

    @GetMapping(value = {"/admin/items", "/admin/items/{page}"})
    public String itemManage(ItemSearchDto itemSearchDto, @PathVariable("page")Optional<Integer> page, Model model){
        Pageable pageable = PageRequest.of(page.orElse(0), 3);

        Page<Item> items = itemService.getAdminItemPage(itemSearchDto, pageable);
        model.addAttribute("items", items)
                // 페이지 전환시 기존 검색 조건을 유지한 채 이동할 수 있도록 뷰에 다시 전달.
                .addAttribute("itemSearchDto", itemSearchDto)
                .addAttribute("maxPage", 5);

        return "item/itemMng";
    }

    @GetMapping("/item/{itemId}")
    public String itemDtl(Model model, @PathVariable("itemId") Long itemId){
        ItemFormDto itemFormDto = itemService.getItemDtl(itemId);
        model.addAttribute("item", itemFormDto);
        return "item/itemDtl";
    }

}
