package com.shop.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.yml")
class ItemRepositoryTest {

  @Autowired
  ItemRepository itemRepository;
  
  @Test
  @DisplayName("상품 저장 테스트")
  public void createItemTest() {

  }

}