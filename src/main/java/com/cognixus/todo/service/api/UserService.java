package com.cognixus.todo.service.api;

import com.cognixus.todo.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

/**
 * User service interface extend UserDetailsService for auth user retrieve purpose
 */
public interface UserService extends UserDetailsService {
    /**
     * Save all users
     *
     * @param users input
     * @return saved data
     */
    List<User> saveAll(List<User> users);

    /**
     * get user by name
     *
     * @param username name
     * @return user data
     */
    User findByUsername(String username);

    /**
     * remove user by name
     *
     * @param username name
     */


    /**
     * google login success handling
     */
    User processOAuthPostLogin(String username);
}
