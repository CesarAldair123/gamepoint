package com.example.gamepoint.service;

import com.example.gamepoint.dto.SignupRequest;
import com.example.gamepoint.model.User;
import com.example.gamepoint.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@AllArgsConstructor
@Service
public class AuthenticationService {

    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private UserRepository userRepository;

    @Transactional
    public void signup(SignupRequest signupRequest){
        createGamepointAccount();
        User user = User.builder()
                .username(signupRequest.getUsername())
                .email(signupRequest.getEmail())
                .password(bCryptPasswordEncoder.encode(signupRequest.getPassword()))
                .role("USER")
                .build();
        userRepository.save(user);
    }

    @Transactional
    public void createGamepointAccount(){
        if(!userRepository.existsById(1)){
            User user = User.builder()
                    .username("Gamepoint")
                    .email("gamepoint@gmail.com")
                    .password(bCryptPasswordEncoder.encode("gamepoint"))
                    .role("ADMIN")
                    .build();
            userRepository.save(user);
        }
    }

    @Transactional
    public boolean actualUserIsAdmin(){
        UserDetails userDetails = (UserDetails) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
        User user = userRepository.findByUsername(userDetails.getUsername()).get();
        if(user.getRole().equals("ADMIN")){
            return true;
        }else{
            return false;
        }
    }
}
