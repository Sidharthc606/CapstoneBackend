package com.hdfclife.secureauthservice.service;

import com.hdfclife.secureauthservice.dto.LoginRequest;
import com.hdfclife.secureauthservice.dto.LoginResponse;
import com.hdfclife.secureauthservice.dto.LogoutRequest;
import com.hdfclife.secureauthservice.dto.RefreshRequest;
import com.hdfclife.secureauthservice.entity.Member;
import com.hdfclife.secureauthservice.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class AuthService{

    private final MemberRepository memberRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    private final Set<String> validRefreshTokens = ConcurrentHashMap.newKeySet();

    public LoginResponse login(LoginRequest request) {
        Member member = memberRepository.findByUserId(request.getUserId())
                .orElseThrow(() -> new RuntimeException("Invalid userId or password"));

        if (!passwordEncoder.matches(request.getPassword(), member.getPassword())) {
            throw new RuntimeException("Invalid userId or password");
        }

        String accessToken = jwtService.generateAccessToken(member.getUserId());
        String refreshToken = jwtService.generateRefreshToken(member.getUserId());

        validRefreshTokens.add(refreshToken);

        LoginResponse response = new LoginResponse();
        response.setAccessToken(accessToken);
        response.setRefreshToken(refreshToken);
        return response;
    }

    public LoginResponse refreshToken(RefreshRequest request) {
        String refreshToken = request.getRefreshToken();

        if (!validRefreshTokens.contains(refreshToken) || !jwtService.validateToken(refreshToken)) {
            throw new RuntimeException("Invalid refresh token");
        }

        String userId = jwtService.getUserIdFromToken(refreshToken);
        String newAccessToken = jwtService.generateAccessToken(userId);
        String newRefreshToken = jwtService.generateRefreshToken(userId);

        validRefreshTokens.remove(refreshToken);
        validRefreshTokens.add(newRefreshToken);

        LoginResponse response = new LoginResponse();
        response.setAccessToken(newAccessToken);
        response.setRefreshToken(newRefreshToken);
        return response;
    }

    public void logout(LogoutRequest request) {
        validRefreshTokens.remove(request.getRefreshToken());
    }

}
