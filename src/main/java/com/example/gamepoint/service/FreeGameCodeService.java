package com.example.gamepoint.service;

import com.example.gamepoint.dto.FreeGameCodeDto;
import com.example.gamepoint.dto.SaleDetailsDto;
import com.example.gamepoint.model.*;
import com.example.gamepoint.repository.FreeGameCodeRepository;
import com.example.gamepoint.repository.GameRepository;
import com.example.gamepoint.repository.SaleRepository;
import com.example.gamepoint.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class FreeGameCodeService {

    private FreeGameCodeRepository freeGameCodeRepository;
    private GameRepository gameRepository;
    private UserRepository userRepository;
    private SaleRepository saleRepository;

    @Transactional
    public String checkGameCode(String gameCode){
        FreeGameCode freeGameCode = freeGameCodeRepository.findFreeGameCodeByGameCode(gameCode).orElse(null);
        if(freeGameCode == null){
            return "Bad Game Code";
        }
        if(freeGameCode.getIsUsed() == 1){
            return "Code is already used";
        }
        return "Correct Code";
    }

    @Transactional
    public List<FreeGameCodeDto> getAllGameCodes(){
        return freeGameCodeRepository.findAll().stream()
                .map(code->FreeGameCodeDto.builder()
                    .id(code.getId())
                    .code(code.getGameCode())
                        .gameImg(code.getGame().getImgUrl())
                    .gameName(code.getGame().getName())
                    .used(code.getIsUsed() == 0 ? false : true)
                    .build())
                    .collect(Collectors.toList());
    }

    @Transactional
    public void addGameCode(FreeGameCodeDto freeGameCodeDto){
        Game game = gameRepository.findById(freeGameCodeDto.getGameId()).get();
        FreeGameCode freeGameCode = new FreeGameCode();
        freeGameCode.setGameCode(generateRandomCode());
        freeGameCode.setGame(game);
        freeGameCode.setIsUsed(0);
        freeGameCodeRepository.save(freeGameCode);
    }


    @Transactional
    public FreeGameCodeDto getGameFromGameCode(String gameCode){
        FreeGameCode freeGameCode = freeGameCodeRepository.findFreeGameCodeByGameCode(gameCode).orElse(null);
        if(freeGameCode == null) return null;
        FreeGameCodeDto freeGameCodeDto = FreeGameCodeDto.builder()
                .code(freeGameCode.getGameCode())
                .gameName(freeGameCode.getGame().getName())
                .gameImg(freeGameCode.getGame().getImgUrl())
                .id(freeGameCode.getId())
                .used(freeGameCode.getIsUsed() == 0 ? false : true)
                .build();
        return freeGameCodeDto;
    }

    @Transactional
    public Sale changeGameCode(FreeGameCodeDto freeGameCodeDto){
        UserDetails userDetails = (UserDetails) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
        User user = userRepository.findByUsername(userDetails.getUsername()).get();
        Game game = gameRepository.findGameByName(freeGameCodeDto.getGameName()).get();
        FreeGameCode freeGameCode = freeGameCodeRepository.findFreeGameCodeByGameCode(freeGameCodeDto.getCode()).get();
        Sale sale = Sale.builder()
                .date(new Date())
                .total(0)
                .totalGames(1)
                .saleDetails(new ArrayList<>())
                .user(user)
                .build();
        SaleDetails saleDetails = SaleDetails.builder()
                .sale(sale)
                .game(game)
                .quantity(1)
                .total(0)
                .build();
        sale.getSaleDetails().add(saleDetails);
        freeGameCode.setIsUsed(1);
        return saleRepository.save(sale);
    }

    public String generateRandomCode(){
        return UUID.randomUUID().toString();
    }
}
