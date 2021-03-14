package com.example.gamepoint.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RentGameRequest {
    private int quantity;
    private int gameId;
    private String finalMonth;
}
