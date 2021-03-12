package com.example.gamepoint.service;

import com.example.gamepoint.dto.GameDto;
import com.example.gamepoint.model.Game;
import com.example.gamepoint.model.User;
import com.example.gamepoint.repository.GameRepository;
import com.example.gamepoint.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class GameService{

    private UserRepository userRepository;
    private GameRepository gameRepository;

    @Transactional
    public List<GameDto> getAllGames(){
        return gameRepository.findAll().stream()
                .map(game -> GameDto.builder()
                        .id(game.getId())
                        .name(game.getName())
                        .devName(game.getDeveloper())
                        .price(game.getPrice())
                        .imgUrl(game.getImgUrl())
                        .desc(game.getDescription())
                        .forRental(game.getForRental() == 1? true : false)
                        .provider(game.getProvider().getUsername())
                        .pricePerMonth(game.getPricePerMonth())
                        .stock(game.getStock())
                        .build())
                .collect(Collectors.toList());
    }

    @Transactional
    public GameDto getGameById(int id){
        return gameRepository.findById(id).map(game -> GameDto.builder()
                .id(game.getId())
                .name(game.getName())
                .devName(game.getDeveloper())
                .price(game.getPrice())
                .imgUrl(game.getImgUrl())
                .desc(game.getDescription())
                .forRental(game.getForRental() == 1? true : false)
                .provider(game.getProvider().getUsername())
                .pricePerMonth(game.getPricePerMonth())
                .stock(game.getStock())
                .build()).get();
    }

    /*@Transactional
    public GameDto getGameByName(String name){
        return gameRepository.findGameByName(name).map(game -> GameDto.builder()
                .id(game.getId())
                .name(game.getName())
                .devName(game.getDeveloper())
                .price(game.getPrice())
                .imgUrl(game.getImgUrl())
                .desc(game.getDescription())
                .forRental(game.getForRental() == 1? true : false)
                .provider(game.getProvider().getUsername())
                .pricePerMonth(game.getPricePerMonth())
                .stock(game.getStock())
                .build()).get();
    }*/

    @Transactional
    public List<GameDto> getGamesByExpression(String name){
        return gameRepository.findGamesByExpression(name).stream()
                .map(game -> GameDto.builder()
                        .id(game.getId())
                        .name(game.getName())
                        .devName(game.getDeveloper())
                        .price(game.getPrice())
                        .imgUrl(game.getImgUrl())
                        .desc(game.getDescription())
                        .forRental(game.getForRental() == 1? true : false)
                        .provider(game.getProvider().getUsername())
                        .pricePerMonth(game.getPricePerMonth())
                        .stock(game.getStock())
                        .build())
                .collect(Collectors.toList());
    }


    @Transactional
    public void updateGame(GameDto gameDto){
        Game game = gameRepository.findById(gameDto.getId()).get();
        game.setName(gameDto.getName());
        game.setDescription(gameDto.getDesc());
        game.setDeveloper(gameDto.getDevName());
        game.setPrice(gameDto.getPrice());
        game.setImgUrl(gameDto.getImgUrl());
        game.setPricePerMonth(gameDto.getPricePerMonth());
        game.setForRental(gameDto.isForRental() ? 1 : 0);
        game.setStock(gameDto.getStock());
    }

    @Transactional
    public int addGame(GameDto gameDto){
        UserDetails userDetails = (UserDetails) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
        User user = userRepository.findByUsername(userDetails.getUsername()).get();
        Game game = Game.builder()
                .name(gameDto.getName())
                .description(gameDto.getDesc())
                .developer(gameDto.getDevName())
                .price(gameDto.getPrice())
                .imgUrl(gameDto.getImgUrl())
                .forRental(gameDto.isForRental() ? 1 : 0)
                .pricePerMonth(gameDto.getPricePerMonth())
                .stock(gameDto.getStock())
                .provider(user)
                .build();
        return gameRepository.save(game).getId();
    }
}
