package com.example.gamepoint.service;

import com.example.gamepoint.dto.GameDto;
import com.example.gamepoint.model.Game;
import com.example.gamepoint.repository.GameRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class SearchGameService {

    private GameService gameService;

    public List<GameDto> searchGames(String name){
        if(name==null || name.isEmpty()){
            return gameService.getAllGames();
        }else{
            return gameService.getGamesByExpression(name);
        }
    }
}
