package com.example.gamepoint.controller;

import com.example.gamepoint.dto.DeveloperDto;
import com.example.gamepoint.dto.FreeGameCodeDto;
import com.example.gamepoint.dto.GameDto;
import com.example.gamepoint.dto.UserDto;
import com.example.gamepoint.model.FreeGameCode;
import com.example.gamepoint.service.DeveloperService;
import com.example.gamepoint.service.FreeGameCodeService;
import com.example.gamepoint.service.GameService;
import com.example.gamepoint.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@AllArgsConstructor
@Controller
@RequestMapping("/admin")
public class AdminController {

    private GameService gameService;
    private DeveloperService developerService;
    private UserService userService;
    private FreeGameCodeService freeGameCodeService;

    @GetMapping
    private String getAdmin(){
        return "/admin";
    }

    @GetMapping("/games")
    private String getGames(Model model){
        model.addAttribute("games",gameService.getAllGames());
        return "adminGame";
    }

    @GetMapping("/games/{id}")
    private String getEditGame(@PathVariable int id, Model model){
        model.addAttribute("game",gameService.getGameById(id));
        model.addAttribute("devs",developerService.getAllDevelopers());
        return "editGame";
    }

    @PostMapping("/games/{id}")
    private String postEditGame(GameDto gameDto, Model model){
        gameService.updateGame(gameDto);
        model.addAttribute("game",gameService.getGameById(gameDto.getId()));
        model.addAttribute("devs",developerService.getAllDevelopers());
        return "editGame";
    }

    @GetMapping("/games/add")
    private String getAddGame(Model model){
        model.addAttribute("devs",developerService.getAllDevelopers());
        return "createGame";
    }

    @PostMapping("/games/add")
    private String postAddGame(GameDto gameDto, Model model){
        gameService.addGame(gameDto);
        model.addAttribute("game",gameService.getGameByName(gameDto.getName()));
        model.addAttribute("devs",developerService.getAllDevelopers());
        return "editGame";
    }

    @GetMapping("/users")
    public String getUsers(Model model){
        model.addAttribute("users", userService.getAllUsers());
        return "adminUser";
    }

    @GetMapping("/users/{id}")
    public String getEditUser(@PathVariable int id, Model model){
        model.addAttribute("user",userService.getUserById(id));
        return "editUser";
    }

    @PostMapping("/users/{id}")
    public String postEditUser(UserDto userDto, Model model){
        userService.updateUser(userDto);
        model.addAttribute("user",userService.getUserByUsername(userDto.getUsername()));
        return "editUser";
    }

    @GetMapping("/users/add")
    public String getAddUser(){
        return "createUser";
    }

    @PostMapping("/users/add")
    public String postAddUser(UserDto userDto, Model model){
        userService.addUser(userDto);
        model.addAttribute("user",userService.getUserByUsername(userDto.getUsername()));
        return "editUser";
    }

    @GetMapping("/developers")
    public String getDevelopers(Model model){
        model.addAttribute("developers",developerService.getAllDevelopers());
        return "adminDeveloper";
    }

    @GetMapping("/developers/{id}")
    public String getEditDeveloper(@PathVariable int id, Model model){
        model.addAttribute("developer",developerService.getDeveloperById(id));
        return "editDeveloper";
    }

    @PostMapping("/developers/{id}")
    public String postEditDeveloper(DeveloperDto developerDto, Model model){
        developerService.updateDeveloper(developerDto);
        model.addAttribute("developer",developerService.getDeveloperById(developerDto.getId()));
        return "editDeveloper";
    }

    @GetMapping("/developers/add")
    public String getAddDeveloper(){
        return "createDeveloper";
    }

    @PostMapping("/developers/add")
    public String postAddDeveloper(DeveloperDto developerDto, Model model){
        developerService.addDeveloper(developerDto);
        model.addAttribute("developer",developerService.getDeveloperByUsername(developerDto.getName()));
        return "editDeveloper";
    }

    @GetMapping("/codes")
    public String getCodes(Model model){
        model.addAttribute("codes",freeGameCodeService.getAllGameCodes());
        return "adminGameCode";
    }

    @GetMapping("/codes/add")
    public String getAddCode(Model model){
        model.addAttribute("games", gameService.getAllGames());
        return "/createGameCode";
    }

    @PostMapping ("/codes/add")
    public String postAddCode(FreeGameCodeDto freeGameCodeDto, Model model){
        freeGameCodeService.addGameCode(freeGameCodeDto);
        model.addAttribute("codes",freeGameCodeService.getAllGameCodes());
        return "adminGameCode";
    }
}
