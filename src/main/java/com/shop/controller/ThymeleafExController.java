package com.shop.controller;

import com.shop.dto.ItemDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/thymeleaf")
public class ThymeleafExController {

  @GetMapping("/ex01")
  public String thymeleafExample01(Model model) {
    model.addAttribute("data", "타임리프 예제입니다.");
    return "thymeleafEx/thymeleafEx01";
  }

  @GetMapping("/ex02")
  public String thymeleafExample02(Model model) {
    ItemDto itemDto = ItemDto.builder()
            .itemDetail("상품 상세 설명")
            .itemNm("테스트 상품1")
            .price(10000)
            .regTime(LocalDateTime.now())
            .build();
    model.addAttribute("itemDto", itemDto);
    return "thymeleafEx/thymeleafEx02";
  }

  @GetMapping("/ex03")
  public String thymeleafExample03(Model model) {
    List<ItemDto> itemDtoList = new ArrayList<>(10);
    for(int i=1; i<=10; i++) {
      ItemDto itemDto = ItemDto.builder()
              .itemDetail("상품 상세 설명"+i)
              .itemNm("테스트 상품"+i)
              .price(10000*i)
              .regTime(LocalDateTime.now())
              .build();
      itemDtoList.add(itemDto);
    }

    model.addAttribute("itemDtoList", itemDtoList);
    return "thymeleafEx/thymeleafEx03";
  }
  @GetMapping("/ex04")
  public String thymeleafExample04(Model model) {
    List<ItemDto> itemDtoList = new ArrayList<>(10);
    for(int i=1; i<=10; i++) {
      ItemDto itemDto = ItemDto.builder()
              .itemDetail("상품 상세 설명"+i)
              .itemNm("테스트 상품"+i)
              .price(10000*i)
              .regTime(LocalDateTime.now())
              .build();
      itemDtoList.add(itemDto);
    }

    model.addAttribute("itemDtoList", itemDtoList);
    return "thymeleafEx/thymeleafEx04";
  }

  @GetMapping("/ex05")
  public String thymeleafExample05(Model model) {
    return "thymeleafEx/thymeleafEx05";
  }
  @GetMapping("/ex06")
  public String thymeleafExample06(String param1, String param2, Model model) {
    model.addAttribute("param1" ,param1);
    model.addAttribute("param2" ,param2);
    return "thymeleafEx/thymeleafEx06";
  }
  @GetMapping("/ex07")
  public String thymeleafExample07(String param1, String param2, Model model) {
    return "thymeleafEx/thymeleafEx07";
    // "thymeleafEx/thymeleafEx07"은 layout1.html의 content 부분에 들어가게 된다.
  }
}