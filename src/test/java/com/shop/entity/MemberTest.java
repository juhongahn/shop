package com.shop.entity;

import com.shop.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;

@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.yml")
public class MemberTest {

    @Autowired
    private MemberRepository memberRepository;

    @PersistenceContext
    private EntityManager em;

    @Test
    @DisplayName("Auditing 테스트")
    @WithMockUser(username = "king", roles = "USER")
    public void auditingTest() {
        Member member = new Member();

        memberRepository.save(member);

        em.flush();
        em.clear();

        Member foundMember = memberRepository.findById(member.getId()).orElseThrow(EntityNotFoundException::new);

        System.out.println(foundMember.getCreatedBy());
        System.out.println(foundMember.getModifiedBy());
        System.out.println(foundMember.getRegTime());
        System.out.println(foundMember.getUpdateTime());
    }
}
