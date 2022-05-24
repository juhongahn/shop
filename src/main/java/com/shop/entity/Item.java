package com.shop.entity;


import com.shop.constant.ItemSellStatus;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "item")
public class Item {
  @Id
  @Column(name = "item_id")
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @Column(nullable = false, length = 50)
  private String itemNm;

  @Column(nullable = false, name = "price")
  private int price;

  @Column(nullable = false)
  private int stockNumber;

  @Lob
  @Column(nullable = false)
  private String itemDetail;

  @Enumerated(EnumType.STRING)
  private ItemSellStatus itemSellStatus;

  private LocalDateTime regTime;
  private LocalDateTime updateTime;
}
