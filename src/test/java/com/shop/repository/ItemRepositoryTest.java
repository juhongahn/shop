package com.shop.repository;

import com.shop.constant.ItemSellStatus;
import com.shop.entity.Item;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.yml")
class ItemRepositoryTest {

  @Autowired
  ItemRepository itemRepository;

  public void createItemList() {
    for (int i=0; i<10;i++) {
      Item item = Item.builder()
              .itemNm("테스트 상품" + i)
              .price(10000 + i)
              .itemDetail("테스트 상품 상세 설명" + i)
              .itemSellStatus(ItemSellStatus.SELL)
              .stockNumber(100)
              .regTime(LocalDateTime.now())
              .updateTime(LocalDateTime.now())
              .build();
      Item savedItem = itemRepository.save(item);
    }
  }
  
  @Test                           
  @DisplayName("상품 저장 테스트")
  public void createItemTest() {
    Item item = Item.builder()
            .itemNm("테스트 상품")
            .price(10000)
            .itemDetail("테스트 상품 상세 설명")
            .itemSellStatus(ItemSellStatus.SELL)
            .stockNumber(100)
            .regTime(LocalDateTime.now())
            .updateTime(LocalDateTime.now())
            .build();
    
    Item savedItem = itemRepository.save(item);
    Item foundItem = itemRepository.findById(savedItem.getId()).orElseGet(()->null);
    assertNotNull(foundItem);
    assertEquals(savedItem.getId(), foundItem.getId());
  }

  @Test
  @DisplayName("상품명 조회 테스트")
  void findByItemNm() {
    createItemList();
    assertDoesNotThrow(()->itemRepository.findByItemNm("테스트 상품1"));
    List<Item> itemList = itemRepository.findByItemNm("테스트 상품1").orElseThrow(
            () -> {throw new RuntimeException("해당 상품이 없습니다.");});
    assertEquals(1, itemList.size());
    assertEquals("테스트 상품1", itemList.get(0).getItemNm());
  }

  @Test
  @DisplayName("상품명, 상품상세설명 'or' 테스트")
  void findByItemNmOrDetail() {
    this.createItemList();
    List<Item> itemList =
            itemRepository.findByItemNmOrItemDetail("테스트 상품1", "테스트 상품 상세 설명5");
    assertEquals(2, itemList.size());
  }

  @Test
  @DisplayName("가격 LessThan 테스트")
  void findByPriceLessThan() {
    this.createItemList();
    List<Item> itemList = itemRepository.findByPriceLessThan(10005);
    assertEquals(5, itemList.size());
    for(Item item : itemList) {
      System.out.println(item.getPrice());
    }

  }

  @Test
  @DisplayName("가격 내림차순 조회 테스트")
  void findByPriceLessThanOrderByPriceDesc() {
    this.createItemList();
    List<Item> itemList = itemRepository.findByPriceLessThanOrderByPriceDesc(10005);
    assertEquals(10004, itemList.get(0).getPrice());
  }
}