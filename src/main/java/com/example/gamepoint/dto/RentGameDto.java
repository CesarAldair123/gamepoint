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
public class RentGameDto {
    private int id;
    private int userId;
    private int gameId;
    private String username;
    private String gameImg;
    private String gameName;
    private boolean wasReturned;
    private Date firstMonth;
    private Date lastMonth;
    private int quantity;
    private double total;
}
