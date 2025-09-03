package com.hdfclife.secureauthservice.service;

import com.hdfclife.secureauthservice.dto.LoginRequest;
import com.hdfclife.secureauthservice.dto.LoginResponse;
import com.hdfclife.secureauthservice.dto.LogoutRequest;
import com.hdfclife.secureauthservice.dto.RefreshRequest;
import com.hdfclife.secureauthservice.entity.Member;
import com.hdfclife.secureauthservice.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final MemberRepository memberRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    private final Set<String> validRefreshTokens = ConcurrentHashMap.newKeySet();

    public LoginResponse login(LoginRequest request) {
        // Authenticate using Spring Security
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUserId(),
                        request.getPassword()
                )
        );

        Member member = memberRepository.findByUserId(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        String accessToken = jwtService.generateAccessToken(member.getUserId());
        String refreshToken = jwtService.generateRefreshToken(member.getUserId());

        validRefreshTokens.add(refreshToken);

        return new LoginResponse(accessToken, refreshToken, "Bearer");
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

        return new LoginResponse(newAccessToken, newRefreshToken, "Bearer");
    }

    public void logout(LogoutRequest request) {
        validRefreshTokens.remove(request.getRefreshToken());
    }
}
