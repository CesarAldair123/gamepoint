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
        Game game = gameRepository.findById(gameId).get();
        for (CartItem cartItem : cart.getItems()) {
            if (cartItem.getGame().getId() == gameId) {
                cartItem.setGame(game);
                return  cartItem;
            }
        }
        CartItem cartItem = new CartItem();
        cartItem.setGame(game);
        cart.getItems().add(cartItem);
        return cartItem;
    }

    @Transactional
    public SaleDto finishOrder(HttpSession session){
        if(checkOutOfStock(session)) return null;
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
            Game game = gameRepository.findById(cartItem.getGame().getId()).get();
            game.setStock(game.getStock() - cartItem.getQuantity());
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

    @Transactional
    public boolean checkOutOfStock(HttpSession session){
        boolean changed = false;
        Cart cart = ((Cart)session.getAttribute("cart"));
        List<CartItem> items = cart.getItems();
        List<CartItem> cartItemsToRemove = new ArrayList<>();
        for(CartItem item : items){
            Game game = gameRepository.findById(item.getGame().getId()).get();
            if(game.getStock() == 0){
                updateCart(cart,game,item, game.getStock());
                cartItemsToRemove.add(item);
                changed = true;
            }
            if(item.getQuantity() > game.getStock()){
                updateCart(cart,game,item, game.getStock());
                changed = true;
            }
        }
        items.removeAll(cartItemsToRemove);
        if(cart.getItems().size() == 0){
            session.removeAttribute("cart");
        }
        return changed;
    }

    public void updateCart(Cart cart, Game game, CartItem cartItem, int quantity){
        List<CartItem> cartItems = new ArrayList<>();
        if(quantity == 0){
            cart.setTotal(cart.getTotal() - cartItem.getTotal());
            cart.setTotalGames(cart.getTotalGames() - cartItem.getQuantity());
        }else{
            cart.setTotal(cart.getTotal() - cartItem.getTotal());
            cart.setTotalGames(cart.getTotalGames() - cartItem.getQuantity());
            cartItem.setQuantity(quantity);
            cartItem.setTotal(game.getPrice() * quantity);
            cart.setTotal(cart.getTotal() + cartItem.getTotal());
            cart.setTotalGames(cart.getTotalGames() + cartItem.getQuantity());
        }
    }
}
