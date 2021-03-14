package com.example.gamepoint.dto;

import com.example.gamepoint.model.Game;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartRentItem {
    private Game game;
    private int quantity;
    private Date finalMonth;
    private String dateInString;
    private double total;
}
