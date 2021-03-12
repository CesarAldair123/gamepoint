package com.example.gamepoint.service;

import com.example.gamepoint.dto.*;
import com.example.gamepoint.model.Game;
import com.example.gamepoint.model.Sale;
import com.example.gamepoint.model.SaleDetails;
import com.example.gamepoint.model.User;
import com.example.gamepoint.repository.GameRepository;
import com.example.gamepoint.repository.SaleRepository;
import com.example.gamepoint.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class CartService {

    private GameRepository gameRepository;
    private UserRepository userRepository;
    private SaleRepository saleRepository;

    public void update(UpdateCartRequest updateCartRequest, HttpSession session) {
        Cart cart = getCartInSession(session);
        CartItem cartItem = getCartItemToUpdate(updateCartRequest.getGameId(), cart);
        cart.setTotalGames(cart.getTotalGames() - cartItem.getQuantity());
        cart.setTotal(cart.getTotal() - cartItem.getTotal());
        cartItem.setQuantity(updateCartRequest.getQuantity());
        cartItem.setTotal(cartItem.getQuantity() * cartItem.getGame().getPrice());
        cart.setTotalGames(cart.getTotalGames() + cartItem.getQuantity());
        cart.setTotal(cart.getTotal() + cartItem.getTotal());
        if(cartItem.getQuantity() == 0){
            cart.getItems().remove(cartItem);
        }
        if(cart.getItems().size() == 0){
            session.removeAttribute("cart");
        }
    }

    private Cart getCartInSession(HttpSession session){
        if (session.getAttribute("cart") == null) {
            Cart cart = new Cart();
            session.setAttribute("cart", cart);
            return cart;
        } else {
            return (Cart) session.getAttribute("cart");
        }
    }

    @Transactional
    public CartItem getCartItemToUpdate(int gameId, Cart cart){
        for (CartItem cartItem : cart.getItems()) {
            if (cartItem.getGame().getId() == gameId) {
                return  cartItem;
            }
        }
        Game game = gameRepository.findById(gameId).get();
        CartItem cartItem = new CartItem();
        cartItem.setGame(game);
        cart.getItems().add(cartItem);
        return cartItem;
    }

    @Transactional
    public SaleDto finishOrder(HttpSession session){
        Cart cart = (Cart) session.getAttribute("cart");
        UserDetails userDetails = (UserDetails) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
        User user = userRepository.findByUsername(userDetails.getUsername()).get();
        Sale sale = new Sale();
        sale.setDate(new Date());
        sale.setUser(user);
        sale.setTotal(cart.getTotal());
        sale.setTotalGames(cart.getTotalGames());
        sale.setSaleDetails(new ArrayList<>());
        for(CartItem cartItem : cart.getItems()){
            SaleDetails saleDetails = new SaleDetails();
            saleDetails.setGame(cartItem.getGame());
            saleDetails.setQuantity(cartItem.getQuantity());
            saleDetails.setTotal(cartItem.getTotal());
            saleDetails.setSale(sale);
            sale.getSaleDetails().add(saleDetails);
        }
        session.removeAttribute("cart");
        Sale savedSale = saleRepository.save(sale);
        return SaleDto.builder()
                .id(savedSale.getId())
                .totalGames(savedSale.getTotalGames())
                .date(savedSale.getDate())
                .total(savedSale.getTotal())
                .build();
    }

}
