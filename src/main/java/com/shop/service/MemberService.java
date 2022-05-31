package com.shop.service;

import com.shop.entity.Member;
import org.springframework.security.core.userdetails.UserDetails;

public interface MemberService {

    Member saveMember(Member member);

    void validateDuplicateMember(Member member);


}
