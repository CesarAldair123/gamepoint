package com.example.gamepoint.dto;

import com.example.gamepoint.model.Game;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartItem {
    private Game game;
    private double total;
    private int quantity;
}
