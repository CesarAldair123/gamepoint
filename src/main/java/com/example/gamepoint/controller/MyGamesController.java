package com.example.gamepoint.controller;

import com.example.gamepoint.dto.GameDto;
import com.example.gamepoint.service.MyGamesService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@AllArgsConstructor
@Controller
public class MyGamesController {

    private MyGamesService myGamesService;

    /*@GetMapping("/mygames")
    public String getMyGames(Model model){
        List<GameDto> games = myGamesService.getGamesFromActualUser();
        model.addAttribute("games",games);
        return "myGames";
    }*/
}
