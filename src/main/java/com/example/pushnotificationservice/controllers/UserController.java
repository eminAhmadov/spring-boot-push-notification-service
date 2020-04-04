package com.example.pushnotificationservice.controllers;

import com.example.pushnotificationservice.entities.User;
import com.example.pushnotificationservice.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<User> getByUserId(
            @RequestParam UUID userId
    ) {
        User user = userService.getByUserId(userId).orElse(null);
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

}
