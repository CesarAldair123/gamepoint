package com.example.gamepoint.service;

import com.example.gamepoint.dto.GameDto;
import com.example.gamepoint.dto.UserDto;
import com.example.gamepoint.model.Game;
import com.example.gamepoint.model.User;
import com.example.gamepoint.model.UserDetailsImpl;
import com.example.gamepoint.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class UserService {

    private UserRepository userRepository;
    private BCryptPasswordEncoder passwordEncoder;

    @Transactional
    public List<GameDto> getGamesByActualUser(){
        UserDetails userDetails = (UserDetails) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
        User user = userRepository.findByUsername(userDetails.getUsername()).get();
        return user.getGames().stream()
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
    public List<UserDto> getAllUsers(){
        return userRepository.findAll().stream()
                .map(user->UserDto.builder()
                        .id(user.getId())
                        .username(user.getUsername())
                        .email(user.getUsername())
                        .role(user.getRole())
                        .build())
                .collect(Collectors.toList());
    }

    @Transactional
    public UserDto getUserById(int id){
        return userRepository.findById(id)
                .map(user->UserDto.builder()
                        .id(user.getId())
                        .username(user.getUsername())
                        .email(user.getEmail())
                        .role(user.getRole())
                        .build()).get();
    }

    @Transactional
    public UserDto getUserByUsername(String username){
        return userRepository.findByUsername(username)
                .map(user->UserDto.builder()
                        .id(user.getId())
                        .username(user.getUsername())
                        .email(user.getEmail())
                        .role(user.getRole())
                        .build()).get();
    }

    @Transactional
    public void updateUser(UserDto userDto){
        boolean actualUser = false;
        UserDetails userDetails = (UserDetails) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
        User user = userRepository.findById(userDto.getId()).get();
        if(userDetails.getUsername().equals(user.getUsername())){
            actualUser = true;
        }
        user.setUsername(userDto.getUsername());
        user.setEmail(userDto.getEmail());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setRole(userDto.getRole());
        if(actualUser){
            ((UserDetailsImpl)userDetails).setUsername(userDto.getUsername());
        }
    }

    @Transactional
    public void addUser(UserDto userDto){
        User user = User.builder()
                .username(userDto.getUsername())
                .email(userDto.getEmail())
                .password(passwordEncoder.encode(userDto.getPassword()))
                .role(userDto.getRole())
                .build();
        userRepository.save(user);
    }
}
