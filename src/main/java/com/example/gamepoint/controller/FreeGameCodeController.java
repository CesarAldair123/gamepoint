package com.example.gamepoint.controller;

import com.example.gamepoint.dto.FreeGameCodeDto;
import com.example.gamepoint.model.Sale;
import com.example.gamepoint.service.FreeGameCodeService;
import com.example.gamepoint.service.ProfileService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@AllArgsConstructor
@Controller
@RequestMapping("/code")
public class FreeGameCodeController {

    private FreeGameCodeService freeGameCodeService;
    private ProfileService profileService;

    @GetMapping
    public String getCode(){
        return "/changeGameCode";
    }

    @PostMapping("/change")
    public String postChange(String gameCode, Model model){
        String status = freeGameCodeService.checkGameCode(gameCode);
        if(!status.equals("Correct Code")){
            model.addAttribute("message",status);
            return "/changeGameCode";
        }
        model.addAttribute("code",freeGameCodeService.getGameFromGameCode(gameCode));
        return "/changeGameCode";
    }

    @PostMapping("/finish")
    public String postFinish(FreeGameCodeDto freeGameCodeDto, Model model){
        Sale sale = freeGameCodeService.changeGameCode(freeGameCodeDto);
        model.addAttribute("details", profileService.getSaleDetails(sale.getId()));
        model.addAttribute("order",profileService.getSale(sale.getId()));
        return "/order";
    }
}
