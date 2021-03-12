package com.example.gamepoint.dto;

import com.example.gamepoint.model.Game;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Cart {
    private List<CartItem> items = new ArrayList<>();
    private int totalGames;
    private double total;
}
