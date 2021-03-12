package com.example.gamepoint.service;

import com.example.gamepoint.dto.SignupRequest;
import com.example.gamepoint.model.User;
import com.example.gamepoint.repository.UserRepository;
import lombok.AllArgsConstructor;
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
        String role = "USER";
        if(!userRepository.existsById(1)){
            role = "ADMIN";
        }
        User user = User.builder()
                .username(signupRequest.getUsername())
                .email(signupRequest.getEmail())
                .password(bCryptPasswordEncoder.encode(signupRequest.getPassword()))
                .role(role)
                .build();
        userRepository.save(user);
    }
}
