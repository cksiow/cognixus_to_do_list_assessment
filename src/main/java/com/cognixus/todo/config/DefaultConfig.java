package com.cognixus.todo.config;

import com.cognixus.todo.model.Todo;
import com.cognixus.todo.model.User;
import com.cognixus.todo.service.api.TodoService;
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

    @Autowired
    private TodoService todoService;

    /**
     * Insert initial data for testing purpose
     */
    @PostConstruct
    void insertTestData() {


        // insert users
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
        //insert to do list for complete and pending
        todoService.saveAll(
                List.of(
                        Todo.builder()
                                .id(1L)
                                .name("completed01")
                                .description("completed task 01")
                                .completed(true)
                                .createdBy(1L)
                                .build(),
                        Todo.builder()
                                .id(2L)
                                .name("completed02")
                                .description("completed task 02")
                                .completed(true)
                                .createdBy(1L)
                                .build()
                        ,
                        Todo.builder()
                                .id(3L)
                                .name("pending01")
                                .description("pending task 01")
                                .completed(false)
                                .createdBy(1L)
                                .build()
                        ,
                        Todo.builder()
                                .id(4L)
                                .name("delete01")
                                .description("delete task 01")
                                .completed(false)
                                .createdBy(2L)
                                .build()
                        ,
                        Todo.builder()
                                .id(5L)
                                .name("update01")
                                .description("update task 01")
                                .completed(false)
                                .createdBy(2L)
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
