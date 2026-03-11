package com.service.command.users.repository;

import com.service.command.users.models.Users;
import org.apache.catalina.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsersRepository extends JpaRepository<Users,Long> {
    Optional<Users> findByUsername(String username);

    boolean existsUsersByUsername(String username);

    Users getUsersByUsername(String username);
}