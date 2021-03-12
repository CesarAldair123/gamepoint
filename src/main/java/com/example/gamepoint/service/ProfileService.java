package com.example.gamepoint.service;

import com.example.gamepoint.dto.*;
import com.example.gamepoint.model.Sale;
import com.example.gamepoint.model.User;
import com.example.gamepoint.model.UserDetailsImpl;
import com.example.gamepoint.repository.SaleRepository;
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
public class ProfileService {

    private UserRepository userRepository;
    private SaleService saleService;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Transactional
    public UserDto getActualUser() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
        User user = userRepository.findByUsername(userDetails.getUsername()).get();
        return UserDto.builder()
                .username(user.getUsername())
                .email(user.getEmail())
                .build();
    }

    @Transactional
    public void saveProfile(UserDto userDto) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
        User user = userRepository.findByUsername(userDetails.getUsername()).get();
        user.setUsername(userDto.getUsername());
        user.setEmail(userDto.getEmail());
        ((UserDetailsImpl) userDetails).setUsername(userDto.getUsername());
    }

    @Transactional
    public String updatePassword(UpdatePasswordRequest updatePasswordRequest) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
        if (!bCryptPasswordEncoder.matches(updatePasswordRequest.getActualPassword(), userDetails.getPassword())) {
            return "Password is incorrect";
        }
        if (!updatePasswordRequest.getNewPassword().equals(updatePasswordRequest.getRepeatPassword())) {
            return "New Password doesn't match";
        }
        User user = userRepository.findByUsername(userDetails.getUsername()).get();
        String newPassword = bCryptPasswordEncoder.encode(updatePasswordRequest.getNewPassword());
        user.setPassword(newPassword);
        ((UserDetailsImpl) userDetails).setPassword(newPassword);
        return "Password Updated Correctly";
    }

    @Transactional
    public List<SaleDto> getSales() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
        return saleService.getSalesByUserUsername(userDetails.getUsername());
    }

    @Transactional
    public SaleDto getSale(int id) {
        return saleService.getSaleById(id);
    }

    @Transactional
    public List<SaleDetailsDto> getSaleDetails(int id) {
        return saleService.getSalesDetailsBySaleId(id);
    }
}
