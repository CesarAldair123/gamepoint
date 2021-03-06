package com.example.gamepoint.controller;

import com.example.gamepoint.dto.GameDto;
import com.example.gamepoint.dto.UpdatePasswordRequest;
import com.example.gamepoint.dto.UserDto;
import com.example.gamepoint.service.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@AllArgsConstructor
@Controller
@RequestMapping("/profile")
public class ProfileController {

    private ProfileService profileService;
    private UserService userService;
    private GameService gameService;
    private SaleService saleService;
    private RentGameService rentGameService;

    @GetMapping()
    private String getProfile(Model model){
        model.addAttribute("user", profileService.getActualUser());
        model.addAttribute("orders",profileService.getSales());
        model.addAttribute("games", userService.getGamesByActualUser());
        model.addAttribute("rented",saleService.getRentGamesReturnedFalseAndUser());
        return "/profile";
    }

    @PostMapping("/update")
    private String saveProfile(UserDto userDto, Model model){
        profileService.saveProfile(userDto);
        model.addAttribute("user", profileService.getActualUser());
        model.addAttribute("orders",profileService.getSales());
        model.addAttribute("profilemessage","Profile Updated!");
        return "/profile";
    }

    @PostMapping("/password")
    private String updatePassword(UpdatePasswordRequest updatePasswordRequest, Model model){
        model.addAttribute("user", profileService.getActualUser());
        model.addAttribute("passwordmessage",profileService.updatePassword(updatePasswordRequest));
        model.addAttribute("orders",profileService.getSales());
        return "/profile";
    }

    @GetMapping("/order/{id}")
    private String getOrder(@PathVariable int id, Model model){
        model.addAttribute("details", profileService.getSaleDetails(id));
        model.addAttribute("order",profileService.getSale(id));
        model.addAttribute("rents",saleService.getRentGamesBySaleId(id));
        return "/order";
    }

    @GetMapping("/game/add")
    private String getAddGame(){
        return "/userAddGame";
    }

    @PostMapping("/game/add")
    private String postAddGame(GameDto gameDto, Model model){
        int id = gameService.addGame(gameDto);
        model.addAttribute("game", gameService.getGameById(id));
        model.addAttribute("message","Game Created");
        return "/userEditGame";
    }

    @GetMapping("/game/{id}")
    private String getEditGame(@PathVariable int id, Model model){
        model.addAttribute("game",gameService.getGameById(id));
        return "/userEditGame";
    }

    @PostMapping("/game/{id}")
    private String postEditGame(@PathVariable int id,GameDto gameDto, Model model){
        gameService.updateGame(gameDto);
        model.addAttribute("game",gameService.getGameById(id));
        model.addAttribute("message","Game Edited");
        return "/userEditGame";
    }

    @GetMapping("/game/sales/{id}")
    private String postEditGame(@PathVariable int id,Model model){
        model.addAttribute("sales", saleService.getSaleDetailsByGameId(id));
        model.addAttribute("game",gameService.getGameById(id));
        model.addAttribute("rents",saleService.getRentGamesByGameId(id));
        return "/userSales";
    }

    @PostMapping("/return")
    private String returnGame(int gameId, Model model){
        System.out.println(gameId);
        rentGameService.returnGame(gameId);
        model.addAttribute("user", profileService.getActualUser());
        model.addAttribute("orders",profileService.getSales());
        model.addAttribute("games", userService.getGamesByActualUser());
        model.addAttribute("rented",saleService.getRentGamesReturnedFalseAndUser());
        model.addAttribute("message","Game returned");
        return "/profile";
    }
}
