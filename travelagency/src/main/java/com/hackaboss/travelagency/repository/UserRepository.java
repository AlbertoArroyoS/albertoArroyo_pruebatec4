package com.hackaboss.travelagency.repository;

import com.hackaboss.travelagency.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByDni(String dni);
}
