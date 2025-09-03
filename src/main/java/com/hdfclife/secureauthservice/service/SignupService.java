package com.hdfclife.secureauthservice.service;

import com.hdfclife.secureauthservice.dto.SignupRequest;
import com.hdfclife.secureauthservice.dto.SignupResponse;
import com.hdfclife.secureauthservice.entity.Member;
import com.hdfclife.secureauthservice.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@RequiredArgsConstructor
public class SignupService {

    private final MemberRepository memberRepository;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    private final Random random = new Random();

    public SignupResponse registerUser(SignupRequest request) {

        Set<String> suggestions = new LinkedHashSet<>();
        while (suggestions.size() < 3) {
            suggestions.add(request.getFirstName() + (100 + random.nextInt(900)));
        }

        List<String> candidates = new ArrayList<>(suggestions);
        candidates.add(request.getUserId());

        List<String> existingUserIds = memberRepository.findAllExistingUserIds(candidates);

        if (!existingUserIds.contains(request.getUserId())) {
            Member user = new Member();
            user.setFirstName(request.getFirstName());
            user.setLastName(request.getLastName());
            user.setEmail(request.getEmail());
            user.setUserId(request.getUserId());
            user.setPassword(request.getPassword());
            user.setDob(LocalDate.parse(request.getDob(), formatter));

            memberRepository.save(user);

            return new SignupResponse("Signup successful", 200, null);
        }

        suggestions.removeAll(existingUserIds);

        while (suggestions.size() < 3) {
            String candidate = request.getFirstName() + (100 + random.nextInt(900));
            if (!existingUserIds.contains(candidate)) {
                suggestions.add(candidate);
            }
        }

        return new SignupResponse("UserId already exists", 409, new ArrayList<>(suggestions));
    }
}
