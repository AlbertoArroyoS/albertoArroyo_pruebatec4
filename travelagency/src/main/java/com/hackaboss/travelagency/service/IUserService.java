package com.hackaboss.travelagency.service;

import com.hackaboss.travelagency.model.User;

import java.util.Optional;

public interface IUserService {

    Optional<User> findByDni(String dni);
    User save(User user);

}
