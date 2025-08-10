package com.example.userapi.controller;

import com.example.userapi.model.Country;
import com.example.userapi.model.User;
import com.example.userapi.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/user-api/v1")
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    /** 1. Все пользователи */
    @GetMapping("/users")
    public List<User> getAll() {
        return service.findAll();
    }

    /** 2. Добавить пользователя */
    @PostMapping("/users")
    @ResponseStatus(HttpStatus.CREATED)
    public User add(@RequestBody User user) {
        return service.save(user);
    }

    /** 3. Фильтр по странам (через запятую) */
    @GetMapping("/additional-info")
    public List<User> byCountries(@RequestParam String countries) {
        List<Country> list = Arrays.stream(countries.split(","))
                .map(String::trim)
                .map(String::toUpperCase)
                .map(Country::valueOf)
                .collect(Collectors.toList());
        return service.findByCountries(list);
    }
}