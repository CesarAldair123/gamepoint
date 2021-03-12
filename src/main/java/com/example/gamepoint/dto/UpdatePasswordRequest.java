package com.example.gamepoint.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdatePasswordRequest {
    private String actualPassword;
    private String newPassword;
    private String repeatPassword;
}
