package com.example.userapi.service;

import com.example.userapi.model.Country;
import com.example.userapi.model.User;
import com.example.userapi.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository repo;

    public UserService(UserRepository repo) {
        this.repo = repo;
    }

    public List<User> findAll() {
        return repo.findAll();
    }

    public User save(User user) {
        return repo.save(user);
    }

    public List<User> findByCountries(List<Country> countries) {
        return repo.findByCountryInOrderByCountryAsc(countries);
    }
}