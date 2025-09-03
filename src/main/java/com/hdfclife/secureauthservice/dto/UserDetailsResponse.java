package com.hdfclife.secureauthservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class UserDetailsResponse {

    private String firstName;
    private String lastName;
    private String email;
    private LocalDate dob;
    private String userId;

}
