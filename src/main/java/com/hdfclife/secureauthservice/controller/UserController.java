//package com.hdfclife.secureauthservice.controller;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@RequestMapping("/api/user")
//@RequiredArgsConstructor
//public class UserController {
//
//    private final UserService userService;
//
//    @GetMapping("/me")
//    public ResponseEntity<SignupResponse> getUserDetails(Principal principal) {
//        return ResponseEntity.ok(userService.getUserDetails(principal.getName()));
//    }
//}
