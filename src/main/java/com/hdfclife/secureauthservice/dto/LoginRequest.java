package com.hdfclife.secureauthservice.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {

    @NotBlank(message = "User ID cannot be blank")
    private String userId;

    @NotBlank(message = "Password cannot be blank")
    private String password;
}
