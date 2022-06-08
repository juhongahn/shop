package com.shop.repository;

import com.shop.entity.Order;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {


    // 조회 조건이 복잡하지 않다면 Querydsl대신 Query어노테이션을 사용해도 좋다.
    // 이메일을 통해 해당 유저의 주문내역을 불러온다.
    @Query("SELECT o FROM Order o " +
            "WHERE o.member.email = :email " +
            "ORDER BY o.orderDate desc ")
    List<Order> findOrders(@Param("email") String email, Pageable pageable);

    @Query("SELECT COUNT(o) FROM Order o WHERE o.member.email = :email")
    Long countOrder(@Param("email") String email);

}
