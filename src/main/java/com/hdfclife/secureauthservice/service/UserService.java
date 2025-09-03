package com.hdfclife.secureauthservice.service;

import com.hdfclife.secureauthservice.dto.UserDetailsResponse;
import com.hdfclife.secureauthservice.entity.Member;
import com.hdfclife.secureauthservice.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final MemberRepository memberRepository;

    public UserDetailsResponse getUserDetails(String userId) {
        Member member = memberRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return new UserDetailsResponse(
                member.getFirstName(),
                member.getLastName(),
                member.getEmail(),
                member.getDob(),
                member.getUserId()

        );
    }
}
