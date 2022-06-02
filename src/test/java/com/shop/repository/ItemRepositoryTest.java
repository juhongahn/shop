package com.shop.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.shop.constant.ItemSellStatus;
import com.shop.entity.Item;
import com.shop.entity.QItem;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.TestPropertySource;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.yml")
class ItemRepositoryTest {

    @Autowired
    ItemRepository itemRepository;

    @PersistenceContext
    EntityManager em;


    public void createItemList() {
        for (int i = 0; i < 10; i++) {
            Item item = Item.builder()
                    .itemNm("테스트 상품" + i)
                    .price(10000 + i)
                    .itemDetail("테스트 상품 상세 설명" + i)
                    .itemSellStatus(ItemSellStatus.SELL)
                    .stockNumber(100)
                    .build();
            Item savedItem = itemRepository.save(item);
        }
    }

    public void createItemList2() {
        for (int i = 1; i <= 5; i++) {
            Item item = Item.builder()
                    .itemNm("테스트 상품" + i)
                    .price(10000 + i)
                    .itemDetail("테스트 상품 상세 설명" + i)
                    .itemSellStatus(ItemSellStatus.SELL)
                    .stockNumber(100)
                    .build();
            Item savedItem = itemRepository.save(item);
        }

        for (int i = 6; i <= 10; i++) {
            Item item = Item.builder()
                    .itemNm("테스트 상품" + i)
                    .price(10000 + i)
                    .itemDetail("테스트 상품 상세 설명" + i)
                    .itemSellStatus(ItemSellStatus.SOLD_OUT)
                    .stockNumber(0)
                    .build();
            Item savedItem = itemRepository.save(item);
        }

    }

    @Test
    @DisplayName("상품 Querydsl 조회 테스트2")
    public void queryDslTest2() {

        this.createItemList2();

        BooleanBuilder booleanBuilder = new BooleanBuilder();
        QItem qItem = QItem.item;

        String itemDetail = "테스트 상품 상세 설명";
        int price = 10003;
        String itemSellState = "SELL";

        booleanBuilder.and(qItem.itemDetail.like("%" + itemDetail + "%"));
        booleanBuilder.and(qItem.price.gt(price));

        if (StringUtils.equals(itemSellState, ItemSellStatus.SELL)) {
            booleanBuilder.and(qItem.itemSellStatus.eq(ItemSellStatus.SELL));
        }

        Pageable pageable = PageRequest.of(0, 5);
        Page<Item> itemPagingResult =
                itemRepository.findAll(booleanBuilder, pageable);
        System.out.println("total elements : " +
                itemPagingResult.getTotalElements());

        List<Item> resultItemList = itemPagingResult.getContent();
        for (Item resultItem : resultItemList) {
            System.out.println(resultItem.toString());
        }

    }

    @Test
    @DisplayName("Querydsl 조회 테스트1")
    public void queryDslTest() {
        this.createItemList();
        JPAQueryFactory jpaQueryFactory = new JPAQueryFactory(em);
        QItem qItem = QItem.item;
        JPAQuery<Item> query = jpaQueryFactory.selectFrom(qItem)
                .where(qItem.itemSellStatus.eq(ItemSellStatus.SELL))
                .where(qItem.itemDetail.like("%" + "테스트 상품 상세 설명" + "%"))
                .orderBy(qItem.price.desc());

        List<Item> itemList = query.fetch();

        for (Item item : itemList) {
            System.out.println(item.toString());
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
                .build();

        Item savedItem = itemRepository.save(item);
        Item foundItem = itemRepository.findById(savedItem.getId()).orElseGet(() -> null);
        assertNotNull(foundItem);
        assertEquals(savedItem.getId(), foundItem.getId());
    }

    @Test
    @DisplayName("상품명 조회 테스트")
    void findByItemNm() {
        createItemList();
        assertDoesNotThrow(() -> itemRepository.findByItemNm("테스트 상품1"));
        List<Item> itemList = itemRepository.findByItemNm("테스트 상품1").orElseThrow(
                () -> {
                    throw new RuntimeException("해당 상품이 없습니다.");
                });
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
        for (Item item : itemList) {
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

    @Test
    @DisplayName("@Query를 사용한 상품 조회 테스트")
    void findByItemDetail() {
        this.createItemList();
        List<Item> itemList = itemRepository.findByItemDetail("테스트 상품 상세 설명");
        assertEquals(10, itemList.size());
        assertEquals(10009, itemList.get(0).getPrice());
    }

    @Test
    @DisplayName("nativeQuery 속성을 이용한 상품 조회 테스트")
    void findByItemDetailByNative() {
        this.createItemList();
        List<Item> itemList = itemRepository.findByItemDetailByNative("테스트 상품 상세 설명");
        assertEquals(10, itemList.size());
        assertEquals(10009, itemList.get(0).getPrice());
    }
}