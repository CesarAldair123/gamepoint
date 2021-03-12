package com.example.gamepoint.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FreeGameCodeDto {
    private int id;
    private String code;
    private String gameImg;
    private String gameName;
    private boolean used;
}
