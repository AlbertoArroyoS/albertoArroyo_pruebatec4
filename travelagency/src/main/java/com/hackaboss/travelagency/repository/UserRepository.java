package com.hackaboss.travelagency.repository;

import com.hackaboss.travelagency.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
