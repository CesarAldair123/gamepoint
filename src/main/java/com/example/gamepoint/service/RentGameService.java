package com.example.gamepoint.service;

import com.example.gamepoint.dto.RentGameRequest;
import com.example.gamepoint.model.Game;
import com.example.gamepoint.model.RentGame;
import com.example.gamepoint.model.User;
import com.example.gamepoint.model.UserDetailsImpl;
import com.example.gamepoint.repository.GameRepository;
import com.example.gamepoint.repository.RentGameRepository;
import com.example.gamepoint.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;

@AllArgsConstructor
@Service
public class RentGameService {

    private RentGameRepository rentGameRepository;
    private UserRepository userRepository;
    private GameRepository gameRepository;
    private UserService userService;

    public void returnGame(int rentGameId){
        RentGame rentGame = rentGameRepository.findById(rentGameId).get();
        rentGame.setWasReturned(1);
    }
}
