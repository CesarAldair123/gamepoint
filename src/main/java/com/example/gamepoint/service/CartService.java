package com.example.gamepoint.service;

import com.example.gamepoint.dto.*;
import com.example.gamepoint.model.*;
import com.example.gamepoint.repository.GameRepository;
import com.example.gamepoint.repository.SaleRepository;
import com.example.gamepoint.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
        if (cartItem.getQuantity() == 0) {
            cart.getItems().remove(cartItem);
        }
        if (cart.getItems().size() == 0 && cart.getRentItems().size() == 0) {
            session.removeAttribute("cart");
        }
    }

    private Cart getCartInSession(HttpSession session) {
        if (session.getAttribute("cart") == null) {
            Cart cart = new Cart();
            session.setAttribute("cart", cart);
            return cart;
        } else {
            return (Cart) session.getAttribute("cart");
        }
    }

    @Transactional
    public CartItem getCartItemToUpdate(int gameId, Cart cart) {
        Game game = gameRepository.findById(gameId).get();
        for (CartItem cartItem : cart.getItems()) {
            if (cartItem.getGame().getId() == gameId) {
                cartItem.setGame(game);
                return cartItem;
            }
        }
        CartItem cartItem = new CartItem();
        cartItem.setGame(game);
        cart.getItems().add(cartItem);
        return cartItem;
    }

    @Transactional
    public SaleDto finishOrder(HttpSession session) {
        boolean changed = false;
        if (checkOutOfStockFromItemsAndRent(session)) changed = true;
        if(checkOutOfStock(session)) changed = true;
        if(checkOutOfStockRentItem(session)) changed = true;
        if(changed) return null;
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
        sale.setSaleRentGames(new ArrayList<>());
        for (CartItem cartItem : cart.getItems()) {
            Game game = gameRepository.findById(cartItem.getGame().getId()).get();
            game.setStock(game.getStock() - cartItem.getQuantity());
            SaleDetails saleDetails = new SaleDetails();
            saleDetails.setGame(cartItem.getGame());
            saleDetails.setQuantity(cartItem.getQuantity());
            saleDetails.setTotal(cartItem.getTotal());
            saleDetails.setSale(sale);
            sale.getSaleDetails().add(saleDetails);
        }
        for (CartRentItem cartItem : cart.getRentItems()) {
            Game game = gameRepository.findById(cartItem.getGame().getId()).get();
            game.setStock(game.getStock() - cartItem.getQuantity());
            RentGame rentGame = new RentGame();
            rentGame.setGame(cartItem.getGame());
            rentGame.setFirstMonth(new Date());
            rentGame.setLasthMonth(cartItem.getFinalMonth());
            rentGame.setTotal(cartItem.getTotal());
            rentGame.setQuantity(cartItem.getQuantity());
            rentGame.setWasReturned(0);
            rentGame.setSale(sale);
            sale.getSaleRentGames().add(rentGame);
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
    public boolean checkOutOfStock(HttpSession session) {
        boolean changed = false;
        Cart cart = ((Cart) session.getAttribute("cart"));
        List<CartItem> items = cart.getItems();
        List<CartItem> cartItemsToRemove = new ArrayList<>();
        for (CartItem item : items) {
            Game game = gameRepository.findById(item.getGame().getId()).get();
            if (game.getStock() == 0) {
                updateCart(cart, game, item, game.getStock());
                cartItemsToRemove.add(item);
                changed = true;
            }
            if (item.getQuantity() > game.getStock()) {
                updateCart(cart, game, item, game.getStock());
                changed = true;
            }
        }
        items.removeAll(cartItemsToRemove);
        if (cart.getItems().size() == 0 && cart.getRentItems().size() == 0) {
            session.removeAttribute("cart");
        }
        return changed;
    }

    @Transactional
    public boolean checkOutOfStockRentItem(HttpSession session) {
        boolean changed = false;
        Cart cart = ((Cart) session.getAttribute("cart"));
        List<CartRentItem> items = cart.getRentItems();
        List<CartRentItem> cartItemsToRemove = new ArrayList<>();
        for (CartRentItem item : items) {
            Game game = gameRepository.findById(item.getGame().getId()).get();
            if (game.getStock() == 0) {
                updateCartRent(cart, game, item, game.getStock());
                cartItemsToRemove.add(item);
                changed = true;
            }
            if (item.getQuantity() > game.getStock()) {
                updateCartRent(cart, game, item, game.getStock());
                changed = true;
            }
        }
        items.removeAll(cartItemsToRemove);
        if (cart.getItems().size() == 0 && cart.getRentItems().size() == 0) {
            session.removeAttribute("cart");
        }
        return changed;
    }

    @Transactional
    public boolean checkOutOfStockFromItemsAndRent(HttpSession session) {
        boolean changed = false;
        Cart cart = ((Cart) session.getAttribute("cart"));
        List<CartItem> items = cart.getItems();
        List<CartRentItem> rentItems = cart.getRentItems();
        List<CartItem> cartItemsToRemove = new ArrayList<>();
        List<CartRentItem> cartRentItemsToRemove = new ArrayList<>();
        for (CartItem item : items) {
            for(CartRentItem cartRentItem : rentItems){
                Game game = gameRepository.findById(cartRentItem.getGame().getId()).get();
                if(cartRentItem.getGame().getId() == item.getGame().getId()){
                    int rentQ = cartRentItem.getQuantity();
                    int itemQ = item.getQuantity();
                    if(game.getStock() >= itemQ + rentQ){
                        break;
                    }else{
                        int stock = game.getStock();
                        int itemToQuit = 0;
                        int rentToQuit = 0;
                        while(stock > 0){
                            if(itemToQuit <= itemQ){
                                itemToQuit++;
                            }else{
                                rentToQuit++;
                            }
                            stock--;
                        }
                        updateCart(cart, game, item, itemToQuit);
                        updateCartRent(cart, game, cartRentItem, rentToQuit);
                        if(itemToQuit == 0){
                            cartItemsToRemove.add(item);
                        }
                        if(rentToQuit == 0){
                            cartRentItemsToRemove.add(cartRentItem);
                        }
                        changed = true;
                    }
                }
                //Here Item and Rent Item are different
            }
        }
        items.removeAll(cartItemsToRemove);
        rentItems.removeAll(cartRentItemsToRemove);
        if (cart.getItems().size() == 0 && cart.getRentItems().size() == 0) {
            session.removeAttribute("cart");
        }
        return changed;
    }

    public void updateCart(Cart cart, Game game, CartItem cartItem, int quantity) {
        if (quantity == 0) {
            cart.setTotal(cart.getTotal() - cartItem.getTotal());
            cart.setTotalGames(cart.getTotalGames() - cartItem.getQuantity());
        } else {
            cart.setTotal(cart.getTotal() - cartItem.getTotal());
            cart.setTotalGames(cart.getTotalGames() - cartItem.getQuantity());
            cartItem.setQuantity(quantity);
            cartItem.setTotal(game.getPrice() * quantity);
            cart.setTotal(cart.getTotal() + cartItem.getTotal());
            cart.setTotalGames(cart.getTotalGames() + cartItem.getQuantity());
        }
    }

    public void updateCartRent(Cart cart, Game game, CartRentItem cartRentItem, int quantity){
        if (quantity == 0) {
            cart.setTotal(cart.getTotal() - cartRentItem.getTotal());
            cart.setTotalGames(cart.getTotalGames() - cartRentItem.getQuantity());
        } else {
            cart.setTotal(cart.getTotal() - cartRentItem.getTotal());
            cart.setTotalGames(cart.getTotalGames() - cartRentItem.getQuantity());
            cartRentItem.setQuantity(quantity);
            cartRentItem.setTotal(game.getPrice() * quantity);
            cart.setTotal(cart.getTotal() + cartRentItem.getTotal());
            cart.setTotalGames(cart.getTotalGames() + cartRentItem.getQuantity());
        }
    }

    public void updateRentItemToCart(RentGameRequest rentGameRequest, HttpSession session) {
        Cart cart = getCartInSession(session);
        if (rentGameRequest.getQuantity() <= 0) {
            removeRentItem(rentGameRequest, cart);
        } else {
            addRentItem(rentGameRequest, cart);
        }
        if (cart.getItems().size() == 0 && cart.getRentItems().size() == 0) {
            session.removeAttribute("cart");
        }
    }

    public void addRentItem(RentGameRequest rentGameRequest, Cart cart) {
        CartRentItem itemToChange = null;
        Game game = gameRepository.findById(rentGameRequest.getGameId()).get();
        for (CartRentItem cartRentItem : cart.getRentItems()) {
            if (cartRentItem.getGame().getId() == rentGameRequest.getGameId()) {
                itemToChange = cartRentItem;
            }
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date parsed = null;
        try {
            parsed = format.parse(rentGameRequest.getFinalMonth());
        }catch(Exception e){
            e.printStackTrace();
        }
        if (itemToChange != null) {
            cart.setTotalGames(cart.getTotalGames() - itemToChange.getQuantity());
            cart.setTotal(cart.getTotal() - itemToChange.getTotal());
            long monthsBetween = getMonthDifference(new Date(), parsed);
            itemToChange.setTotal(rentGameRequest.getQuantity() * itemToChange.getGame().getPricePerMonth() * monthsBetween);
            itemToChange.setQuantity(rentGameRequest.getQuantity());
            itemToChange.setFinalMonth(parsed);
            itemToChange.setDateInString(rentGameRequest.getFinalMonth());
            cart.setTotalGames(cart.getTotalGames() + itemToChange.getQuantity());
            cart.setTotal(cart.getTotal() + itemToChange.getTotal());
        } else {
            long monthsBetween = getMonthDifference(new Date(), parsed);
            itemToChange = new CartRentItem();
            itemToChange.setGame(game);
            itemToChange.setTotal(rentGameRequest.getQuantity() * itemToChange.getGame().getPricePerMonth() * monthsBetween);
            itemToChange.setQuantity(rentGameRequest.getQuantity());
            itemToChange.setFinalMonth(parsed);
            itemToChange.setDateInString(rentGameRequest.getFinalMonth());
            cart.setTotal(cart.getTotal() + itemToChange.getTotal());
            cart.setTotalGames(cart.getTotalGames() + itemToChange.getQuantity());
            cart.getRentItems().add(itemToChange);
        }
    }

    public void removeRentItem(RentGameRequest rentGameRequest, Cart cart) {
        CartRentItem itemToRemove = null;
        for (CartRentItem cartRentItem : cart.getRentItems()) {
            Game game = cartRentItem.getGame();
            if (game.getId() == rentGameRequest.getGameId()) {
                itemToRemove = cartRentItem;
                cart.setTotalGames(cart.getTotalGames() - cartRentItem.getQuantity());
                cart.setTotal(cart.getTotal() - cartRentItem.getTotal());
                break;
            }
        }
        if (itemToRemove != null) {
            cart.getRentItems().remove(itemToRemove);
        }
    }

    public long getMonthDifference(Date date1, Date date2){
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String date1String = dateFormat.format(date1);
        String date2String = dateFormat.format(date2);
        long monthsBetween = ChronoUnit.MONTHS.between(
                LocalDate.parse(date1String).withDayOfMonth(1),
                LocalDate.parse(date2String).withDayOfMonth(1));
        return monthsBetween + 1;
    }
}
