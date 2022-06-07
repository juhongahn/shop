package com.shop.dto;

import com.shop.constant.ItemSellStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemSearchDto {

    private String searchDateType;
    private ItemSellStatus searchSellStatus;
    private String searchBy;
    private String searchQuery="";

}
