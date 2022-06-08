package com.shop.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartDetailDto {

    private Long cartItemId;

    private String itemNm;

    private int price;

    private int count;

    private String imgUrl;

}
