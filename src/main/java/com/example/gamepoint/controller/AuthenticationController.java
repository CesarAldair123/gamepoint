package com.example.gamepoint.controller;

import com.example.gamepoint.dto.SignupRequest;
import com.example.gamepoint.service.AuthenticationService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.sql.SQLIntegrityConstraintViolationException;

@AllArgsConstructor
@Controller
public class AuthenticationController {

    private AuthenticationService authenticationService;

    @GetMapping("/login")
    public String getLogin(){
        return "/login";
    }

    @GetMapping("/signup")
    public String getSignup(){
        return "/signup";
    }

    @PostMapping("/signup")
    public String postSignup(SignupRequest signupRequest, Model model){
        try{
            authenticationService.signup(signupRequest);
        }catch(Exception e){
            model.addAttribute("message","Error");
            return "/signup";
        }
        model.addAttribute("message","Account Created Correctly, Login to enter");
        return "/login";
    }
}
