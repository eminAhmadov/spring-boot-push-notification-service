package com.example.pushnotificationservice.controllers;

import com.example.pushnotificationservice.entities.User;
import com.example.pushnotificationservice.services.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("user")
public class UserController {

    final
    UserService userService;

    public UserController(
            UserService userService
    ) {
        this.userService = userService;
    }

    @PostMapping("/add")
    public User add(
            @RequestBody User user
    ) {
        return userService.add(user);
    }

    @GetMapping("/find")
    public User getByUserId(
            @RequestParam UUID userId
    ) {
        return userService.getByUserId(userId).orElse(null);
    }

    @GetMapping("/getAllUserIds")
    public List<UUID> getAllUserIds() {
        return  userService.getAllUserIds();
    }

}
