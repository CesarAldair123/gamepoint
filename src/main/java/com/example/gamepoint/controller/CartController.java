package com.example.gamepoint.controller;

import com.example.gamepoint.dto.Cart;
import com.example.gamepoint.dto.SaleDto;
import com.example.gamepoint.dto.UpdateCartRequest;
import com.example.gamepoint.service.CartService;
import com.example.gamepoint.service.GameService;
import com.example.gamepoint.service.SaleService;
import com.example.gamepoint.service.SearchGameService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

@AllArgsConstructor
@Controller
@RequestMapping("/cart")
public class CartController {

    private CartService cartService;
    private GameService gameService;
    private SaleService saleService;

    @GetMapping
    public String getCart(){
        return "/cart";
    }

    @PostMapping("/add")
    public String postAdd(UpdateCartRequest updateCartRequest, HttpSession session, Model model){
        cartService.update(updateCartRequest,session);
        model.addAttribute("game",gameService.getGameById(updateCartRequest.getGameId()));
        return "/game";
    }

    @PostMapping("/update")
    public String postUpdate(UpdateCartRequest updateCartRequest, HttpSession session){
        cartService.update(updateCartRequest, session);
        return "/cart";
    }

    @PostMapping("/finish")
    public String postUpdate(HttpSession session, Model model){
        SaleDto saleDto = cartService.finishOrder(session);
        if(saleDto == null){
            model.addAttribute("message","Cart changed because some items are already out of stock!");
            return "/cart";
        }
        model.addAttribute("order", saleDto);
        model.addAttribute("details",saleService.getSalesDetailsBySaleId(saleDto.getId()));
        model.addAttribute("rents",saleService.getRentGamesBySaleId(saleDto.getId()));
        return "/order";
    }
}
