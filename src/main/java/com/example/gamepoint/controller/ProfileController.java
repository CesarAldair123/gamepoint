package com.example.gamepoint.controller;

import com.example.gamepoint.dto.UpdatePasswordRequest;
import com.example.gamepoint.dto.UserDto;
import com.example.gamepoint.service.ProfileService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@AllArgsConstructor
@Controller
public class ProfileController {

    private ProfileService profileService;

    @GetMapping("/profile")
    private String getProfile(Model model){
        model.addAttribute("user", profileService.getActualUser());
        model.addAttribute("orders",profileService.getSales());
        return "/profile";
    }

    @PostMapping("/saveprofile")
    private String saveProfile(UserDto userDto, Model model){
        profileService.saveProfile(userDto);
        model.addAttribute("user", profileService.getActualUser());
        model.addAttribute("orders",profileService.getSales());
        model.addAttribute("profilemessage","Profile Updated!");
        return "/profile";
    }

    @PostMapping("/password")
    private String updatePassword(UpdatePasswordRequest updatePasswordRequest, Model model){
        model.addAttribute("user", profileService.getActualUser());
        model.addAttribute("passwordmessage",profileService.updatePassword(updatePasswordRequest));
        model.addAttribute("orders",profileService.getSales());
        return "/profile";
    }

    @GetMapping("/order/{id}")
    private String getOrder(@PathVariable int id, Model model){
        model.addAttribute("details", profileService.getSaleDetails(id));
        model.addAttribute("order",profileService.getSale(id));
        return "/order";
    }
}
