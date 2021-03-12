package com.example.gamepoint.controller;

import com.example.gamepoint.dto.GameDto;
import com.example.gamepoint.service.AuthenticationService;
import com.example.gamepoint.service.CartService;
import com.example.gamepoint.service.GameService;
import com.example.gamepoint.service.SearchGameService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Controller
public class HomeController {

    private AuthenticationService authenticationService;
    private SearchGameService searchGameService;
    private GameService gameService;

    @GetMapping({"/home","/"})
    public String getHome(@RequestParam Optional<String> name, Model model){
        if(authenticationService.actualUserIsAdmin()){
            return "/admin";
        }
        String game = name.orElse("");
        List<GameDto> games = searchGameService.searchGames(game);
        model.addAttribute("games", games);
        return "/home";
    }

    @GetMapping("/game/{id}")
    public String getGame(@PathVariable int id, Model model){
        GameDto gameDto = gameService.getGameById(id);
        model.addAttribute("game",gameDto);
        return "/game";
    }

}
