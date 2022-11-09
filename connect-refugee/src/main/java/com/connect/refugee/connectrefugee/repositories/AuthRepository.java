package com.connect.refugee.connectrefugee.repositories;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.connect.refugee.connectrefugee.models.User;

public interface AuthRepository extends JpaRepository<User, Long>{
    Optional<User> findByUsername(String username);
    Boolean existsByUsername(String username);
    Boolean existsByUsernameIgnoreCase(String username);
}
