package com.example.gamepoint.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SaleDetailsDto {
    private int id;
    private String gameImg;
    private String gameName;
    private int quantity;
    private double total;
}
