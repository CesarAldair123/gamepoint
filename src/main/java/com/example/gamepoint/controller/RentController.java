package com.example.gamepoint.controller;

import com.example.gamepoint.dto.RentGameRequest;
import com.example.gamepoint.service.CartService;
import com.example.gamepoint.service.GameService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

@AllArgsConstructor
@Controller
@RequestMapping("/rent")
public class RentController {

    private CartService cartService;
    private GameService gameService;

    @PostMapping("/add")
    public String postRentAdd(RentGameRequest rentGameRequest, HttpSession session, Model model){
        cartService.updateRentItemToCart(rentGameRequest, session);
        model.addAttribute("game",gameService.getGameById(rentGameRequest.getGameId()));
        return "/game";
    }

    @PostMapping("/update")
    public String postRentUpdate(RentGameRequest rentGameRequest, HttpSession session){
        cartService.updateRentItemToCart(rentGameRequest, session);
        return "/cart";
    }
}
