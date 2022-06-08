package com.shop.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "order_item")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderItem extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "order_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    private int orderPrice;

    private int count;

    public static  OrderItem createOrderItem(Item item, int count) {

        OrderItem orderItem = OrderItem.builder()
                .item(item)
                .count(count)
                .orderPrice(item.getPrice())
                .build();

        item.removeStock(count);
        return orderItem;
    }

    public int getTotalPrice(){
        return orderPrice * count;
    }

    /*
    * 주문 취소했을 때 해당 orderItem의 count만큼 재고수를 올려줘야한다.
    * */
    public void cancel() {
        this.getItem().addStock(count);
    }
}
