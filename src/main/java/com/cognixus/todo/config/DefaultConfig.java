package com.cognixus.todo.config;

import com.cognixus.todo.model.User;
import com.cognixus.todo.service.api.UserService;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.List;

@Configuration
public class DefaultConfig {


    @Autowired
    private UserService userService;
    

    /**
     * Insert initial data for testing purpose
     */
    @PostConstruct
    void insertTestData() {


        // insert users with roles
        userService.saveAll(
                List.of(
                        User.builder()
                                .username("ck")

                                .build(),
                        User.builder()
                                .username("staff")

                                .build()
                        ,
                        User.builder()
                                .username("member01")

                                .build()
                        ,
                        User.builder()
                                .username("member02")

                                .build()
                )
        );


    }

    /**
     * Create Object mapper to let it can ser any class that come with field
     *
     * @return Object mapper
     */
    @Bean
    ObjectMapper mapper() {
        var mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        return mapper;
    }

}
