package com.example.gamepoint.service;

import com.example.gamepoint.dto.GameDto;
import com.example.gamepoint.model.User;
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
public class MyGamesService {

    private UserRepository userRepository;

    /*@Transactional
    public List<GameDto> getGamesFromActualUser(){
        UserDetails userDetails = (UserDetails) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
        User user = userRepository.findByUsername(userDetails.getUsername()).get();
        return user.getGames().stream()
                .map(game -> GameDto.builder()
                        .id(game.getId())
                        .name(game.getName())
                        .price(game.getPrice())
                        .imgUrl(game.getImgUrl())
                        .desc(game.getDescription())
                        .build())
                .collect(Collectors.toList());
    }*/
}
