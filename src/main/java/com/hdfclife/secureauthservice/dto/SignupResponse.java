package com.hdfclife.secureauthservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignupResponse {

    private String message;
    private int status;
    private List<String> suggestions;
}
