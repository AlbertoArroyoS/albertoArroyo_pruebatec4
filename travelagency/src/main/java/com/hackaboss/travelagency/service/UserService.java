package com.hackaboss.travelagency.service;

import com.hackaboss.travelagency.dto.request.UserDTORequest;
import com.hackaboss.travelagency.exception.EntityNotFoundException;
import com.hackaboss.travelagency.model.User;
import com.hackaboss.travelagency.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
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
        User userNew = userRepository.findByDni(user.getDni())
                .orElseThrow(() -> new EntityNotFoundException("No existe el usuario"));
        return userRepository.save(userNew);
    }

    public User findOrCreateUserByDni(UserDTORequest hostDTO) {
        return userRepository.findByDni(hostDTO.getDni())
                .orElseGet(() -> {
                    User newUser = User.builder()
                            .name(hostDTO.getName())
                            .surname(hostDTO.getSurname())
                            .phone(hostDTO.getPhone())
                            .dni(hostDTO.getDni())
                            .username(hostDTO.getDni())
                            .build();
                    return userRepository.save(newUser);
                });
    }
}
