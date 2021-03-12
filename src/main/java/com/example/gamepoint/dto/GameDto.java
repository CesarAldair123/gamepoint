package com.example.gamepoint.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GameDto {
    private int id;
    private String name;
    private String devName;
    private String desc;
    private String imgUrl;
    private String provider;
    private int stock;
    private boolean forRental;
    private double pricePerMonth;
    private double price;
}
