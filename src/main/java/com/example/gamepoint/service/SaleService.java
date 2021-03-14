package com.example.gamepoint.service;

import com.example.gamepoint.dto.RentGameDto;
import com.example.gamepoint.dto.SaleDetailsDto;
import com.example.gamepoint.dto.SaleDto;
import com.example.gamepoint.model.RentGame;
import com.example.gamepoint.model.User;
import com.example.gamepoint.repository.RentGameRepository;
import com.example.gamepoint.repository.SaleDetailsRepository;
import com.example.gamepoint.repository.SaleRepository;
import com.example.gamepoint.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class SaleService {

    private SaleRepository saleRepository;
    private UserRepository userRepository;
    private SaleDetailsRepository saleDetailsRepository;
    private RentGameRepository rentGameRepository;
    private UserService userService;

    @Transactional
    public List<SaleDto> getSalesByUserUsername(String username) {
        User user = userRepository.findByUsername(username).get();
        return saleRepository.getSaleByUser(user).stream()
                .map(sale -> SaleDto.builder()
                        .id(sale.getId())
                        .date(sale.getDate())
                        .total(sale.getTotal())
                        .totalGames(sale.getTotalGames())
                        .build())
                .collect(Collectors.toList());
    }

    @Transactional
    public List<SaleDto> getSalesByUserId(int userId) {
        User user = userRepository.findById(userId).get();
        return saleRepository.getSaleByUser(user).stream()
                .map(sale -> SaleDto.builder()
                        .id(sale.getId())
                        .date(sale.getDate())
                        .total(sale.getTotal())
                        .totalGames(sale.getTotalGames())
                        .build())
                .collect(Collectors.toList());
    }

    @Transactional
    public SaleDto getSaleById(int saleId) {
        return saleRepository.findById(saleId).map(sale -> SaleDto.builder()
                .id(sale.getId())
                .date(sale.getDate())
                .total(sale.getTotal())
                .totalGames(sale.getTotalGames())
                .build())
                .get();
    }

    @Transactional
    public List<SaleDetailsDto> getSalesDetailsBySaleId(int saleId) {
        return saleRepository.findById(saleId).get().getSaleDetails()
                .stream().map(saleDetails ->
                        SaleDetailsDto.builder()
                                .id(saleDetails.getId())
                                .gameImg(saleDetails.getGame().getImgUrl())
                                .gameName(saleDetails.getGame().getName())
                                .quantity(saleDetails.getQuantity())
                                .total(saleDetails.getTotal())
                                .build())
                .collect(Collectors.toList());
    }

    @Transactional
    public List<RentGameDto> getRentGamesBySaleId(int saleId){
        return saleRepository.findById(saleId).get().getSaleRentGames()
                .stream().map(rentGame ->
                    RentGameDto.builder()
                            .id(rentGame.getId())
                            .gameImg(rentGame.getGame().getImgUrl())
                            .quantity(rentGame.getQuantity())
                            .total(rentGame.getTotal())
                            .firstMonth(rentGame.getFirstMonth())
                            .lastMonth(rentGame.getLasthMonth())
                            .wasReturned(rentGame.getWasReturned() == 1 ? true : false)
                            .gameName(rentGame.getGame().getName())
                            .build())
                .collect(Collectors.toList());
    }

    @Transactional
    public List<SaleDetailsDto> getSaleDetailsByGameId(int gameId){
        return saleDetailsRepository.findSaleDetailsByGame_Id(gameId)
                .stream().map(saleDetails ->
                        SaleDetailsDto.builder()
                                .id(saleDetails.getId())
                                .gameImg(saleDetails.getGame().getImgUrl())
                                .gameName(saleDetails.getGame().getName())
                                .quantity(saleDetails.getQuantity())
                                .total(saleDetails.getTotal())
                                .username(saleDetails.getSale().getUser().getUsername())
                                .userId(saleDetails.getSale().getUser().getId())
                                .build())
                .collect(Collectors.toList());
    }

    @Transactional
    public List<RentGameDto> getSaleDetailsReturnedFalseAndUser(){
        UserDetails user = userService.getActualUser();
        User u = userRepository.findByUsername(user.getUsername()).get();
        List<RentGame> rented =  rentGameRepository.findByQuery(u);
        return rented.stream().map(rentGame ->
                        RentGameDto.builder()
                                .id(rentGame.getId())
                                .gameImg(rentGame.getGame().getImgUrl())
                                .quantity(rentGame.getQuantity())
                                .total(rentGame.getTotal())
                                .firstMonth(rentGame.getFirstMonth())
                                .lastMonth(rentGame.getLasthMonth())
                                .gameId(rentGame.getGame().getId())
                                //.wasReturned(rentGame.getWasReturned() == 1 ? true : false)
                                .gameName(rentGame.getGame().getName())
                                .build())
                .collect(Collectors.toList());
    }
}
