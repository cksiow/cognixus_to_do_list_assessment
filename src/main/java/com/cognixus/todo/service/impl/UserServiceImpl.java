package com.cognixus.todo.service.impl;


import com.cognixus.todo.exception.BadRequestException;
import com.cognixus.todo.model.User;
import com.cognixus.todo.repository.UserRepo;
import com.cognixus.todo.service.api.UserService;
import com.cognixus.todo.snowflake.SnowflakeHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

/**
 * User service for saving and listing
 */
@Service
@Transactional
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    PasswordEncoder passwordEncoder;


    @Override
    public List<User> saveAll(List<User> users) {

        log.info("save users: {}", users);
        //set id when it is new role insert
        SnowflakeHelper.assignLongIds(users);
        //encode password by encoder and assign role by roles name
        encodePwdAssignRoles(users);
        //save and return the saved data
        return userRepo.saveAll(users);
    }

    @Override
    public User findByUsername(String username) {
        log.info("get user: {}", username);
        //find the user, if not found throw error
        return userRepo.findByUsername(username)
                .orElseThrow(() -> new BadRequestException("User not exists"));
    }


    @Override
    public UserDetails loadUserByUsername(String username) {
        log.info("load user for auth: {}", username);
        User user = this.findByUsername(username);
        //todo: future need implement user key in password
        return new org.springframework.security.core.userdetails.User(user.getUsername(), passwordEncoder.encode("test"), Collections.emptyList());
    }

    @Override
    public User processOAuthPostLogin(String username) {
        User existUser = userRepo.findByUsername(username).orElse(null);

        if (existUser == null) {
            User newUser = new User();
            newUser.setUsername(username);
            this.saveAll(List.of(newUser));
        }
        return userRepo.findByUsername(username).orElse(null);
    }


    private void encodePwdAssignRoles(List<User> users) {
        //todo: for future when allow standalone login or with roles we need encoded the raw password for verification
    }
}
