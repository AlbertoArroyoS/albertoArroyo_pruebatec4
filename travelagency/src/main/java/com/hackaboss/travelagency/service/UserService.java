package com.hackaboss.travelagency.service;

import com.hackaboss.travelagency.model.User;
import com.hackaboss.travelagency.repository.UserRepository;

import java.util.Optional;

public class UserService implements IUserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Optional<User> findByDni(String dni) {
        return userRepository.findByDni(dni);
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }
}
