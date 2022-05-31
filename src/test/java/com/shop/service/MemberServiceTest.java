package com.shop.service;

import com.shop.dto.MemberFormDto;
import com.shop.entity.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@TestPropertySource(locations = "classpath:application-test.yml")
@Transactional
@SpringBootTest
class MemberServiceTest {

    @Autowired
    private MemberService memberService;

    @Autowired
    PasswordEncoder passwordEncoder;

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
    void saveMember() {
        Member member = createMember();
        Member savedMember = memberService.saveMember(member);

        assertThat(member.getEmail(), is(savedMember.getEmail()));
        assertThat(member.getName(), is(savedMember.getName()));
        assertThat(member.getAddress(), is(savedMember.getAddress()));
        assertThat(member.getPassword(), is(savedMember.getPassword()));
        System.out.println(member.getPassword() + "|" + savedMember.getPassword());
        assertThat(member.getRole(), is(savedMember.getRole()));

    }

    @Test
    @DisplayName("중복 회원 가입 테스트")
    public void saveDuplicateMemberTest() {
        Member member1 = createMember();
        Member member2 = createMember();

        memberService.saveMember(member1);

        Throwable e = assertThrows(IllegalStateException.class, ()-> {memberService.saveMember(member2);});

        assertEquals("이미 가입된 회원입니다.", e.getMessage());
    }
}