package com.cognixus.todo.repository;

import com.cognixus.todo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {
    /**
     * find user by username
     *
     * @param username user name
     * @return user
     */
    Optional<User> findByUsername(String username);
}
