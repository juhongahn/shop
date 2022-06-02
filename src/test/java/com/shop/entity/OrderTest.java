package com.shop.entity;

import com.shop.constant.ItemSellStatus;
import com.shop.repository.ItemRepository;
import com.shop.repository.MemberRepository;
import com.shop.repository.OrderItemRepository;
import com.shop.repository.OrderRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.yml")
public class OrderTest {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @PersistenceContext
    private EntityManager em;

    public Item createItem() {
        Item item = Item.builder()
                .itemNm("테스트 상품")
                .price(10000)
                .itemDetail("상세설명")
                .itemSellStatus(ItemSellStatus.SELL)
                .stockNumber(100)
                .regTime(LocalDateTime.now())
                .updateTime(LocalDateTime.now())
                .build();
        return item;
    }

    public Order createOrder() {
        Order order = new Order();

        for (int i=0;i<3;i++) {
            Item item = createItem();
            itemRepository.save(item);
            OrderItem orderItem = OrderItem.builder()
                    .item(item)
                    .count(10)
                    .orderPrice(1000)
                    .order(order)
                    .build();
            order.getOrderItemList().add(orderItem);
        }

        Member member = new Member();
        memberRepository.save(member);
        order.setMember(member);
        orderRepository.save(order);

        return order;
    }

    @Test
    @DisplayName("영속성 전이 테스트")
    public void cascadeTest() {

        Order order = new Order();

        for (int i=0; i<3;i++){
            Item item = createItem();
            itemRepository.save(item);
            OrderItem orderItem = OrderItem.builder()
                    .item(item)
                    .count(10)
                    .orderPrice(1000)
                    .order(order)
                    .build();
            order.getOrderItemList().add(orderItem);
        }

        orderRepository.saveAndFlush(order);
        em.clear();

        // order 객체에 orderItem 객체를 주입후 order 객체만 저장했음에도 orderItem 도 함께 저장됨을 볼 수 있다.

        Order savedOrder = orderRepository.findById(order.getId()).orElseThrow(EntityNotFoundException::new);
        assertEquals(savedOrder.getOrderItemList().size(), order.getOrderItemList().size());

    }

    @Test
    @DisplayName("고아 객체 제거 테스트")
    public void orphanRemovalTest() {

        // 영속 상태
        Order order = createOrder();

        // 부모 엔티티에서 orderItem을 요소 한개를 삭제했기 때문에,
        // 자식 orderItem 은 참조 받는곳이 없어지기 때문에
        // 고아 객체 제거 기능에의해 삭제된다.
        order.getOrderItemList().remove(0);
        em.flush();

        Order foundOrder = orderRepository.findById(order.getId()).orElseThrow(EntityNotFoundException::new);

        assertEquals(2, foundOrder.getOrderItemList().size());

    }

    // 지연 로딩은 DB에 값을 가져올 때 참조관계에 있는 객체를 바로 가져온느것이 아니라, get...와 같이
    // 그 객체 호출이 있을 때서야 가져온다.
    @Test
    @DisplayName("지연 로딩 테스트")
    public void lazyLoadingTest() {
        Order order = createOrder();

        Long orderItemId = order.getOrderItemList().get(0).getId();
        em.flush();
        em.clear();

        OrderItem orderItem = orderItemRepository.findById(orderItemId).orElseThrow(EntityNotFoundException::new);

//        // 지연로딩 적용하기전 (디폴트로 eager)에는 order 객체와 관계를 갖는 Member, OrderItem, Item, Orders 테이블을
//        // 조인해서 한꺼번에 가져온다.
//        System.out.println("Order class: " +
//                orderItem.getOrder().getClass());

        System.out.println("Order class: " + orderItem.getOrder().getClass());
        System.out.println("===================================");
        orderItem.getOrder().getOrderDate();
        System.out.println("===================================");

        // Member 엔티티, Item 엔티티를 가져오지 않는다!
    }

}
