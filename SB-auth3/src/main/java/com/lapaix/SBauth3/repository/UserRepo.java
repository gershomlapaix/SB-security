package com.lapaix.SBauth3.repository;

import com.lapaix.SBauth3.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepo extends JpaRepository<User,Long> {
    public Optional<User> findByEmail(String email);
}
