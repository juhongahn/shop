package com.shop.entity;

import com.shop.dto.MemberFormDto;
import com.shop.repository.CartRepository;
import com.shop.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.yml")
public class CartTest {
    
    @Autowired
    CartRepository cartRepository;
    
    @Autowired
    MemberRepository memberRepository;
    
    @Autowired
    PasswordEncoder passwordEncoder;
    
    @PersistenceContext
    EntityManager entityManager;

    public Member createMember() {
        MemberFormDto memberFormDto = MemberFormDto.builder()
                .email("test@email.com")
                .name("홍길동")
                .address("부산광역시 금정구 장전동")
                .password("1234")
                .build();

        return Member.createMember(memberFormDto, this.passwordEncoder);
    }
    
    @Test
    @DisplayName("장바구니 회원 엔티티 매핑 조회 테스트")
    public void findCartAndMemberTest() {
        Member member = createMember();
        memberRepository.save(member);

        Cart cart = new Cart();
        cart.setMember(member);
        cartRepository.save(cart);

        // JPA는 1차 캐시가 비어있으면 db를 조회하므로, 강제로 영속성 컨텍스트를 비워주자.
        entityManager.flush();
        entityManager.clear();

        Cart savedCart = cartRepository.findById(cart.getId()).orElseThrow(
                () -> new EntityNotFoundException());

        assertEquals(savedCart.getMember().getId(), member.getId());

    }
    
}
