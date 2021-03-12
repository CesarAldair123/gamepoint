package com.example.gamepoint.service;

import com.example.gamepoint.dto.GameDto;
import com.example.gamepoint.model.Developer;
import com.example.gamepoint.model.Game;
import com.example.gamepoint.repository.DeveloperRepository;
import com.example.gamepoint.repository.GameRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class GameService{

    private GameRepository gameRepository;
    private DeveloperRepository developerRepository;

    @Transactional
    public List<GameDto> getAllGames(){
        return gameRepository.findAll().stream()
                .map(game -> GameDto.builder()
                        .id(game.getId())
                        .name(game.getName())
                        .devName(game.getDeveloper().getName())
                        .price(game.getPrice())
                        .imgUrl(game.getImgUrl())
                        .desc(game.getDescription())
                        .build())
                .collect(Collectors.toList());
    }

    @Transactional
    public GameDto getGameById(int id){
        return gameRepository.findById(id).map(game -> GameDto.builder()
                .id(game.getId())
                .name(game.getName())
                .devName(game.getDeveloper().getName())
                .price(game.getPrice())
                .imgUrl(game.getImgUrl())
                .desc(game.getDescription())
                .build()).get();
    }

    @Transactional
    public GameDto getGameByName(String name){
        return gameRepository.findGameByName(name).map(game -> GameDto.builder()
                .id(game.getId())
                .name(game.getName())
                .devName(game.getDeveloper().getName())
                .price(game.getPrice())
                .imgUrl(game.getImgUrl())
                .desc(game.getDescription())
                .build()).get();
    }

    @Transactional
    public List<GameDto> getGamesByExpression(String name){
        return gameRepository.findGamesByExpression(name).stream()
                .map(game -> GameDto.builder()
                        .id(game.getId())
                        .name(game.getName())
                        .devName(game.getDeveloper().getName())
                        .price(game.getPrice())
                        .imgUrl(game.getImgUrl())
                        .desc(game.getDescription())
                        .build())
                .collect(Collectors.toList());
    }


    @Transactional
    public void updateGame(GameDto gameDto){
        Game game = gameRepository.findById(gameDto.getId()).get();
        Developer developer = developerRepository.findDeveloperByName(gameDto.getDevName()).get();
        game.setName(gameDto.getName());
        game.setDescription(gameDto.getDesc());
        game.setDeveloper(developer);
        game.setPrice(gameDto.getPrice());
        game.setImgUrl(gameDto.getImgUrl());
    }

    @Transactional
    public void addGame(GameDto gameDto){
        Developer developer = developerRepository.findDeveloperByName(gameDto.getDevName()).get();
        Game game = Game.builder()
                .name(gameDto.getName())
                .description(gameDto.getDesc())
                .developer(developer)
                .price(gameDto.getPrice())
                .imgUrl(gameDto.getImgUrl())
                .build();
        gameRepository.save(game);
    }
}
