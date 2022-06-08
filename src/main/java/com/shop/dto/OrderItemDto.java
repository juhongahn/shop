package com.shop.dto;

import com.shop.entity.OrderItem;
import lombok.Data;


/*
* 조회한 주문 데이터를 화면에 보낼 때  사용할 DTO 클래스
* */
@Data
public class OrderItemDto {

    public OrderItemDto(OrderItem orderItem, String imgUrl){
        this.itemNm = orderItem.getItem().getItemNm();
        this.count = orderItem.getCount();
        this.orderPrice = orderItem.getOrderPrice();
        this.imgUrl = imgUrl;
    }

    private String itemNm;
    private int count;
    private int orderPrice;
    private String imgUrl;
}
