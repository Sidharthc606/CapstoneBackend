package com.hdfclife.secureauthservice.dto;

import lombok.Data;

@Data
public class LogoutRequest {

    private String refreshToken;
}
