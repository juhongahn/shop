package com.shop.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "cart_item")
public class CartItem extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "cart_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id")
    private Cart cart;


    // 하나의 상품은 여러 장바구니 상품에 등록 될 수 있다. 과자 -> a의 장바구니상품, b의 장바구니 상품, ...
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    private int count;

    public static CartItem createCartItem(Cart cart, Item item, int count){
        CartItem cartItem = new CartItem();
        cartItem.setItem(item);
        cartItem.setCart(cart);
        cartItem.setCount(count);
        return cartItem;
    }

    /*
    * 장보구니에 이미 담겨있는 상품인데, 해당 상품을 추가로 장바구니에 담을 떄 기존수량에 현제 담을 수량을 더해줄 때 사용
    * */
    public void addCount(int count){
        this.count += count;
    }

}
