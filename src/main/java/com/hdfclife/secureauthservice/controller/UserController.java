package com.hdfclife.secureauthservice.controller;

import com.hdfclife.secureauthservice.dto.UserDetailsResponse;
import com.hdfclife.secureauthservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/myDetails")
    public ResponseEntity<UserDetailsResponse> getUserDetails(Principal principal) {
        return ResponseEntity.ok(userService.getUserDetails(principal.getName()));
    }
}
